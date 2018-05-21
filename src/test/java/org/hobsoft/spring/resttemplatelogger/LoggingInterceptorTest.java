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

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
	
	@Mock
	private LogFormatter formatter;
	
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
		
		loggingInterceptor = new LoggingInterceptor(log, formatter);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void canLogRequest() throws IOException
	{
		MockClientHttpRequest request = new MockClientHttpRequest();
		byte[] body = new byte[] {(byte) 1};
		when(formatter.formatRequest(request, body)).thenReturn("message");
		
		loggingInterceptor.intercept(request, body, execution);
		
		verify(log).debug("message");
	}
	
	@Test
	public void canLogResponse() throws IOException
	{
		MockClientHttpRequest request = new MockClientHttpRequest();
		byte[] body = new byte[] {(byte) 1};
		
		ClientHttpResponse response = withSuccess().createResponse(null);
		when(execution.execute(request, body)).thenReturn(response);
		when(formatter.formatResponse(response)).thenReturn("message");
		
		loggingInterceptor.intercept(request, body, execution);
		
		verify(log).debug("message");
	}
}
