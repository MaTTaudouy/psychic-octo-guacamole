# r2dbrest-json-server
R2DBC Postrgres is a rest API for CRUD JSON Server purpose build on top of a real reactive database.
Is for development usage only, not for production, no security, and a lor of sql injection should be possible.

## Install
1. Clone repository
2. Install a simple postgresql database (currently works with the official docker image postgres:13-alpine
3. Execute the init.sql script in src/main/resources/db/migration/
4. Configure db access into application.yml
5. Run the app (just launch the main works fine :))
6. Run the test into r2dbrest-api-integration-test project and enjoy !

## Principles
You could create any kind of object from your client app.
If you post a json object to "localhost:8080/myresource" server will create a table named "myresource" and insert your json into a jsonb column in postgres.
The table will have two column id of type UUID not null and data of type json not null 
* id is a UUID is generate by the json-server and merge into the json object with property named "id"
* data is the json object passed throw the http request with the uuid merged
You could retrive your object with the location attribute in the Response (like localhost:8080/myresource/{uuid}
Actually only GET("/*/{id}") for an object, GET("/*") for all the objects, POST("/*") to insert and PUT("/*/{id}) to update is avalaible

### TODO tech
Integration test for PUT and DELETE
Add a flyway to update Database

### TODO fonc
delete
put
filter/search
secure extract url with regex
secure sql tablename
nested entity ?
rework converter
