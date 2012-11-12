package com.reignite.messaging.amf;

import com.reignite.messaging.server.AMFServiceContext;

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
 * A filter that forms a part of a chain through the AMFEndpoint lifecycle.
 * 
 * @author Surrey
 * 
 */
public interface AMFFilter {

	/**
	 * Does work on the given visitor (AMFServiceContext) This method should
	 * invoke the next filter in the chain
	 * 
	 * @param context
	 *            the visitor with the input and output stream.
	 */
	void invoke(AMFServiceContext context);

	/**
	 * @param next
	 *            the next filter in the chain
	 */
	void setNext(AMFFilter next);

	/**
	 * @return the next filter in the chain or null.
	 */
	AMFFilter getNext();

}
