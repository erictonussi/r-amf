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
 * Thrown when the deserializer hits an object it can't deserialize.
 * 
 * @author Surrey
 * 
 */
public class DeserializeException extends RuntimeException {

	private static final long serialVersionUID = 3031066442495522862L;

	public DeserializeException() {
	}

	public DeserializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DeserializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeserializeException(String message) {
		super(message);
	}

	public DeserializeException(Throwable cause) {
		super(cause);
	}

}
