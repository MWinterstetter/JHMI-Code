package org.fraunhofer.jhmi.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.fraunhofer.jhmi.mqtt_client.ClientInterfaceFactory;
import org.fraunhofer.jhmi.user_interface.IMqttReceiver;
import org.fraunhofer.jhmi.util.ClientInterface;
import org.fraunhofer.jhmi.util.ConnectionOptions;
/** 
 * 
 * @author Matthias
 *	The ReceiverManager, which can store multiple clients that subscribe to a specified topic with a provided callback.
 *		The Callback that you will need to provide will react whenever a message arrives on a subscribed topic
 *		and execute whatever code you write into the interface.
 */
public class MqttReceiverManager {
	
	Logger logger;
	/**
	 * The HashMap that contains all the clients(subscribers) that are held by this manager.
	 */
	HashMap<String, ClientInterface> subscriberMap;
	
	/**
	 * The Message broker that will be used by all the clients of the deviceRepresentations that are held by this manager.
	 */
	String broker;
	
	/**
	 * Manager for message reception.
	 * Enables the creation of new Subscribers for topics.
	 * Use the newSubscriber method to get started.
	 * @param broker
	 * The broker that should be used for the DeviceRepresentations that are held by this manager.
	 */
	public MqttReceiverManager(String broker) {
		this.logger = Logger.getLogger(MqttSenderManager.class.getName());
		this.subscriberMap = new HashMap<>();
		this.broker = broker;
	}
	
	/**
	 * Creates a new subscriber with a given callback.
	 * @param topic
	 * The topic that the subscription is for.
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @param receiver
	 * The implementation of the IMqttReceiver interface that describes what should be done with the received message.
	 * @return
	 * The id of the created subscriber.
	 */
	public String newSubscriber(String topic, ConnectionOptions connectionOptions, IMqttReceiver receiver) {
						
        ClientInterface client = ClientInterfaceFactory.createClientInterface(broker, connectionOptions);
        client.subscribe(topic, receiver);
        
		subscriberMap.put(client.getClientId(), client);
		
		return client.getClientId();
	}
	
	/**
	 * Disconnects a specified subscriber.
	 * To reconnect the subscriber use the connectSubscriber function.
	 * @param clientId
	 * The Id of the client that should be disconnected.
	 * This is the value that is returned upon the creation of a new subscriber.
	 * @return
	 * The result of the disconnection of the subscriber.
	 */
	public String disconnectSubscriber(String clientId) {
		return subscriberMap.get(clientId).disconnectClient();
	}
	
	/**
	 * Connects a specified subscriber.
	 * A new subscriber is automatically connected and does not need to be explicitly connected with this function.
	 * This function is for reconnecting a disconnected subscriber.
	 * @param clientId
	 * The Id of the client that should be connected.
	 * This is the value that is returned upon the creation of a new subscriber.
	 * @return
	 * The result of connecting the subscriber.
	 */
	public String connectSubscriber(String clientId) {
		return subscriberMap.get(clientId).connectClient();
	}
	
	/**
	 * Remove a specified subscriber.
	 * After the subscriber has been removed it can no longer be used.
	 * @param clientId
	 * The Id of the client that should be removed.
	 * This is the value that is returned upon the creation of a new subscriber.
	 * 
	 */
	public void removeSubscriber(String clientId) {
		subscriberMap.get(clientId).closeClient();
	}
	
	/**
	 * Removes all Subscribers that this manager holds.
	 * All subscribing clients will be closed and will no longer receive any messages.
	 */
	public void removeAllSubscribers() {
		ArrayList<String> keys = new ArrayList<>(subscriberMap.keySet());
		for(String key: keys) {
			removeSubscriber(key);
		}
	}

}
