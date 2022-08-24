# TODO
A simple spring boot backend java application(with H2 in mem DB) to support basic todo functionalities and a scheduler to take care of past due todo items.
## Functionalities of TODO
The application comes with rest endpoints for the support of
1. Creation of a todo with NOT_DONE and DONE status.
```shell
curl -X POST \
  http://localhost:8080/v1/todo/ \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a451b863-2546-399a-dfa1-8e30ea2b63c4' \
  -d '{
	"description": "Working model1",
	"status": "NOT_DONE",
	"dueDate": "2021-11-12 13:23:44"
}'
```
2. Updating description/status(cannot be switched to PAST_DUE) of a todo that is not PAST_DUE.
```shell
curl -X PATCH \
  http://localhost:8080/v1/todo/1a7519c9-02a2-4437-9fe9-c0f783a6a754 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: f0d0496f-78e2-3483-4d2b-64bbd614ef1b' \
  -d '{
	"description": "Working model4",
	"status": "NOT_DONE"
}'
```
3. Get all todo that are still in NOT_DONE status.
```shell
curl -X GET \
  http://localhost:8080/v1/todo/ \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 4dd650dd-9118-147b-f191-51b8e724ef8d'
```
4. Get a todo item using the ID
```shell
curl -X GET \
  http://localhost:8080/v1/todo/ed5a6959-6d93-42ee-9578-d8be6fa05f19 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 444cbb06-78d6-750c-827c-9afa69d3fba1'
```

## How to run?
**Run the below commands from the root directory of the project**
1. Create build jar using gradle command
```shell
./gradlew clean build
```
2. Run the dockerfile available in root directory to create image with the application jar.
```shell
docker build -t todo:1.0 -f Dockerfile .
```
3. Run the image using the docker run command.
```shell
docker run -p 8080:8080 todo:1.0
```
Voila! The application is setup locally. Any changes to the config are to be made in application properties file.