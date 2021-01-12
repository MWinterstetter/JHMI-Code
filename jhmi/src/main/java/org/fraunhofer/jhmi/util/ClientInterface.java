package org.fraunhofer.jhmi.util;

import org.fraunhofer.jhmi.user_interface.IMqttReceiver;

public interface ClientInterface {
		
	/**
	 * Success message that will be delivered if a function completes successfully.
	 */
	static final String SUCCESS = "success";
	
	/**
	 * 
	 * Init function to initialize the client with the broker.
	 * @param broker
	 * The address of the broker to which the client should be connected
	 * An address has the IP address of the server where the broker runs, lead by the connection type and followed by the port.
	 * It could look like this "tcp://192.168.0.102:1883"
	 */
	public void init(String broker);

	/**
	 * 
	 * Sends the given content as a message to the given topic.
	 * @param topic
	 * The topic to which the content should be delivered.
	 * @param content
	 * The content that should be delivered to the topic as message.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	public String sendMessage(String topic, String content);
	
	/**
	 * Sends the given content as a message to the given topic.
	 * @param topic
	 * The topic to which the message should be sent.
	 * @param content
	 * The content that will be sent to the specified topic.
	 * @param qos
	 * The quality of service for the message that should be used.
	 * 0 for lowest and 2 for highest.
	 * 2 guarantees delivery if the broker is available but costs more resources.
	 * 1 guarantees delivery but can create more network traffic.
	 * 0 does not guarantee delivery.
	 * @param retained
	 * If retained is true the sent message will be retained by the topic.
	 * All new Subscribers will get this message even if they weren't subscribed at the time of delivery.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	public String sendMessage(String topic, String content, int qos, boolean retained);
	/**
	 *
	 * Subscribes the client to the given topic with the given receiver called by the callback of the client.
	 * @param topic
	 * The topic which should be subscribed to
	 * @param receiver
	 * The implementation of the IMqttReceiver interface which will be called by the callback of the client.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 *
	 */
	public String subscribe(String topic, IMqttReceiver receiver);

	/**
	 * Closes the client and makes it unusable.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	public String closeClient();

	/**
	 * Disconnects the client from the broker.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	public String disconnectClient();

	/**
	 * Connects the client to the broker
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	public String connectClient();

	/**
	 * Returns the clientId used to identify the client to the broker.
	 */
	public String getClientId();
	
	/**
	 * returns the connection status of the client
	 */
	public boolean getConnectionStatus();

}
