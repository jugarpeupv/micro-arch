FROM rust:1.80.0-slim-bullseye AS build

# View app name in Cargo.toml
ARG APP_NAME=auth-svc
ENV SQLX_OFFLINE=true
ENV APP_PORT=5000

WORKDIR /build

COPY Cargo.lock Cargo.toml ./
RUN mkdir src \
    && echo "// dummy file" > src/lib.rs \
    && cargo build --release

COPY src src
COPY .sqlx .sqlx
RUN cargo build --locked --release
RUN cp ./target/release/$APP_NAME /bin/server

FROM debian:bullseye-slim AS final
COPY --from=build /bin/server /bin/
RUN apt-get update && apt-get install -y curl make cmake gettext git

EXPOSE $APP_PORT

CMD ["/bin/server"]
