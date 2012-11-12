/**
 * 
 */
package com.reignite.exception;


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
public class MessageIncompatibleException extends MessagingException {

	private static final long serialVersionUID = -3266187373239251023L;

	public MessageIncompatibleException() {
		super();
	}

	public MessageIncompatibleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MessageIncompatibleException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageIncompatibleException(String message) {
		super(message);
	}

	public MessageIncompatibleException(Throwable cause) {
		super(cause);
	}

}
