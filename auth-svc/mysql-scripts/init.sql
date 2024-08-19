-- CREATE USER 'auth_user'@'localhost' IDENTIFIED BY 'Auth123';

-- CREATE DATABASE auth;

-- GRANT ALL PRIVILEGES ON auth.* TO 'auth_user'@'localhost';

CREATE TABLE auth.users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

INSERT INTO auth.users (email, password) VALUES ('test@gmail.com', 'test123');
