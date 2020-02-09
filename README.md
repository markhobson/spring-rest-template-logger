# Spring RestTemplate Logger

Spring `RestTemplate` customizer to log HTTP traffic.

## Getting started

Add a dependency to your Maven project:

```xml
<dependency>
	<groupId>org.hobsoft.spring</groupId>
	<artifactId>spring-rest-template-logger</artifactId>
	<version>2.0.0</version>
</dependency>
```

Customize your `RestTemplate` as follows:

```java
RestTemplate restTemplate = new RestTemplateBuilder()
	.customizers(new LoggingCustomizer())
	.build();
```

Ensure that debug logging is enabled in `application.properties`:

```properties
logging.level.org.hobsoft.spring.resttemplatelogger.LoggingCustomizer = DEBUG
```

Now all `RestTemplate` HTTP traffic will be logged to `org.hobsoft.spring.resttemplatelogger.LoggingCustomizer` at debug
level:

```
2020-02-09 10:59:48.954 DEBUG 17602 --- [nio-8080-exec-1] o.h.s.r.LoggingCustomizer                : Request: GET http://example.com/ 
2020-02-09 10:59:49.291 DEBUG 17602 --- [nio-8080-exec-1] o.h.s.r.LoggingCustomizer                : Response: 200 <!doctype html>
<html>
<head>
    <title>Example Domain</title>
...
```

See the [demo](demo) Spring Boot application to see this in action.

## Configuration

### Using a different logger

To log HTTP traffic to an alternative logger, simply pass it to the customizer: 

```java
RestTemplate restTemplate = new RestTemplateBuilder()
	.customizers(new LoggingCustomizer(LogFactory.getLog("my.http.log")))
	.build();
```

### Customising the log format

To use a different format when logging HTTP traffic, implement `LogFormatter` and pass it to the customizer: 

```java
RestTemplate restTemplate = new RestTemplateBuilder()
	.customizers(new LoggingCustomizer(LogFactory.getLog(LoggingCustomizer.class), new MyLogFormatter()))
	.build();
```

Consider subclassing `DefaultLogFormatter` and overriding `formatBody` if you only need to customise the logged HTTP
body.

## Acknowledgements

Thanks to [@nwholloway](https://github.com/nwholloway) and [@hdpe](https://github.com/hdpe) for the original
implementation.

## License

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/markhobson/spring-rest-template-logger.svg?branch=master)](https://travis-ci.org/markhobson/spring-rest-template-logger)
