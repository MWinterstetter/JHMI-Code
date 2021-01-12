package org.fraunhofer.jhmi.util;

/**
 * A triple of String to save message information.
 * @author WinterstetterM
 *
 */
public class StringTripple {
	
	private String value1;
	private String value2;
	private String value3;
	
	public StringTripple(String value1, String value2, String value3) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}

	public String getValue1() {
		return value1;
	}

	public String getValue2() {
		return value2;
	}

	public String getValue3() {
		return value3;
	}

	
	
}
