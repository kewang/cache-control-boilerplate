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

### GET someone's articles from database

`curl -v -X GET -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

![](./demo/get-1.png)

### GET someone's articles from cache via ETag

`curl -v -X GET -H "If-None-Match: hDpZljKoQHSt+OGuAaZQjA==" -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

![](./demo/get-2.png)

### POST add someone's articles, so invalidate someone's cache

`curl -v -X POST -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

![](./demo/post-1.png)

### DELETE remove everyone's articles, so invalidate everyone's caches

`curl -v -X DELETE -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

![](./demo/delete-1.png)
