package com.reignite.messaging.proxy;

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
 * A standard TypeEditor that converts Doubles to other number types and
 * converts NaN to null.
 * 
 * @author Surrey
 * 
 */
public class NumberAwareEditor implements TypeEditor {

	@Override
	public Object convert(Object value, Class<?> requiredType) {
		if (value == null || value.equals(Double.NaN)) { // shortcut
			return null;
		}
		Object result = value;
		if (value instanceof Number) {
			if (requiredType.isPrimitive()) {
				if (int.class.isAssignableFrom(requiredType)) {
					result = ((Number) value).intValue();
				} else if (long.class.isAssignableFrom(requiredType)) {
					result = ((Number) value).longValue();
				} else if (double.class.isAssignableFrom(requiredType)) {
					result = ((Number) value).doubleValue();
				} else if (short.class.isAssignableFrom(requiredType)) {
					result = ((Number) value).shortValue();
				} else if (char.class.isAssignableFrom(requiredType)) {
					result = ((Character) value).charValue();
				}
			} else if (Number.class.isAssignableFrom(requiredType)) {
				if (Integer.class.isAssignableFrom(requiredType)) {
					result = new Integer(((Number) value).intValue());
				} else if (Long.class.isAssignableFrom(requiredType)) {
					result = new Long(((Number) value).longValue());
				} else if (Double.class.isAssignableFrom(requiredType)) {
					result = new Double(((Number) value).doubleValue());
				} else if (Short.class.isAssignableFrom(requiredType)) {
					result = new Short(((Number) value).shortValue());
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		NumberAwareEditor e = new NumberAwareEditor();
		System.out.println(e.convert(new Double(4), long.class));
	}
}
