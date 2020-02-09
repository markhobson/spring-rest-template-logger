package org.hobsoft.spring.resttemplatelogger.demo;

import org.hobsoft.spring.resttemplatelogger.LoggingCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class Application
{
	@GetMapping("/")
	public String go()
	{
		RestTemplate restTemplate = new RestTemplateBuilder()
			.customizers(new LoggingCustomizer())
			.build();
		
		ResponseEntity<String> response = restTemplate.getForEntity("http://example.com/", String.class);
		
		return response.getStatusCode().toString();
	}
	
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
