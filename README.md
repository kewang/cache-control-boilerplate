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

#### GET data from database

`curl -v -X GET -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

```sh
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /api/articles HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.47.0
> Accept: */*
> uid: kewang
> token: hello
> 
< HTTP/1.1 200 OK
< ETag: "hDpZljKoQHSt+OGuAaZQjA=="
< Cache-Control: max-age=3
< Content-Type: application/json
< Content-Length: 888
< 
* Connection #0 to host localhost left intact
{"articles":[{"title":"DsD","description":"NOteyQW0m1"},{"title":"Wni","description":"lGll6GFMTZ"},{"title":"abW","description":"ihebl9TuoW"},{"title":"hRw","description":"pE4T5j47PQ"},{"title":"1Zr","description":"ATcEiqzlaz"},{"title":"iJi","description":"FNuPtXOwNJ"},{"title":"cpo","description":"hKv444PVLq"},{"title":"Zl6","description":"0GZRGHmGAl"},{"title":"GHP","description":"j9s075f6dp"},{"title":"ph6","description":"6SX9swEsbX"},{"title":"5Y5","description":"XjtvPuQb19"},{"title":"wnn","description":"LaQzE0sAuM"},{"title":"gZo","description":"DrBnb1cJmh"},{"title":"9d8","description":"EQnk4owbHT"},{"title":"nhn","description":"XqCqErYGmF"},{"title":"QF1","description":"Dv81zy8oPP"},{"title":"dKs","description":"vmVu0E48p6"},{"title":"pqw","description":"aED3ZVW9zO"},{"title":"Joy","description":"leqSiBkXzW"},{"title":"tZn","description":"M7DMWHLV8O"}],"result":true}
```

#### GET data from cache via ETag

`curl -v -X GET -H "If-None-Match: hDpZljKoQHSt+OGuAaZQjA==" -H "uid: kewang" -H "token: hello" http://localhost:8080/api/articles`

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

### POST

`curl -v -X POST -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`

### DELETE

`curl -v -X DELETE -H "uid: kewang" -H "token: hello" http://localhost:8081/api/articles`
