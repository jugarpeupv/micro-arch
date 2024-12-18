use std::env;
use dotenv::dotenv;
use std::time::Duration;

use actix_web::{web, App, HttpServer};
use sqlx::mysql::{MySqlPool, MySqlPoolOptions};
pub mod handlers;

#[derive(Clone)]
struct AppState {
    jwt_secret: String,
    sql_pool: MySqlPool,
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    dotenv().ok();
    let db_url = env::var("DATABASE_URL").unwrap();
    let jwt_secret = env::var("JWT_SECRET").unwrap();
    let port = env::var("APP_PORT").unwrap().parse::<u16>().unwrap();
    println!("JWT Secret: {}", jwt_secret);
    println!("db_url: {}", db_url);
    println!("port: {}", port);
    let sql_pool = MySqlPoolOptions::new()
        .max_connections(10)
        .idle_timeout(Duration::from_secs(3000))
        .connect(&db_url)
        .await
        .unwrap();
    println!("Connected to MySQL!");
    println!("sql_pool: {:?}", sql_pool);

    let app_state = AppState {
        jwt_secret,
        sql_pool: sql_pool.clone(),
    };

    HttpServer::new(move || {
        App::new()
            .app_data(web::Data::new(app_state.clone()))
            .configure(handlers::config)
    })
    .bind(("0.0.0.0", port))?
    .system_exit()
    .run()
    .await
}
