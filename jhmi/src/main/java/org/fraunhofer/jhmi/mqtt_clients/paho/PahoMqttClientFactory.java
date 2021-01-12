package org.fraunhofer.jhmi.mqtt_clients.paho;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.fraunhofer.jhmi.util.ConnectionOptions;

public class PahoMqttClientFactory {

	private PahoMqttClientFactory() {
		
	}
	
	/**
	 * Factory for creating the different types of clients.
	 * The clients should be differentiated by the clientType.
	 * @param broker
	 * The address for the broker to which the client should connect.
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @return
	 * The client created according to the provided clientType.
	 */
	public static PahoClientContainer createMqttClient(String broker, ConnectionOptions connectionOptions) {
		PahoClientContainer client = createMqttClient(broker);
		MqttConnectOptions options = new MqttConnectOptions();
		
		setAuthentification(options, connectionOptions.getPassword(), connectionOptions.getUsername());
		setReconnect(options, connectionOptions.getAutoReconnect());
		setCleanSession(options, connectionOptions.getCleanSession());
		setConnectionTimeout(options, connectionOptions.getConnectionTimeout());
		setKeepAlive(options, connectionOptions.getKeepAlive());
		setMaxInflight(options, connectionOptions.getMaxInflight());
		setLastWill(options, connectionOptions.getLastWillMessage(), connectionOptions.getLastWillTopic());
		
		client.setMqttConnectionOptions(options);
		client.setMessageQos(connectionOptions.getQos());
		return client;
	}
	
	/**
	 * Create a client with default MqttConnectionOptions.
	 * @param broker
	 * The broker to which the client should connect
	 * @return
	 * The client that was created.
	 */
	public static PahoClientContainer createDefaultMqttClient(String broker) {
		PahoClientContainer client = createMqttClient(broker);
		client.setMqttConnectionOptions(new MqttConnectOptions());
		return client;
		
	}

	/**
	 * Create a new MqttClient with the given broker
	 * @param broker
	 * The address of the Mqtt broker to which the client should connect
	 * @return client
	 * The Client that was created.
	 */
	private static PahoClientContainer createMqttClient(String broker) {
		PahoClientContainer client = new PahoClientContainer();
		client.init(broker);
		return client;			
	}
	
	
	private static void setAuthentification (MqttConnectOptions options, char[] password, String username) {
		if(password.length > 0 && !username.isEmpty()) {
			options.setPassword(password);
			options.setUserName(username);			
		}
	}
	
	private static void setReconnect(MqttConnectOptions options, boolean autoReconnect) {
		options.setAutomaticReconnect(autoReconnect);
	}
	
	private static void setCleanSession(MqttConnectOptions options, boolean cleanSession) {
		options.setCleanSession(cleanSession);
		
	}
	
	private static void setConnectionTimeout(MqttConnectOptions options, int connectionTimeout) {
		options.setConnectionTimeout(connectionTimeout);
	}
	
	private static void setKeepAlive(MqttConnectOptions options, int keepAlive) {
		options.setKeepAliveInterval(keepAlive);
	}
	
	private static void setMaxInflight(MqttConnectOptions options, int maxInflight) {
		options.setMaxInflight(maxInflight);
	}
	
	private static void setLastWill(MqttConnectOptions options, String lastWillMessage, String lastWillTopic) {
		if(lastWillMessage != null && lastWillTopic != null) {
			options.setWill(lastWillTopic, lastWillMessage.getBytes(), 2, true);			
		}
	}
}
