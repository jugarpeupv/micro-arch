# Auth service

## Populate `.env` file on micro-arch/auth-svc with the follow content

```bash
# auth-scv
DATABASE_URL=mysql://<user>:<user_password>@db:3306/auth
JWT_SECRET=<mysupersecret>

# mysql
MYSQL_ROOT_PASSWORD=<root_password>
MYSQL_DATABASE=auth
MYSQL_USER=<user>
MYSQL_PASSWORD=<user_password>
```

## Build rust docker image

```bash
docker build . -t auth-svc
```

## Run docker compose

```bash
docker compose up -d
```

## Make http calls to login/validate

Http call are already configured in `auth-svc/http/test.http`

