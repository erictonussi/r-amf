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
 * The base abstract implementation of the AMFFilter. All AMFFilters should
 * probably extend this one.
 * 
 * @author Surrey
 * 
 */
public abstract class BaseAMFFilter implements AMFFilter {

	private AMFFilter next;

	protected void nextFilter(AMFServiceContext context) {
		if (next != null) {
			next.invoke(context);
		}
	}

	@Override
	public void setNext(AMFFilter next) {
		this.next = next;
	}

	@Override
	public AMFFilter getNext() {
		return next;
	}
}
