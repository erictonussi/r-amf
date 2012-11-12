package com.reignite.messaging.amf;

import java.util.ArrayList;
import java.util.List;

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
 * An AMF message
 * 
 * @author Surrey
 * 
 */
public class AMFMessage {

	private int version;
	private List<AMFMessageHeader> headers;
	private List<AMFMessageBody> bodies;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<AMFMessageHeader> getHeaders() {
		if (headers == null) {
			headers = new ArrayList<AMFMessageHeader>();
		}
		return headers;
	}

	public void setHeaders(List<AMFMessageHeader> headers) {
		this.headers = headers;
	}

	public List<AMFMessageBody> getBodies() {
		if (bodies == null){
			bodies = new ArrayList<AMFMessageBody>();
		}
		return bodies;
	}

	public void setBodies(List<AMFMessageBody> bodies) {
		this.bodies = bodies;
	}

}
