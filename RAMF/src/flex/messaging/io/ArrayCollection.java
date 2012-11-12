/*************************************************************************
 * 
 * ADOBE CONFIDENTIAL
 * __________________
 * 
 *  [2002] - [2007] Adobe Systems Incorporated 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
package flex.messaging.io;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;

import com.reignite.codec.amf.AMF3SerializeWorker;
import com.reignite.logging.LogWriter;

/**
 * Used to map to client mx.collections.ArrayCollection to java.util.Lists in
 * Java.
 */
public class ArrayCollection extends ArrayList<Object> implements Externalizable {
	private static final long serialVersionUID = 8037277879661457358L;

	public ArrayCollection() {
		super();
	}

	public ArrayCollection(Collection<Object> c) {
		super(c);
	}

	public ArrayCollection(int initialCapacity) {
		super(initialCapacity);
	}

	public Object[] getSource() {
		return toArray();
	}

	public void setSource(Object[] s) {
		if (s != null) {
			clear();

			for (int i = 0; i < s.length; i++) {
				add(s[i]);
			}
		} else {
			clear();
		}
	}

	public void setSource(Collection<Object> s) {
		addAll(s);
	}

	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		Object s = input.readObject();
		if (s instanceof Collection) {
			s = ((Collection<Object>) s).toArray();
		}
		Object[] source = (Object[]) s;
		setSource(source);
	}

	public void writeExternal(ObjectOutput output) throws IOException {
		if (output instanceof AMF3SerializeWorker) {
			try {
				((AMF3SerializeWorker) output).writeAMF3Object(getSource());
			} catch (ClassNotFoundException e) {
				LogWriter.error(getClass(), "Could not write external ArrayCollection: " + e, e);
				throw new IOException("Class not found", e);
			}
		} else {
			output.writeObject(getSource());
		}	}
}
