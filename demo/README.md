# Spring RestTemplate Logger Demo

Spring Boot application demonstrating how to use Spring RestTemplate Logger.

Run the application:

```
mvn spring-boot:run
```

Visit http://localhost:8080/ and observe the logged HTTP traffic:

```
2020-02-09 10:59:48.954 DEBUG 17602 --- [nio-8080-exec-1] o.h.s.r.LoggingCustomizer                : Request: GET http://example.com/ 
2020-02-09 10:59:49.291 DEBUG 17602 --- [nio-8080-exec-1] o.h.s.r.LoggingCustomizer                : Response: 200 <!doctype html>
<html>
<head>
    <title>Example Domain</title>
...
```
