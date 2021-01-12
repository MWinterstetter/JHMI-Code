package org.fraunhofer.jhmi.util;

import java.util.Map.Entry;

/**
 * Key value pair that is used for storing message information.
 * @author WinterstetterM
 *
 */
public class MessageEntry implements Entry<String, String> {

	private final String key;
	private String value;
	
	/**
	 * Creates a new MessageEntry.
	 * 
	 * @param key
	 * The first value which is the target topic of the saved message.
	 * @param value
	 * The second value which is the content of the saved message.
	 */
	public MessageEntry(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		return oldValue;
	}

}
