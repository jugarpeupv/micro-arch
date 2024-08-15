use actix_web::{dev::Payload, get, web, FromRequest, HttpRequest, HttpResponse};
use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize)]
struct AuthBearerToken {
    token: String,
}

impl FromRequest for AuthBearerToken {
    fn from_request(req: &HttpRequest, payload: &mut Payload) -> Self::Future {
        let token = req
            .headers()
            .get("Authorization")
            .unwrap()
            .to_str()
            .unwrap()
            .to_string();
    }

    //fn from_request(req: &actix_web::HttpRequest, _payload: &mut actix_web::dev::Payload) -> Self::Future {
    //    let token = req.headers().get("Authorization").unwrap().to_str().unwrap().to_string();
    //    futures::future::ready(Ok(AuthBearerToken { token }))
    //}
}

#[get("/login")]
async fn login(token: AuthBearerToken) -> HttpResponse {
    println!("{:?}", token);
    HttpResponse::Ok().body("hello world probando")
}

pub fn config(conf: &mut web::ServiceConfig) {
    let scope = web::scope("/auth").service(login);

    conf.service(scope);
}
