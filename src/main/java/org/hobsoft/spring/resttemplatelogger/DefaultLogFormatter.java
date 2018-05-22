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

import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import static org.springframework.util.StreamUtils.copyToByteArray;

/**
 * Default {@code LogFormatter} implementation.
 */
public class DefaultLogFormatter implements LogFormatter
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------
	
	private static final Charset DEFAULT_CHARSET = ISO_8859_1;
	
	// ----------------------------------------------------------------------------------------------------------------
	// LogFormatter methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Override
	public String formatRequest(HttpRequest request, byte[] body)
	{
		String formattedBody = formatBody(body, getCharset(request));
		
		return String.format("Request: %s %s %s", request.getMethod(), request.getURI(), formattedBody);
	}
	
	@Override
	public String formatResponse(ClientHttpResponse response) throws IOException
	{
		String formattedBody = formatBody(copyToByteArray(response.getBody()), getCharset(response));
		
		return String.format("Response: %s %s", response.getStatusCode().value(), formattedBody);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------
	
	protected String formatBody(byte[] body, Charset charset)
	{
		return new String(body, charset);
	}
	
	protected Charset getCharset(HttpMessage message)
	{
		return Optional.ofNullable(message.getHeaders().getContentType())
			.map(MediaType::getCharset)
			.orElse(DEFAULT_CHARSET);
	}
}
