# Auth service

## Develop against local mysql

### Run init sql script to initialize database

#### Initialize mysql service (macos)
```bash
brew services start mysql
```

#### Execute mysql init script
```bash
mysql -h 127.0.0.1 -u root < ./auth-svc/mysql-scripts/init.sql
```

### Populate `.env` file on micro-arch/auth-svc with the follow content

```bash
# auth-scv
# mysql
MYSQL_ROOT_PASSWORD=$your_custom_root_password
MYSQL_DATABASE=auth
MYSQL_USER=auth_user
MYSQL_PASSWORD=auth_pass

# app
DB_PORT=3306
DB_HOST=127.0.0.1
DATABASE_URL=mysql://${MYSQL_USER}:${MYSQL_PASSWORD}@${DB_HOST}:${DB_PORT}/auth
JWT_SECRET=mysecret
```

Make sure ${DB_HOST} is pointing to 127.0.0.1


## Containerized service

### Populate `.env` file on micro-arch/auth-svc with the follow content

```bash
# .env

# mysql
MYSQL_ROOT_PASSWORD=$your_custom_root_password
MYSQL_DATABASE=auth
MYSQL_USER=auth_user
MYSQL_PASSWORD=auth_pass

# app
DB_PORT=3306
DB_HOST=db
DATABASE_URL=mysql://${MYSQL_USER}:${MYSQL_PASSWORD}@${DB_HOST}:${DB_PORT}/auth
JWT_SECRET=$your_custom_jwt_secret>
```

Make sure ${DB_HOST} is pointing to db (name of the mysql service in docker)

### Build rust docker image

```bash
docker build . -t auth-svc
```

### Run docker compose

```bash
docker compose up -d
```

### Make http calls to login/validate

Http call are already configured in `auth-svc/http/test.http`

