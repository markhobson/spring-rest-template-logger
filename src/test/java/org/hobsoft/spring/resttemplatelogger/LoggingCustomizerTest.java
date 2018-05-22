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
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.StreamUtils.copyToByteArray;

/**
 * Tests {@code LoggingCustomizer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggingCustomizerTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	private LoggingCustomizer loggingCustomizer;
	
	private RestTemplate restTemplate;
	
	@Mock
	private ClientHttpRequestFactory requestFactory;
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Before
	public void setUp()
	{
		Log log = mock(Log.class);
		when(log.isDebugEnabled()).thenReturn(true);
		loggingCustomizer = new LoggingCustomizer(log);
		
		restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(requestFactory);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void canBufferResponses() throws IOException
	{
		MockClientHttpRequest request = new MockClientHttpRequest();
		MockClientHttpResponse response = new MockClientHttpResponse(singleUseStream("hello".getBytes()), OK);
		request.setResponse(response);
		when(requestFactory.createRequest(URI.create("http://example.com"), GET)).thenReturn(request);
		
		loggingCustomizer.customize(restTemplate);
		
		ClientHttpResponse actualResponse = restTemplate.getRequestFactory()
			.createRequest(URI.create("http://example.com"), GET)
			.execute();
		assertThat(copyToByteArray(actualResponse.getBody()), equalTo("hello".getBytes()));
	}
	
	@Test
	public void canAddLoggingInterceptor()
	{
		RestTemplate restTemplate = new RestTemplate();
		
		loggingCustomizer.customize(restTemplate);
		
		assertThat(restTemplate.getInterceptors(), contains(instanceOf(LoggingInterceptor.class)));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private static InputStream singleUseStream(byte[] bytes)
	{
		return new InputStream()
		{
			private int index;
			
			@Override
			public int read()
			{
				return index < bytes.length ? bytes[index++] : -1;
			}
		};
	}
}
