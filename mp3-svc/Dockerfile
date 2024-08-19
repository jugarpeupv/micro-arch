FROM rust:1.70.0-slim-bullseye AS build

ARG APP_NAME=auth-svc

WORKDIR /build

# Build a dummy project to cache our dependencies
COPY Cargo.lock Cargo.toml ./
RUN mkdir src \
    && echo "// dummy file" > src/lib.rs \
    && cargo build --release

COPY src src
RUN cargo build --locked --release
RUN cp ./target/release/$APP_NAME /bin/server

FROM debian:bullseye-slim AS final
COPY --from=build /bin/server /bin/
CMD ["/bin/server"]
