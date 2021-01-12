package org.fraunhofer.jhmi.mqtt_client;

import org.fraunhofer.jhmi.mqtt_clients.paho.PahoMqttClientFactory;
import org.fraunhofer.jhmi.util.ClientInterface;
import org.fraunhofer.jhmi.util.ConnectionOptions;

public class ClientInterfaceFactory {
	
	private ClientInterfaceFactory() {
		
	}

	/*
	 * Factory for creating different client's. If a new client needs to be managed please add it to the switch case below.
	 * The new client and it's subtypes also need to be added to the clientType enum.
	 * To change the default client, replace the line under the DEFAULT case to create the desired client
	 * @param broker
	 * The broker to which the new client will connect.
	 * @param connectionOptions
	 * The options that will define the connection between the client and the broker.
	 */
	public static ClientInterface createClientInterface(String broker, ConnectionOptions connectionOptions) {
		switch (connectionOptions.getClientType()) {
		case PAHO:
			return PahoMqttClientFactory.createMqttClient(broker, connectionOptions);
		case DEFAULT:
			return PahoMqttClientFactory.createMqttClient(broker, connectionOptions);
		default:
			return PahoMqttClientFactory.createDefaultMqttClient(broker);
		}
	}
	
}
