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

### GET someone articles from database

![](https://raw.github.com/kewang/cache-control-boilerplate/master/demo/get-1.svg?sanitize=true)

### GET someone articles from cache via ETag

#### Request

`curl -v -X GET -H "If-None-Match: hDpZljKoQHSt+OGuAaZQjA==" -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

#### Result

```sh
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /api/articles HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.47.0
> Accept: */*
> If-None-Match: hDpZljKoQHSt+OGuAaZQjA==
> uid: kewang
> token: hello
> 
< HTTP/1.1 304 Not Modified
< ETag: "hDpZljKoQHSt+OGuAaZQjA=="
< Cache-Control: max-age=3
< 
* Connection #0 to host localhost left intact
```

### POST add someone articles

`curl -v -X POST -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`

### DELETE

`curl -v -X DELETE -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`
