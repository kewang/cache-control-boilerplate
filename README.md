# cache-control-boilerplate

## Requirement

* Maven
* Docker

## ZooKeeper

### Run server

`./bin/zk-server.sh`

### Run client

`./bin/zk-client.sh`

## Web server

### Compile code

`./bin/web-compile.sh`

### Start up web node

`./bin/web-run.sh [PORT]`

## Test

### GET

`curl -v -X GET -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`

### POST

`curl -v -X POST -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`

### DELETE

`curl -v -X DELETE -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`
