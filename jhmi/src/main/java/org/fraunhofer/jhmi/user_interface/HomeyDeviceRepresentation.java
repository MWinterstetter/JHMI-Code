package org.fraunhofer.jhmi.user_interface;

import java.util.HashMap;

import org.fraunhofer.jhmi.util.ConnectionOptions;

public class HomeyDeviceRepresentation extends DeviceRepresentation {

	/**
	 * Topic under which the Homey MqttHub listens for commands.
	 */
	protected final String homeyCommandPath;
	
	/**
	 * Map of saved commands which can be sent through the send command function.
	 */
	protected HashMap<String, String> commands;
	
	/**
	 * Create a new HomeyDeviceRepresentation object that represents a device connected to a Homey.
	 * @param representedDeviceName
	 * the name that has been given to the device in Homey
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @param broker
	 * The broker for that the Homey is connected to and on which status updates of the device are sent
	 * @param topic
	 * The topic under which status updates of the device are sent
	 */
	public HomeyDeviceRepresentation(String representedDeviceName, ConnectionOptions connectionOptions, String broker, String topic) {
		super(representedDeviceName, connectionOptions, broker, topic);		
		commands = new HashMap<>();
		homeyCommandPath = topic.toLowerCase().split("/")[1] + "/$command"; 
		
		IMqttReceiver receiver = new IMqttReceiver() {
			
			@Override
			public void messageReceived(String topic, String messageString, int messageId) {
				String capability = topic.toLowerCase().replace(deviceTopic, "");
				if(capability.subSequence(0, 1).equals("/")) {
					capability = topic.split("/")[1];					
				} else {
					capability = topic.split("/")[0];
				}
				deviceValuesMap.put(capability, messageString);			
			}
		};
		client.subscribe(topic + "/#", receiver);
	}

	
	/**
	 * Register a command for later usage.
	 * @param commandId
	 * The id that the command should be given.
	 * @param command
	 * The command keyword that should be executed.
	 * @param capability
	 * The capability for which the command is intended.
	 * @param value
	 * The value that the capability should assume after the command has been executed.
	 */
	public void registerCommand(String commandId, String command, String capability, String value) {
		
		 String content= "{"
				 +"\"command\":\""+ command +"\","
				 +"\"device\":{"
				 +"\"name\":\"" + representedDeviceName + "\""
				 +"},"
				 +"\"capability\":\"" + capability + "\","
				 +"\"value\":\"" + value + "\""
				 +"}";
		 
		 commands.put(commandId, content);
	}
	
	/**
	 * Execute a registered command.
	 * @param commandId
	 * The id under which the command has been registered.
	 * @return the result of the sending of the command
	 */
	public String sendCommand(String commandId) {		
		return client.sendMessage(homeyCommandPath, commands.get(commandId));
	}
	
	/**
	 * Returns the currently saved value for the given capability
	 * @param capability
	 * The capability whose value will be returned
	 * @return 
	 * The value of the specified capability.
	 */
	@Override
	public String getDeviceValue(String capability) {
		return deviceValuesMap.get(capability);
	}
		
}
