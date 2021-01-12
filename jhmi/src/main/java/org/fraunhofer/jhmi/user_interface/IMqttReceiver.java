package org.fraunhofer.jhmi.user_interface;

public interface IMqttReceiver {
	
	/**
	 * This function is called by the callback with the relevant information for the message.
	 * @param topic 
	 * The topic under which the message was received.
	 * @param messageString 
	 * The received message in byte array format.
	 * @param messageId
	 * The id of the received message.
	 */
	public void messageReceived( String topic, String messageString, int messageId);

}
