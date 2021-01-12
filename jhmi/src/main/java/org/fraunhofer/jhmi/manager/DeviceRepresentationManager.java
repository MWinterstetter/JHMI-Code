package org.fraunhofer.jhmi.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.fraunhofer.jhmi.user_interface.DeviceRepresentation;
import org.fraunhofer.jhmi.user_interface.HomeyDeviceRepresentation;
import org.fraunhofer.jhmi.util.ConnectionOptions;

/**
 * 
 * @author Matthias
 * 	This class Manages the DeviceRepresentations and it's subclasses.
 * 	A DeviceRepresentations can be seen as a representation of a device.
 * 	To utilize a DeviceRepresentation to it's fullest potential, provide the main topic of a device as the topic
 *	 	for the creation of the DeviceRepresentation followed by "/#" as this will save the newest update
 *		of each subtopic in the DeviceRepresentation.
 *	These updates can be accessed over the getDeviceValue function of the DeviceRepresentation. 
 * 
 */
public class DeviceRepresentationManager {
	
	/**
	 * The HashMap that contains the deviceRepresentations that are held by this manager.
	 */
	HashMap<String, DeviceRepresentation> deviceMap;
	/**
	 * The Message broker that will be used by all the clients of the deviceRepresentations that are held by this manager.
	 */
	final String broker;
	
	/**
	 * create a new DeviceRepresentationManager
	 * Use this to create new DeviceRepresentations to access the state of devices and send them commands.
	 * A DeviceRepresentation should be used for most interactions with a device.
	 * @param broker
	 * The broker that should be used for the DeviceRepresentations that are held by this manager.
	 */
	public DeviceRepresentationManager(String broker) {
		this.broker = broker;
		deviceMap = new HashMap<>();
	}
	
	/**
	 * Returns the DeviceRepresentation with the given name.
	 * @param representedDeviceName
	 * Name of the DeviceRepresentation you wish to access.
	 * @return
	 * Returns the specified DeviceRepresentation.
	 */
	public DeviceRepresentation getDeviceRepresentation(String representedDeviceName) {
		return deviceMap.get(representedDeviceName);
	}
	
	/**
	 * Add a new DeviceRepresentation to the manager.
	 * @param representedDeviceName
	 * The name that will be given to the DeviceRepresentation.
	 * This name must be unique.
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @param topic
	 * The topic that the DeviceRepresentation should subscribe to.
	 * The newest updates that will be sent under the given topic will be saved and can be accessed over the DeviceRepresentation object.
	 * Wildcard's are allowed if more that one topic's updates should be saved which should generally the case.
	 * Recommended topic is " .../'Name_of_the_device_that_should_be_represented'/#".
	 */
	public void addDeviceRepresentation(String representedDeviceName, ConnectionOptions connectionOptions, String topic) {
		deviceMap.put(representedDeviceName, new DeviceRepresentation(representedDeviceName, connectionOptions, broker, topic));
	}
	
	/**
	 * Add a new HomeyDeviceRepresentation to the manager.
	 * HomeyDeviceRepresentations must be given a unique name.
	 * @param representedDeviceName
	 * The name that will be given to the HomeyDeviceRepresentation.
	 * This name must be unique.
	 * @param connectionOptions
	 * The options that define the connection between the client and the broker.
	 * @param topic
	 * The topic that the DeviceRepresentation should subscribe to.
	 * The newest updates that will be sent under the given topic will be saved and can be accessed over the DeviceRepresentation object.
	 * Wildcard's are allowed if more that one topic's updates should be saved which should generally the case.
	 * Recommended topic is " .../'Name_of_the_device_that_should_be_represented'/#".
	 */
	public void addHomeyDeviceRepresentation(String representedDeviceName, ConnectionOptions connectionOptions, String topic) {
		deviceMap.put(representedDeviceName, new HomeyDeviceRepresentation(representedDeviceName, connectionOptions, broker, topic));
	}
	
	/**
	 * Remove the DeviceRepresentation with the given name.
	 * @param representedDeviceName
	 * The name of the DeviceRepresentation that will be removed.
	 */
	public void removeDeviceRepresentation(String representedDeviceName) {
		deviceMap.get(representedDeviceName).closeClient();
		deviceMap.remove(representedDeviceName);
	}

	/**
	 * Removes all DeviceRepresentations that are held by this manager.
	 */
	public void removeAllDeviceRepresentations() {
		ArrayList<String> keys = new ArrayList<>(deviceMap.keySet());
		for(String key: keys) {
			removeDeviceRepresentation(key);
		}
	}
	
	

}
