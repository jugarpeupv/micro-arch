use actix_web::{
    body::BoxBody, dev::Payload, http::header::ContentType, post, web, Either,
    Error as ActixWebError, FromRequest, HttpRequest, HttpResponse, Responder,
};
use base64::prelude::*;
use chrono::{Duration, Utc};
use jsonwebtoken::{decode, encode, DecodingKey, EncodingKey, Header, Validation};
use serde::{Deserialize, Serialize};
use std::future::{ready, Ready};

use crate::AppState;

#[derive(sqlx::FromRow, Debug)]
struct User {
    id: i32,
    email: String,
    password: String,
}

#[derive(Serialize, Deserialize, Debug)]
struct BasicAuthHeader {
    username: String,
    password: String,
}

impl FromRequest for BasicAuthHeader {
    type Error = ActixWebError;

    type Future = Ready<Result<Self, Self::Error>>;

    fn from_request(req: &HttpRequest, _payload: &mut Payload) -> Self::Future {
        let header_value = req.headers().get("Authorization").unwrap().to_str();
        let credentials_base64 = header_value
            .as_ref()
            .unwrap()
            .split_whitespace()
            .nth(1)
            .unwrap();
        let decoded_credentials =
            String::from_utf8(BASE64_STANDARD.decode(credentials_base64).unwrap()).unwrap();
        let username = decoded_credentials.split(":").nth(0).unwrap();
        let password = decoded_credentials.split(":").nth(1).unwrap();

        match header_value {
            Ok(_) => ready(Ok(BasicAuthHeader {
                username: username.to_string(),
                password: password.to_string(),
            })),
            Err(err) => ready(Err(actix_web::error::ErrorUnauthorized(err))),
        }
    }
}

#[derive(Serialize, Deserialize, Debug)]
struct BearerAuthHeader {
    token: String,
}

impl FromRequest for BearerAuthHeader {
    type Error = ActixWebError;

    type Future = Ready<Result<Self, Self::Error>>;

    fn from_request(req: &HttpRequest, _payload: &mut Payload) -> Self::Future {
        let header_value = req.headers().get("Authorization").unwrap().to_str();
        let token_value = header_value
            .as_ref()
            .unwrap()
            .split_whitespace()
            .nth(1)
            .unwrap();

        match header_value {
            Ok(_) => ready(Ok(BearerAuthHeader {
                token: token_value.to_string(),
            })),
            Err(err) => ready(Err(actix_web::error::ErrorUnauthorized(err))),
        }
    }
}

#[derive(Debug, Serialize, Deserialize)]
struct Claims {
    sub: String,
    exp: usize,
    iat: usize,
    admin: Option<bool>,
}

#[derive(Serialize)]
struct TokenResponse {
    access_token: String,
}

impl Responder for TokenResponse {
    type Body = BoxBody;

    fn respond_to(self, _req: &HttpRequest) -> HttpResponse<Self::Body> {
        let body = serde_json::to_string(&self).unwrap();

        HttpResponse::Ok()
            .content_type(ContentType::json())
            .body(body)
    }
}

type LoginResult = Either<HttpResponse, TokenResponse>;

#[post("/login")]
async fn login(auth_header: BasicAuthHeader, app_state: web::Data<AppState>) -> LoginResult {
    let user = sqlx::query_as!(
        User,
        "SELECT * FROM users WHERE email = ?",
        auth_header.username
    )
    .fetch_optional(&app_state.sql_pool)
    .await
    .unwrap();

    match &user {
        Some(user) => {
            if user.password == auth_header.password {
                println!("[User Authenticated] User email: {}", user.email);
                let my_claims = Claims {
                    sub: user.email.clone(),
                    exp: Utc::now().timestamp() as usize + Duration::days(1).num_seconds() as usize,
                    iat: Utc::now().timestamp() as usize,
                    admin: Some(true),
                };
                println!("[User Authenticated] Claims: {:?}", my_claims);
                let token = encode(
                    &Header::default(),
                    &my_claims,
                    &EncodingKey::from_secret(app_state.jwt_secret.as_ref()),
                )
                .unwrap();

                let token_response = TokenResponse {
                    access_token: token,
                };
                return Either::Right(token_response);
            } else {
                println!("User not authenticated");
                return Either::Left(HttpResponse::Unauthorized().body({}));
            }
        }
        None => {
            println!("User not found");
            Either::Left(HttpResponse::BadRequest().body({}))
        }
    }
}

#[post("/validate")]
async fn validate_token(
    bearer: BearerAuthHeader,
    app_state: web::Data<AppState>,
) -> impl Responder {
    println!("[/validate] access_token: {}", bearer.token);

    let decoded_token = decode::<Claims>(
        &bearer.token,
        &DecodingKey::from_secret(app_state.jwt_secret.as_ref()),
        &Validation::default(),
    );

    match decoded_token {
        Ok(decoded) => {
            println!("[/validate] Token is valid");
            let claims = serde_json::to_string(&decoded.claims).unwrap();
            return HttpResponse::Ok()
                .content_type(ContentType::json())
                .body(claims)
        }
        Err(err) => {
            println!("[/validate] Token is invalid: {:?}", err);
            return HttpResponse::Unauthorized().finish();
        }
    }
}

pub fn config(conf: &mut web::ServiceConfig) {
    let scope = web::scope("/auth").service(login).service(validate_token);
    conf.service(scope);
}
