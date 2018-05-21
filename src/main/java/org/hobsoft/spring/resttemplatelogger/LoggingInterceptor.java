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
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * {@code ClientHttpRequestInterceptor} that logs request and response bodies.
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	private final Log log;
	
	private final LogFormatter formatter;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------
	
	public LoggingInterceptor(Log log, LogFormatter formatter)
	{
		this.log = log;
		this.formatter = formatter;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// ClientHttpRequestInterceptor methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
		throws IOException
	{
		if (log.isDebugEnabled())
		{
			log.debug(formatter.formatRequest(request, body));
		}
		
		ClientHttpResponse response = execution.execute(request, body);
		
		if (log.isDebugEnabled())
		{
			log.debug(formatter.formatResponse(response));
		}
		
		return response;
	}
}
