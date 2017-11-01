/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hobsoft.spring.resttemplatelogger;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static java.net.HttpURLConnection.HTTP_OK;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Integration test for {@code LoggingCustomizer} against a real endpoint.
 */
@SpringBootTest
public class LoggingCustomizerIT
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort());
	
	private Log log;
	
	private RestTemplate restTemplate;
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Before
	public void setUp()
	{
		log = mock(Log.class);
		when(log.isDebugEnabled()).thenReturn(true);
		
		restTemplate = new RestTemplateBuilder()
			.customizers(new LoggingCustomizer(log))
			.rootUri(wireMockRule.url("/"))
			.build();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void canLogRequest()
	{
		wireMockRule.givenThat(post("/hello").withRequestBody(equalTo("world"))
			.willReturn(aResponse().withStatus(HTTP_OK)));
		
		restTemplate.postForEntity("/hello", "world", String.class);
		
		verify(log).debug(String.format("Request: POST http://localhost:%d/hello world", wireMockRule.port()));
	}
	
	@Test
	public void canGetResponse()
	{
		wireMockRule.givenThat(get("/hello")
			.willReturn(aResponse().withStatus(HTTP_OK).withBody("world")));
		
		ResponseEntity<String> response = restTemplate.getForEntity("/hello", String.class);
		
		assertThat("status", response.getStatusCode(), is(OK));
		assertThat("body", response.getBody(), is("world"));
	}
	
	@Test
	public void canLogResponse()
	{
		wireMockRule.givenThat(get("/hello")
			.willReturn(aResponse().withStatus(HTTP_OK).withBody("world")));
		
		restTemplate.getForEntity("/hello", String.class);
		
		verify(log).debug("Response: 200 world");
	}
}
