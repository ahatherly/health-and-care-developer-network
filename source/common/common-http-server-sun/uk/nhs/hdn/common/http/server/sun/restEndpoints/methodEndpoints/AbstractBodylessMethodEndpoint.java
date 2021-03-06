/*
 * © Crown Copyright 2013
 *
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

package uk.nhs.hdn.common.http.server.sun.restEndpoints.methodEndpoints;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.nhs.hdn.common.http.server.sun.restEndpoints.clientError4xxs.BadRequestException;
import uk.nhs.hdn.common.http.server.sun.restEndpoints.resourceStateSnapshots.ResourceStateSnapshot;
import uk.nhs.hdn.common.http.ResponseCode;
import uk.nhs.hdn.common.reflection.toString.AbstractToString;

import java.io.IOException;

import static uk.nhs.hdn.common.GregorianCalendarHelper.toRfc2822Form;
import static uk.nhs.hdn.common.GregorianCalendarHelper.utcNow;
import static uk.nhs.hdn.common.http.server.sun.helpers.ResponseHeadersHelper.emptyInvariantResponseHeaders;

public abstract class AbstractBodylessMethodEndpoint<R extends ResourceStateSnapshot> extends AbstractToString implements MethodEndpoint<R>
{
	protected AbstractBodylessMethodEndpoint()
	{
	}

	@Override
	public final void handle(@NotNull final String rawRelativeUriPath, @Nullable final String rawQueryString, @NotNull final HttpExchange httpExchange, @NotNull final R resourceStateSnapshot) throws IOException, BadRequestException
	{
		validateRequestHeaders(httpExchange.getRequestHeaders());
		final Headers headers = emptyInvariantResponseHeaders(httpExchange, toRfc2822Form(utcNow()));
		@ResponseCode final int responseCode = addResponseHeaders(headers);
		httpExchange.sendResponseHeaders(responseCode, NoContentBodyMagicValue);
		httpExchange.close();
	}

	protected abstract void validateRequestHeaders(@NotNull final Headers requestHeaders) throws BadRequestException;

	@ResponseCode
	protected abstract int addResponseHeaders(@NotNull final Headers responseHeaders);
}
