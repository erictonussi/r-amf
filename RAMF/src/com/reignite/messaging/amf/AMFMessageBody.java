package com.reignite.messaging.amf;

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
 * @author Surrey
 *
 */
public class AMFMessageBody {

	public static final String RESULT_METHOD = "/onResult";
	public static final String STATUS_METHOD = "/onStatus";

	private String targetURI;
	private String responseURI;
	private Object data; //array of RemotingMessage

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the targetURI
	 */
	public String getTargetURI() {
		return targetURI;
	}

	/**
	 * @param targetURI
	 *            the targetURI to set
	 */
	public void setTargetURI(String targetURI) {
		this.targetURI = targetURI;
	}

	/**
	 * @return the reponseURI
	 */
	public String getResponseURI() {
		return responseURI;
	}

	/**
	 * @param reponseURI
	 *            the reponseURI to set
	 */
	public void setResponseURI(String responseURI) {
		this.responseURI = responseURI;
	}
}
