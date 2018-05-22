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

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

/**
 * Tests {@code DefaultLogFormatter}.
 */
public class DefaultLogFormatterTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	private DefaultLogFormatter formatter;
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Before
	public void setUp()
	{
		formatter = new DefaultLogFormatter();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void canFormatRequestUsingDefaultCharset()
	{
		MockClientHttpRequest request = new MockClientHttpRequest(POST, URI.create("/hello"));
		byte[] body = "world £".getBytes(ISO_8859_1);
		
		String actual = formatter.formatRequest(request, body);
		
		assertThat(actual, is("Request: POST /hello world £"));
	}
	
	@Test
	public void canFormatRequestUsingCharset()
	{
		MockClientHttpRequest request = new MockClientHttpRequest(POST, URI.create("/hello"));
		request.getHeaders().setContentType(parseMediaType("text/plain;charset=UTF-8"));
		byte[] body = "world £".getBytes(UTF_8);
		
		String actual = formatter.formatRequest(request, body);
		
		assertThat(actual, is("Request: POST /hello world £"));
	}
	
	@Test
	public void canFormatResponseUsingDefaultCharset() throws IOException
	{
		byte[] body = "hello £".getBytes(ISO_8859_1);
		MockClientHttpResponse response = new MockClientHttpResponse(body, OK);
		
		String actual = formatter.formatResponse(response);
		
		assertThat(actual, is("Response: 200 hello £"));
	}
	
	@Test
	public void canFormatResponseUsingCharset() throws IOException
	{
		byte[] body = "hello £".getBytes(UTF_8);
		MockClientHttpResponse response = new MockClientHttpResponse(body, OK);
		response.getHeaders().setContentType(parseMediaType("text/plain;charset=UTF-8"));
		
		String actual = formatter.formatResponse(response);
		
		assertThat(actual, is("Response: 200 hello £"));
	}
}
