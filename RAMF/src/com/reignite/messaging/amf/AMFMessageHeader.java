package com.reignite.messaging.amf;

public class AMFMessageHeader {

	private String name;
	private boolean mustUnderstand;
	private Object data;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mustUnderstand
	 */
	public boolean getMustUnderstand() {
		return mustUnderstand;
	}

	/**
	 * @param mustUnderstand
	 *            the mustUnderstand to set
	 */
	public void setMustUnderstand(boolean mustUnderstand) {
		this.mustUnderstand = mustUnderstand;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
