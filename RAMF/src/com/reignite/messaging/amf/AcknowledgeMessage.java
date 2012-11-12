package com.reignite.messaging.amf;

import com.reignite.messaging.Aliased;
import com.reignite.messaging.amfr.RemoteResponseMessage;

/**
 * This file is part of r-amf.
 * 
 * r-amf is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License.
 * 
 * r-amf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * r-amf. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Created by Surrey Hughes of Reignite Pty Ltd <http://www.reignite.com.au>
 * 
 * The response from an AMFEndpoint routed remote call.
 * 
 * @author Surrey
 * 
 */
public class AcknowledgeMessage extends RemoteResponseMessage implements Aliased {

	private static final long serialVersionUID = -3060421139350506857L;
	
	@Override
	public String getAlias() {
		return "flex.messaging.messages.AcknowledgeMessage";
	}

}
