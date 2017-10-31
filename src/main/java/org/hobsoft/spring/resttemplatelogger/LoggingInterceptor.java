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
import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import static org.springframework.util.StreamUtils.copyToString;

/**
 * {@code ClientHttpRequestInterceptor} that logs request and response bodies.
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------
	
	private static final Charset DEFAULT_CHARSET = ISO_8859_1;
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------
	
	private final Log log;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------
	
	public LoggingInterceptor(Log log)
	{
		this.log = log;
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
			log.debug(String.format("Request: %s %s %s", request.getMethod(), request.getURI(),
				new String(body, getCharset(request))));
		}
		
		ClientHttpResponse response = execution.execute(request, body);
		
		if (log.isDebugEnabled())
		{
			log.debug(String.format("Response: %s %s", response.getStatusCode().value(),
				copyToString(response.getBody(), getCharset(response))));
		}
		
		return response;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private static Charset getCharset(HttpMessage message)
	{
		return Optional.ofNullable(message.getHeaders().getContentType())
			.map(MediaType::getCharset)
			.orElse(DEFAULT_CHARSET);
	}
}
