-- TODO: Use environment variables and a script to substitute them in this file

CREATE USER IF NOT EXISTS 'auth_user'@'localhost' IDENTIFIED BY 'auth_pass';

CREATE DATABASE IF NOT EXISTS auth;

GRANT ALL PRIVILEGES ON auth.* TO 'auth_user';

CREATE TABLE IF NOT EXISTS auth.users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

INSERT IGNORE INTO auth.users (email, password) VALUES ('testmail200087@gmail.com', 'test123');
