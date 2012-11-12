package com.reignite.messaging.amf;

import com.reignite.messaging.server.AMFServiceContext;

/**
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
