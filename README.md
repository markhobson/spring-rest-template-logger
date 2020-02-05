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
21:14:53.688 [main] DEBUG org.hobsoft.spring.resttemplatelogger.LoggingCustomizer - Request: GET http://www.hobsoft.org/ 
21:14:53.892 [main] DEBUG org.hobsoft.spring.resttemplatelogger.LoggingCustomizer - Response: 200 <!DOCTYPE html>
<html>
	<head>
		<title>Hobsoft</title>
...
```

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
