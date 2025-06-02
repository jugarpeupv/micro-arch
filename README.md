# Framework de Desarrollo

## Propósito
Definir las herramientas y tecnologías utilizadas en el desarrollo de arquetipos frontend

## Contenido esperado:
- **Librerías y frameworks utilizados**.
- **Estándares de desarrollo y mejores prácticas**.
- **Guías para contribuir al código**.


# Configuración inicial proyecto

## Capacitor

### Configuración de secretos

- A día de hoy los secretos será necesario darlos de alta a nivel del repositorio. Esto puede que cambie más adelante, ya que se espera que se defina una nueva herramienta para la gestión de secretos validada por la DISMA
- Los secretos marcados con colores significa que tan solo sería necesario setear el grupo correspondiente de ellos, dependiendo de la cuenta de Apple/Google donde se vaya a publicar la aplicación

| Nombre                                                                                 | Tipo   | Scope     | Nivel        | Scope     | Necesario crear secreto?  | Value                              |
|----------------------------------------------------------------------------------------|--------|-----------|--------------|-----------|---------------------------|------------------------------------|
| AZURE_ARTIFACTS_PW                                                                     | Secret | common    | Organización | common    | NO                        | ${{ secrets.AZURE_ARTIFACTS_PW }}  |
| ACR_TOKEN_USER                                                                         | Secret | common    | Organización | common    | NO                        | ${{ secrets.ACR_TOKEN_USER }}      |
| ACR_TOKEN_PW                                                                           | Secret | common    | Organización | common    | NO                        | ${{ secrets.ACR_TOKEN_PW }}        |
| SONAR_TOKEN                                                                            | Secret | common    | Proyecto*    | common    | SI                        |                                    |
| SONAR_ROOT_CERT                                                                        | Secret | common    | Proyecto*    | common    | SI                        |                                    |
| RAM_GITHUB_GIT_TOKEN                                                                   | Secret | common    | Proyecto*    | common    | SI                        |                                    |
| FIREBASE_TOKEN                                                                         | Secret | common    | Proyecto*    | common    | SI (si se usa capacitor)  |                                    |
| MATCH_PASSWORD                                                                         | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| MATCH_GIT_BASIC_AUTHORIZATION                                                          | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| <span style="color:#F2CDCD">DIGITALHEALTH_APP_STORE_KEY_ID</span>                      | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| <span style="color:#F2CDCD">DIGITALHEALTH_APP_STORE_ISSUER_ID</span>                   | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| <span style="color:#F2CDCD">DIGITALHEALTH_APP_STORE_ADMIN_API_KEY_P8</span>            | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| <span style="color:#F2CDCD">MAPFRE_ESP_APP_STORE_KEY_ID</span>                         | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| <span style="color:#F2CDCD">MAPFRE_ESP_APP_STORE_ISSUER_ID</span>                      | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| <span style="color:#F2CDCD">MAPFRE_ESP_APP_STORE_ADMIN_API_KEY_P8</span>               | Secret | ios       | Proyecto*    | ios       | SI (si se usa capacitor)  |                                    |
| ANDROID_MATCH_GIT_BASIC_AUTHORIZATION                                                  | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#F38BA8">DIGITALHEALTH_GOOGLE_PLAY_SERVICE_ACCOUNT_JSON</span>      | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#F38BA8">MAPFRE_ESP_GOOGLE_PLAY_SERVICE_ACCOUNT_JSON</span>         | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">DIGITALHEALTH_ANDROID_KEYSTORE_PASSWORD</span>             | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">DIGITALHEALTH_ANDROID_KEYSTORE_KEY_ALIAS</span>            | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:blue">DIGITALHEALTH_ANDROID_KEYSTORE_KEY_PASSWORD</span>         | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">MAPFRE_ESP_ANDROID_KEYSTORE_PASSWORD</span>                | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">MAPFRE_ESP_ANDROID_KEYSTORE_KEY_ALIAS</span>               | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">MAPFRE_ESP_ANDROID_KEYSTORE_KEY_PASSWORD</span>            | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">MAPFRE_ESP_ENTERPRISE_ANDROID_KEYSTORE_PASSWORD</span>     | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
| <span style="color:#89ddff">MAPFRE_ESP_ENTERPRISE_ANDROID_KEYSTORE_KEY_ALIAS</span>    | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
 | <span style="color:blue">DIGITALHEALTH_ANDROID_KEYSTORE_KEY_ALIAS</span>            | Secret | android   | Proyecto*    | android   | SI (si se usa capacitor)  |                                    |
