package com.reignite.messaging.amf;

import com.reignite.messaging.MessageBodyData;

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
 * An object describing an error that occured during RAMF handling of a message.
 * This is not an error in the service code.
 * 
 * @author Surrey
 * 
 */
public class ErrorMessage implements MessageBodyData {

	private static final long serialVersionUID = 6180346423554155977L;

	private String message;
	private String cause;

	/**
	 * @param cause
	 *            a string describing the error.
	 * @param destination
	 *            the message destination when the error occured.
	 * @param operation
	 *            the operation attempted.
	 * @param params
	 *            the params passed.
	 */
	public ErrorMessage(String cause, String destination, String operation, Object[] params) {
		this.cause = cause;
		String paramString = createParamString(params);
		message = "Failed during call to: " + destination + "." + operation + paramString;
	}

	private String createParamString(Object[] params) {
		if (params == null) {
			return "(null)";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (Object object : params) {
			if (object == null) {
				sb.append("null");
			} else {
				sb.append(object.getClass().getSimpleName());
			}
			sb.append(",");
		}
		if (sb.indexOf(",") > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * @param cause
	 *            the cause to set
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	@Override
	public String toString() {
		return cause + "\n" + message;
	}
}
