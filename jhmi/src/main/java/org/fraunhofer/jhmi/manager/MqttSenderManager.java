package org.fraunhofer.jhmi.manager;

import java.util.HashMap;
import java.util.logging.Logger;


import org.fraunhofer.jhmi.mqtt_client.ClientInterfaceFactory;
import org.fraunhofer.jhmi.util.ClientInterface;
import org.fraunhofer.jhmi.util.ConnectionOptions;
import org.fraunhofer.jhmi.util.StringTripple;

public class MqttSenderManager {

	Logger logger;

	/**
	 * The HashMap that contains the clients that are held by this manager.
	 */
	HashMap<String, ClientInterface> clientMap;
	/**
	 * The HashMap that contains the saved Messages.
	 * The key is the name of the message which was specified on the saveMessage function call.
	 * The StringTripple contains in order the clientId the topic and the content of the saved message.
	 */
	HashMap<String, StringTripple> messageMap;
	/**
	 * The broker that will be used by all the clients held by this manager.
	 */
	String broker;
	
	/**
	 * The manager responsible for sending messages and Homey commands.
	 * @param broker
	 * The broker that will be used by all the clients held by this manager.
	 */
	public MqttSenderManager(String broker) {
		clientMap = new HashMap<>();
		this.logger = Logger.getLogger(MqttSenderManager.class.getName());
		this.broker = broker;
	}
	
	/**
	 * Quick send message function
	 * This will create a single use client and use the default clientType for sending the specified message to the specified topic.
	 * @param topic
	 * The topic to which the message should be send.
	 * @param content
	 * The content of the message.
	 * @param options
	 * The options that define the connection between the client and the broker.
	 * @return returns the success or failure of the message transmission.
	 */
	public String sendMessage(String topic, String content, ConnectionOptions options) {
		ClientInterface client = ClientInterfaceFactory.createClientInterface(broker, options);
		String result = client.sendMessage(topic, content);
		client.closeClient();
		return result;
	}
	

	/**
	 * This function is used to send a message to a given topic.
	 * @param topic
	 * The topic to which this message should be send.
	 * @param clientId
	 * The Id of the client that should be used to send the message.
	 * @param content
	 * The content of the message
	 * @return returns the success or failure of the message transmission.
	 */
	public String sendMessage(String topic, String clientId, String content) {
		if(!clientMap.containsKey(clientId)) {
			return "failure no client with this name exists";
		}
		ClientInterface client = clientMap.get(clientId);
		
		return client.sendMessage(topic, content);
	}
	
	/**
	 * This function is used to send a message to a given topic.
	 * @param topic
	 * The topic to which this message should be send.
	 * @param clientId
	 * The Id of the client that should be used to send the message.
	 * @param content
	 * The content of the message
	 * @param qos
	 * The quality of service for the message that should be used.
	 * 0 for lowest and 2 for highest.
	 * 2 guarantees delivery if the broker is available but costs more resources.
	 * 1 guarantees delivery but can create more network traffic.
	 * 0 does not guarantee delivery.
	 * @param retained
	 * If retained is true the sent message will be retained by the topic.
	 * All new Subscribers will get this message even if they weren't subscribed at the time of delivery.
	 * @return returns the success or failure of the message transmission.
	 */
	public String sendMessage(String topic, String clientId, String content, int qos, boolean retained) {
		if(!clientMap.containsKey(clientId)) {
			return "failure no client with this name exists";
		}
		ClientInterface client = clientMap.get(clientId);
		return client.sendMessage(topic, content, qos, retained);
	}
	
	/**
	 * Send a previously saved message by the specified name.
	 * @param messageName
	 * The messageName that was given for calling the saveMessage function
	 * @return returns the success or failure of the message transmission.
	 * 
	 */
	public String sendMessage(String messageName) {
		StringTripple messageValues = messageMap.get(messageName);
		ClientInterface client = clientMap.get( messageValues.getValue1());
		return client.sendMessage(messageValues.getValue2(), messageValues.getValue3());
	}	
	
	/**
	 * Send a previously saved message by the specified name.
	 * @param messageName
	 * The messageName that was given for calling the saveMessage function
	 * @param qos
	 * The quality of service for the message that should be used.
	 * 0 for lowest and 2 for highest.
	 * 2 guarantees delivery if the broker is available but costs more resources.
	 * 1 guarantees delivery but can create more network traffic.
	 * 0 does not guarantee delivery.
	 * @param retained
	 * If retained is true the sent message will be retained by the topic.
	 * All new Subscribers will get this message even if they weren't subscribed at the time of delivery.
	 * @return returns the success or failure of the message transmission.
	 * 
	 */
	public String sendMessage(String messageName, int qos, boolean retained) {
		StringTripple messageValues = messageMap.get(messageName);
		ClientInterface client = clientMap.get( messageValues.getValue1());
		return client.sendMessage(messageValues.getValue2(), messageValues.getValue3(), qos, retained);
	}	
	
	/**
	 * Add a client to the manager that can be used to send messages or commands.
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @return
	 * The success or failure of the operation
	 */
	public String addMqttClient(ConnectionOptions connectionOptions){
		ClientInterface client = ClientInterfaceFactory.createClientInterface(broker, connectionOptions);
		clientMap.put(client.getClientId(), client);
		return client.getClientId();
	}
	
	/**
	 * Saves a message to be sent at a later time.
	 * @param messageName
	 * The name for the message which needs to be specified in the sendMessage function.
	 * @param topic
	 * The topic to which this message should be send upon calling the sendMessage function
	 * @param clientId
	 * The clientId for the client that should be used for sending the specified message.
	 * @param content
	 * The content that will be send in the message.
	 * @return
	 * The success or failure of sending the message.
	 */
	public String saveMessage(String messageName, String topic, String clientId, String content) {
		if(!clientMap.containsKey(clientId)) {
			return "failure no client with the specified name exists";
		}
		if(!messageMap.containsKey(messageName)) {
			return "failure no message with this name exists";
		}
		messageMap.put(messageName, new StringTripple(clientId, topic, content));
		return "success";
	}
	
	/**
	 * Removes a saved message.
	 * @param messageName
	 * The name of the message that should be removed.
	 * @return
	 * The success or failure of removing the message.
	 * 
	 */
	public String removeMessage(String messageName) {
		if(!clientMap.containsKey(messageName)) {
			return "failure no message with this name exists";
		}
		return "success";
	}

	/**
	 * Closes the specified client
	 * The client can not be used anymore after it is closed.
	 * @param name
	 * The name of the client that should be closed.
	 * @return
	 * The success or failure of closing the client.
	 */
	public String closeMqttClient(String name) {
		return clientMap.get(name).closeClient();
	}
	/**
	 * Disconnects the specified client
	 * @param name
	 * The name of the client that should be disconnected.
	 * @return
	 * The success or failure of disconnecting the client.
	 */	
	public String disconnectClient(String name) {
		return clientMap.get(name).disconnectClient();
	}
	
	/**
	 * Disconnects the specified client
	 * @param name
	 * The name of the client that should be connected.
	 * @return
	 * The success or failure of connecting the client.
	 */
	public String connectClient(String name) {
		return clientMap.get(name).connectClient();
	}
	
	
}
