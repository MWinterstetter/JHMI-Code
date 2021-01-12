package org.fraunhofer.jhmi.mqtt_clients.paho;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.fraunhofer.jhmi.user_interface.IMqttReceiver;
import org.fraunhofer.jhmi.util.ClientInterface;


public class PahoClientContainer implements ClientInterface {
	
	private MqttClient client;
	private MqttConnectOptions options;
	private String clientId;	
	
	/**
	 * Default qos that should be used for the message unless otherwise specified.
	 */
	private int qos;

	Logger logger;

	/**
	 * Init function to initialize the client with the broker.
	 * @param broker
	 * The broker to which the client will connect.
	 */
	@Override
	public void init(String broker) {
		this.logger = Logger.getLogger(PahoClientContainer.class.getName());
		try {
			String id = MqttClient.generateClientId();
			MqttDefaultFilePersistence persistance = new MqttDefaultFilePersistence("/tmp");
			client = new MqttClient(broker, id, persistance);
			this.clientId = id;
		} catch (MqttException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		this.clientId = client.getClientId();
	}



	/**
	 * Sends the given content as a message to the given topic.
	 * @param topic
	 * The topic to which the message should be sent.
	 * @param content
	 * The content that will be sent to the specified topic.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	@Override
	public String sendMessage(String topic, String content) {
		MqttMessage message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
		message.setQos(this.qos);
		if(!client.isConnected()) {
			connectClient();
		}
		try {
			client.publish(topic, message);
			logger.fine("Message published");
		} catch (MqttException e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
	
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
	@Override
	public String sendMessage(String topic, String content, int qos, boolean retained) {
		MqttMessage message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
		if(0 <= qos && qos < 3) {
			message.setQos(qos);			
		} else {
			message.setQos(this.qos);
		}
		message.setRetained(retained);
		if(!client.isConnected()) {
			connectClient();
		}
		try {
			client.publish(topic, message);
			logger.fine("Message published");
		} catch (MqttException e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	 * Subscribes the client to the given topic with the given receiver called by the callback of the client.
	 * @param topic
	 * The topic to which the message should be sent.
	 * @param receiver
	 * The callback that defines how the message should be processed.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	@Override
	public String subscribe(String topic, IMqttReceiver receiver) {
		if(!client.isConnected()) {
			connectClient();
		}
		try {
            client.subscribe(topic, qos);
            client.setCallback( new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					String messageString = new String(message.getPayload(), StandardCharsets.UTF_8);
					receiver.messageReceived(topic, messageString, message.getId());	
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					//
				}
				
				@Override
				public void connectionLost(Throwable cause) {
					//
				}
			});
		}catch(MqttException e) {
            return e.getMessage();
		}
		
		return SUCCESS;
	}

	/**
	 * Closes the client and makes it unusable.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	@Override
	public String closeClient() {
		try {
			if(this.client.isConnected()) {
				this.client.disconnect();				
			}
			this.client.close();
		} catch (MqttException e) {
			if(e.getReasonCode() == MqttException.REASON_CODE_CLIENT_CLOSED) {
				return SUCCESS + " but client was already closed";
			}
            return e.getMessage();
		}
		return SUCCESS;
	}

	/**
	 * Returns the clientId used to identify the client to the broker.
	 */
	@Override
	public String getClientId() {
		return this.clientId;
	}

	/**
	 * Disconnects the client from the broker.
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	@Override
	public String disconnectClient() {
		try {
			this.client.disconnect();
			logger.info("Disconnected client");
		} catch (MqttException e) {
			if(e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
				return SUCCESS + " but the client was already disconnected";
			}
            return e.getMessage();
		}
		return SUCCESS;
	}

	/**
	 * Connects the client to the broker
	 * @return
	 * "success" if the client was successfully created, the exception message if not.
	 */
	@Override
	public String connectClient() {
		
		try {
			client.connect(options);
		} catch (MqttException e) {
			if(e.getReasonCode() == MqttException.REASON_CODE_CLIENT_CONNECTED) {
				return SUCCESS + " but the client was alrady connected";
			}
			return e.getMessage();
		}
		return SUCCESS;
	}

	/**
	 * Returns the connection status of the client
	 * @return
	 * true if client is connected and false if it is not.
	 */
	@Override
	public boolean getConnectionStatus() {
		return client.isConnected();
	}
	
	/**
	 * Set the connection options of the client
	 * @param options
	 */
	public void setMqttConnectionOptions (MqttConnectOptions options) {
		this.options = options;
	}
	
	/**
	 * Set the default qos
	 * @param qos
	 */
	public void setMessageQos(int qos) {
		this.qos = qos;
	}

}
