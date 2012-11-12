package com.reignite.messaging.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reignite.exception.MessagingException;
import com.reignite.logging.LogWriter;
import com.reignite.messaging.MessageBodyData;
import com.reignite.messaging.amf.AMFFilter;
import com.reignite.messaging.amf.BatchAMFFilter;
import com.reignite.messaging.amf.ErrorMessage;
import com.reignite.messaging.amf.RAMFServerAMFFilter;
import com.reignite.messaging.amf.SerializationAMFFilter;
import com.reignite.messaging.amfr.RemoteResponseMessage;
import com.reignite.messaging.amfr.RemotingMessage;

/**
 * And endpoint that opperates synchronously on AMF streams. It passes the
 * streams through a set of filter chains.
 * 
 * @author Surrey
 * 
 */
public class AMFEndpoint implements Endpoint {

	private Map<String, Destination> destinationMap = new HashMap<String, Destination>();
	private AMFFilter filterChain;
	private RAMFServer server;

	/**
	 * Constructs a default filter chain
	 */
	public AMFEndpoint(RAMFServer server) {
		this.server = server;
		List<AMFFilter> filters = new ArrayList<AMFFilter>();
		filters.add(new SerializationAMFFilter());
		filters.add(new BatchAMFFilter());
		filters.add(new RAMFServerAMFFilter());

		this.setFilterChain(filters);
	}

	@Override
	public void service(InputStream in, OutputStream out) {
		AMFServiceContext context = new AMFServiceContext(this, server, in, out);
		filterChain.invoke(context);
	}

	/**
	 * If you want to override the default filter chain
	 * 
	 * @param chain
	 *            the new filter chain.
	 */
	public void setFilterChain(List<AMFFilter> chain) {
		if (chain != null & chain.size() > 0) {
			filterChain = chain.remove(0);
			AMFFilter previous = filterChain;
			for (AMFFilter filter : chain) {
				previous.setNext(filter);
				previous = filter;
			}
		}
	}

	/**
	 * Used to insert a filter at the start of the filter chain.
	 * 
	 * @param first
	 *            the filter to insert.
	 */
	public void setFirstFilter(AMFFilter first) {
		if (filterChain != null) {
			first.setNext(filterChain);
		}
		filterChain = first;
	}

	/**
	 * Used to add an additional filter to the end of the chain. If the default
	 * filter chain is in place the last filter will be placed after the
	 * RAMFServerAMFFilter which calls the next filter prior to routing the
	 * message on to be serviced. This means any filters after the
	 * RAMFServerAMFFilter will not see the response.
	 * 
	 * @param last
	 *            the last filter to add.
	 */
	public void setLastFilter(AMFFilter last) {
		if (filterChain == null) {
			filterChain = last;
		} else {
			walkToEndOfChain(filterChain).setNext(last);
		}
	}

	/**
	 * This is used to add a filter between the last and current second to last.
	 * If the default chain is in place this means the filter is placed just
	 * prior to the message being routed to the service and after the
	 * BatchAMFFilter. This filter will see both the request and response.
	 * 
	 * @param secondLast
	 */
	public void setSecondLastFilter(AMFFilter secondLast) {
		if (filterChain == null) {
			filterChain = secondLast;
		} else {
			AMFFilter last = walkToEndOfChain(filterChain);
			if (last == filterChain) {
				secondLast.setNext(filterChain);
				filterChain = secondLast;
			} else {
				AMFFilter parent = walkToParentFilter(filterChain, last);
				secondLast.setNext(last);
				parent.setNext(secondLast);
			}
		}
	}

	private AMFFilter walkToParentFilter(AMFFilter previous, AMFFilter child) {
		if (child == null || (previous.getNext() != null && previous.getNext() == child)) {
			return previous;
		}
		return walkToParentFilter(child, child.getNext());
	}

	private AMFFilter walkToEndOfChain(AMFFilter filter) {
		if (filter.getNext() == null) {
			return filter;
		}
		return walkToEndOfChain(filter.getNext());
	}

	@Override
	public MessageBodyData routeMessageToService(MessageBodyData data) throws MessagingException {
		RemotingMessage message = (RemotingMessage) data;
		String destination = message.getDestination();
		String operation = message.getOperation();
		Object[] params = message.getParameters();
		Service service = locateService(destination, operation, params);
		Object response = null;
		if (service == null) {
			response = new ErrorMessage("Service was null.", destination, operation, params);
			LogWriter.error(getClass(), response.toString());
		} else {
			try {
				response = service.invoke();
			} catch (Exception e) {
				response = new ErrorMessage("Service invocation failed: " + e.getMessage(), destination, operation,
						params);
				LogWriter.error(getClass(), response.toString(), e);
			}
		}
		RemoteResponseMessage ack = message.createResponse();
		ack.setBody(response);

		return ack;
	}

	private Service locateService(String destination, String operation, Object[] params) throws MessagingException {
		Destination serviceDestination = destinationMap.get(destination);
		if (serviceDestination == null) {
			throw new MessagingException("Service destination: " + destination
					+ " is undefined.  Have you configured it?");
		}
		Service service = serviceDestination.getService(operation, params);
		return service;
	}

	/**
	 * @return the destinationMap
	 */
	@Override
	public Map<String, Destination> getDestinationMap() {
		return destinationMap;
	}

	/**
	 * @param destinationMap
	 *            the destinationMap to set
	 */
	@Override
	public void setDestinationMap(Map<String, Destination> destinationMap) {
		this.destinationMap = destinationMap;
	}
}
