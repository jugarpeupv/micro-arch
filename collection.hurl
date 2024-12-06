################################################################ auth-svc
POST http://localhost:{{APP_PORT}}/auth/login
Content-Type: application/json
[BasicAuth]
{{MYSQL_TEST_USERNAME}}: {{MYSQL_TEST_PASSWORD}}
HTTP 200
[Captures]
auth_token: jsonpath "$['access_token']"


POST http://localhost:{{APP_PORT}}/auth/validate
Authorization: Bearer {{auth_token}}
Content-Type: application/json

################################################################ gateway-svc

POST http://localhost:8080/login
Content-Type: application/json
[BasicAuth]
{{MYSQL_TEST_USERNAME}}: {{MYSQL_TEST_PASSWORD}}
HTTP 200
[Captures]
auth_token_gw: jsonpath "$['access_token']"


POST http://localhost:8080/upload
Authorization: Bearer {{auth_token_gw}}
[MultipartFormData]
file: file,test-neovim.mp4; video/mp4


################################################################ mp3 svc
POST http://localhost:8000/api/convert
[MultipartFormData]
file: file,test-neovim.mp4; video/mp4
