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

package uk.nhs.hcdn.dts.domain.status;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import uk.nhs.hcdn.common.naming.ActualName;

public enum StatusType implements ActualName
{
	CollectReport("Collect Report"),
	CollectError("Collect Error"),
	ServerAuthenticationError("Server Authentication Error"),
	TransferReport("Transfer Report"),
	ClientAuthenticationError("Client Authentication Error"),
	DelayReport("Delay Report"),
	TransferFail("Transfer Fail"),
	ServerFail("Server Fail"),
	NonDelivery("Non Delivery"),
	PollReport("Poll Report"),
	PollFail("Poll Fail"),
	;

	@NonNls
	@NotNull
	private final String actualName;

	StatusType(@NonNls @NotNull final String actualName)
	{
		this.actualName = actualName;
	}

	@NotNull
	@Override
	public String actualName()
	{
		return actualName;
	}

	@NotNull
	@SuppressWarnings("RefusedBequest")
	@Override
	public String toString()
	{
		return actualName;
	}
}
