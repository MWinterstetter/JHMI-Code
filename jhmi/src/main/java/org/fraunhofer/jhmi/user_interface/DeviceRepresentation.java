package org.fraunhofer.jhmi.user_interface;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.fraunhofer.jhmi.mqtt_client.ClientInterfaceFactory;
import org.fraunhofer.jhmi.util.ClientInterface;
import org.fraunhofer.jhmi.util.ConnectionOptions;
import org.fraunhofer.jhmi.util.MessageEntry;


public class DeviceRepresentation {

	protected Logger logger;
	/**
	 * The name of the device that this DeviceRepresentaion should represent.
	 * It has to be unique.
	 */
	protected final String representedDeviceName;
	/**
	 * The broker to which the clients of this DeviceRepresentation should connect.
	 */
	protected final String broker;
	/**
	 * The topic which specifies which values will be saved.
	 * The values under the specified topic and all it's subtopics will be saved in the deviceValuesMap.
	 * To access the saved values call the getDeviceValue functions.
	 */
	protected final String deviceTopic;
	/**
	 * The ConnectionOptions for all the clients used by the DeviceRepresentation.
	 */
	protected final ConnectionOptions connectionOptions;
	/**
	 * The client responsible for filling the deviceValuesMap.
	 * It listens to the deviceTopic topic.
	 */
	protected ClientInterface client;
	/**
	 * The map that holds the saved message.
	 * The saved messages can be sent with the sendMessage function.
	 */
	protected HashMap<String, Map.Entry<String, String>> messageMap;
	/**
	 * The map that holds the values of the deviceTopic and all it's sub topics.
	 */
	protected HashMap<String, String> deviceValuesMap;
	/**
	 * The map that holds all the clients used by this DeviceRepresentation to add further callbacks to this DeviceRepresentation.
	 */
	protected HashMap<String, ClientInterface> clientMap;

	
	/**
	 * Creates a new DeviceRepresentation.
	 * @param representedDeviceName
	 * The name of the represented device.
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @param broker
	 * The broker over which status updates of the device a sent.
	 * @param topic
	 * The topic to which status updates of the device are sent.
	 */
	public DeviceRepresentation(String representedDeviceName, ConnectionOptions connectionOptions, String broker, String topic) {
		this.connectionOptions = connectionOptions;
		this.deviceTopic = topic + "/#";
		this.broker = broker;
		this.clientMap = new HashMap<>();
		logger = Logger.getLogger(DeviceRepresentation.class.getName());
		messageMap = new HashMap<>();
		deviceValuesMap = new HashMap<>();
		this.representedDeviceName = representedDeviceName;
		
		client = ClientInterfaceFactory.createClientInterface(broker, connectionOptions);
		IMqttReceiver receiver = new IMqttReceiver() {
			
			@Override
			public void messageReceived(String topic, String messageString, int messageId) {
				deviceValuesMap.put(topic, messageString);			
			}
		};
		client.subscribe(deviceTopic, receiver);
		
	}

	/**
	 * Set the callback that should be executed if a message has been received under the subscribed topic.
	 * @param topic
	 * The topic that should be listened to.
	 * @param receiver
	 * An implementation of the IMqttReceiver object which will be called when a message has been received.
	 */
	public String addCallback(String topic, IMqttReceiver receiver ) {
		
		ClientInterface newClient = ClientInterfaceFactory.createClientInterface(broker, connectionOptions);
		clientMap.put(topic, newClient);
		
		return client.subscribe(topic, receiver);
	
	}
	
	/**
	 * Disables the callback for the specified topic.
	 * The callback can be enabled again by the enableCallback function.
	 * @param topic
	 * The topic who the callback that should be disabled is listening to
	 * @return
	 * Success or failure of disabling the client.
	 */
	public String disableCallback(String topic) {
		return clientMap.get(topic).disconnectClient();
		
	}
	
	/**
	 * Enable the callback for the specified topic.
	 * @param topic
	 * The topic who the callback that should be enabled was listening to.
	 * @return
	 * Success or failure of enabling the client.
	 */
	public String enableCallback(String topic) {
		return clientMap.get(topic).connectClient();
	}
	
	/**
	 * Permanently removes the callback.
	 * @param topic
	 * The topic who the callback that should be removed is listening to
	 * @return
	 * Success or failure of removing the client.
	 */
	public String removeCallback(String topic) {
		return clientMap.get(topic).closeClient();
	}
	
	/**
	 * Register a new message to be sent over the sendMessage method. registering a message does not send the message.
	 * @param name
	 * The name of the message which will be used to send the message with the sendMessage method.
	 * @param topic
	 * The topic for which the message is intended. this is not restricted by the topic given for the constructor.
	 * @param value
	 * The value that will be sent to the topic upon sending the message.
	 */
	public void registerMessage(String name, String topic, String value) {
		MessageEntry entry = new MessageEntry(topic, value);
		
		messageMap.put(name, entry);
	}
	
	/**
	 * Send a registered message.
	 * @param name
	 * The name of the registered message that should be sent.
	 * @return
	 * The result of the sending of the message.
	 */
	public String sendMessage(String name) {
		return client.sendMessage(messageMap.get(name).getKey(), messageMap.get(name).getValue());
	}
	
	/**
	 * Send a registered message.
	 * @param name
	 * The name of the registered message that should be sent.
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
	 * The result of the sending of the message.
	 */
	public String sendMessage(String name, int qos, boolean retained) {
		return client.sendMessage(messageMap.get(name).getKey(), messageMap.get(name).getValue(), qos, retained);
	}
	
	/**
	 * Get the newest message that was provided under a given topic.
	 * @param key
	 * The topic under which the message was sent.
	 * @return
	 * The newest message of the given topic.
	 */
	public String getDeviceValue(String key){
		return deviceValuesMap.get(key);
	}
	
	/**
	 * Close the client of the HomeyDeviceRepresentation.
	 * @return
	 * The result of closing the client.
	 */
	public String closeClient() {
		return client.closeClient();
		
	}

}
