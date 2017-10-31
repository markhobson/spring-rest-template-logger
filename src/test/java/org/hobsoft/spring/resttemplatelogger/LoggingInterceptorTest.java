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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

/**
 * Tests {@code LoggingInterceptor}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggingInterceptorTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	@Mock
	private Log log;
	
	private LoggingInterceptor loggingInterceptor;
	
	@Mock
	private ClientHttpRequestExecution execution;
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Before
	public void setUp()
	{
		when(log.isDebugEnabled()).thenReturn(true);
		
		loggingInterceptor = new LoggingInterceptor(log);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void canLogRequestUsingDefaultCharset() throws IOException, URISyntaxException
	{
		MockClientHttpRequest request = new MockClientHttpRequest(POST, new URI("/hello"));
		byte[] body = "world £".getBytes(ISO_8859_1);
		when(execution.execute(request, body)).thenReturn(new MockClientHttpResponse(new byte[0], OK));
		
		loggingInterceptor.intercept(request, body, execution);
		
		verify(log).debug("Request: POST /hello world £");
	}
	
	@Test
	public void canLogRequestUsingCharset() throws IOException, URISyntaxException
	{
		MockClientHttpRequest request = new MockClientHttpRequest(POST, new URI("/hello"));
		request.getHeaders().setContentType(parseMediaType("text/plain;charset=UTF-8"));
		byte[] body = "world £".getBytes(UTF_8);
		when(execution.execute(request, body)).thenReturn(new MockClientHttpResponse(new byte[0], OK));
		
		loggingInterceptor.intercept(request, body, execution);
		
		verify(log).debug("Request: POST /hello world £");
	}
	
	@Test
	public void canLogResponseUsingDefaultCharset() throws IOException, URISyntaxException
	{
		MockClientHttpRequest request = new MockClientHttpRequest();
		byte[] body = "hello £".getBytes(ISO_8859_1);
		when(execution.execute(request, new byte[0])).thenReturn(new MockClientHttpResponse(body, OK));
		
		loggingInterceptor.intercept(request, new byte[0], execution);
		
		verify(log).debug("Response: 200 hello £");
	}
	
	@Test
	public void canLogResponseUsingCharset() throws IOException, URISyntaxException
	{
		MockClientHttpRequest request = new MockClientHttpRequest();
		byte[] body = "hello £".getBytes(UTF_8);
		MockClientHttpResponse response = new MockClientHttpResponse(body, OK);
		response.getHeaders().setContentType(parseMediaType("text/plain;charset=UTF-8"));
		when(execution.execute(request, new byte[0])).thenReturn(response);
		
		loggingInterceptor.intercept(request, new byte[0], execution);
		
		verify(log).debug("Response: 200 hello £");
	}
}
