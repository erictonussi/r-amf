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
 * Thrown when initialization failes.
 * @author Surrey
 *
 */
public class InitializeException extends Exception {

	private static final long serialVersionUID = 3841965701234550334L;

	public InitializeException() {
	}

	public InitializeException(String message) {
		super(message);
	}

	public InitializeException(Throwable cause) {
		super(cause);
	}

	public InitializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
