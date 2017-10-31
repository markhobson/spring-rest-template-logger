RestTemplate Logger
===================

Spring `RestTemplate` customizer to log HTTP traffic.

Getting started
---------------

Add a dependency to your Maven project:

```xml
<dependency>
	<groupId>org.hobsoft.spring</groupId>
	<artifactId>rest-template-logger</artifactId>
	<version>0.1.0</version>
</dependency>
```

Customize your `RestTemplate` as follows:

```java
RestTemplate restTemplate = new RestTemplateBuilder()
	.customizers(new LoggingCustomizer())
	.build()
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

License
-------

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/markhobson/rest-template-logger.svg?branch=master)](https://travis-ci.org/markhobson/rest-template-logger)
