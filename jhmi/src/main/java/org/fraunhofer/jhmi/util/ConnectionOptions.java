package org.fraunhofer.jhmi.util;

public class ConnectionOptions {

	/**
	 * Determines if the client should automatically reconnect to the broker if connection was cut.
	 * By default this is set to true.
	 */
	private Boolean autoReconnect = true;
	/**
	 * Determines if the client should remember it's state between reconnects.
	 * By default this is set to true.
	 */
	private Boolean cleanSession = true;
	/**
	 * Determines the time in seconds that the client will wait to establish it's connection to the broker.
	 * By default this value is 30 Seconds.
	 */
	private int keepAlive = 30;
	/**
	 * Determines the maximum time between messages that the client uses to determine if the server is still available.
	 * This can be either a message received or a message sent.
	 * The client will sent a "ping" message if no messages had to be received or sent within this time frame.
	 * By default this value is set to 60
	 */
	private int connectionTimeout = 60;
	/**
	 * Determines the maximum number of messages that can be "inflight" at the same time in relation to this client.
	 * A message is "inflight" if it is sent but it's arrival has not yet been acknowledged.
	 * By default this value is set to 10.
	 */
	private int maxInflight = 10;
	/**
	 * The quality of service for the message that should be used.
	 * 0 for lowest and 2 for highest.
	 * 2 guarantees delivery if the broker is available but costs more resources.
	 * 1 guarantees delivery but can create more network traffic.
	 * 0 does not guarantee delivery.
	 */
	private int qos = 2;
	private char[] password;
	private String username;
	private String lastWillTopic;
	private String lastWillMessage;
	
	/**
	 * The type of client that should be used for the connection with the broker.
	 * @param clientType
	 * The type of client you want to create.
	 * For more Information towards specific clients, please refer to the Javadoc of the ClientType Enum and it's individual Types.
	 */
	private ClientType clientType = ClientType.DEFAULT;
	
	/**
	 * Creates the default ConnectionOptions with 
	 * autoConnect and CleanSession set to true
	 * keepAlive = 30
	 * connectionTimeout = 60
	 * maxInflight = 10
	 * qos = 2.
	 * 
	 */
	public ConnectionOptions() {
		
	}
	
	/**
	 * Creates the default ConnectionOptions with 
	 * autoConnect and CleanSession set to true
	 * keepAlive = 30
	 * connectionTimeout = 60
	 * maxInflight = 10
	 * qos = 2.
	 * Also set's the specified password and username for authentification
	 * @param password
	 * The password for the authentification
	 * @param username
	 * The username for the authentification
	 */
	public ConnectionOptions (char[] password, String username) {
		this.password = password;
		this.username = username;
	}
	
	/**
	 * Set's keepAlive to 45
	 * and connectionTimeout to 90
	 * and maxInflight to 7
	 */
	public void unstableNetworkOptions() {
		keepAlive = 45;
		connectionTimeout = 90;
		maxInflight = 7;
	}
	
	/**
	 * Set's keepAlive to 15
	 * and connectionTimeout to 30
	 * and maxInflight to 15
	 */
	public void stableNetworkOptions() {
		keepAlive = 15;
		connectionTimeout = 30;
		maxInflight = 15;
	}
	
	/**
	 * Specifies a new value for keepAlive value
	 * @param newKeepAlive
	 * Determines the time in seconds that the client will wait to establish it's connection to the broker.
	 * By default this value is 30 Seconds.
	 */
	public void setKeepAlive(int newKeepAlive) {
		keepAlive = newKeepAlive;
	}

	/**
	 * Get the specified value for keepAlive.
	 * Determines the time in seconds that the client will wait to establish it's connection to the broker.
	 * By default this value is 30 Seconds.
	 * @return
	 * The specified keepAlive value.
	 */
	public int getKeepAlive() {
		return keepAlive;
	}

	/**
	 * Specifies a new value for autoReconnect value.
	 * @param newAutoReconnect
	 * Determines if the client should automatically reconnect to the broker if connection was cut.
	 * By default this is set to true.
	 */
	public void setAutoReconnect(Boolean newAutoReconnect) {
		autoReconnect = newAutoReconnect;
	}

	/**
	 * Get the specified value for autoReconnect.
	 * Determines if the client should automatically reconnect to the broker if connection was cut.
	 * By default this is set to true.
	 * @return
	 * The specified value for autoReconnect.
	 */
	public Boolean getAutoReconnect() {
		return autoReconnect;
	}

	/**
	 * Specifies a new value for cleanSession.
	 * @param newCleanSession
	 * Determines if the client should remember it's state between reconnects.
	 * By default this is set to true.
	 */
	public void setCleanSession(Boolean newCleanSession) {
		cleanSession = newCleanSession;
	}

	/**
	 * Get the specified value for cleanSession.
	 * Determines if the client should remember it's state between reconnects.
	 * By default this is set to true.
	 * @return
	 * The specified cleanSession value.
	 */
	public Boolean getCleanSession() {
		return cleanSession;
	}

	/**
	 * Specifies a new value for connectionTimeout
	 * @param newConnectionTimeout
	 * Determines the maximum time between messages that the client uses to determine if the server is still available.
	 * This can be either a message received or a message sent.
	 * The client will sent a "ping" message if no messages had to be received or sent within this time frame.
	 * By default this value is set to 60

	 */
	public void setConnectionTimeout(int newConnectionTimeout) {
		connectionTimeout = newConnectionTimeout;
	}

	/**
	 * Get the specified value for connectionTimeout
	 * Determines the maximum time between messages that the client uses to determine if the server is still available.
	 * This can be either a message received or a message sent.
	 * The client will sent a "ping" message if no messages had to be received or sent within this time frame.
	 * By default this value is set to 60
	 * @return
	 * The specified connectionTimeout value.
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Specifies a new value for maxInflight.
	 * @param newMaxInflight
	 * Determines the maximum number of messages that can be "inflight" at the same time in relation to this client.
	 * A message is "inflight" if it is sent but it's arrival has not yet been acknowledged.
	 * By default this value is set to 10.
	 */
	public void setMaxInflight(int newMaxInflight) {
		maxInflight = newMaxInflight;
	}

	/**
	 * Get the specified value for maxInflight.
	 * Determines the maximum number of messages that can be "inflight" at the same time in relation to this client.
	 * A message is "inflight" if it is sent but it's arrival has not yet been acknowledged.
	 * By default this value is set to 10.
	 * @return
	 * The value for MaxInflight.
	 */
	public int getMaxInflight() {
		return maxInflight;
	}

	/**
	 * Specifies a new password.
	 * @param newPassword
	 * The password for authentification
	 */
	public void setPassword(char[] newPassword) {
		password = newPassword;
	}

	public char[] getPassword() {
		return password;
	}

	/**
	 * Specifies a new username.
	 * @param newUsername
	 * The username for authentification
	 */
	public void setUsername(String newUsername) {
		username = newUsername;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * Specifies a last will message and topic.
	 * @param newLastWillMessage
	 * The last will message that will be sent when the client disconnects.
	 * @param newLastWillTopic
	 * The Topic to which the message will be sent.
	 */
	public void setLastWill( String newLastWillMessage, String newLastWillTopic) {
		lastWillMessage = newLastWillMessage;
		lastWillTopic = newLastWillTopic;
	}

	/**
	 * Get the specified lastWillMessage
	 * The last will message that will be sent when the client disconnects.
	 * @return
	 * The lastWillMessage
	 */
	
	public String getLastWillMessage() {
		return lastWillMessage;
	}

	/**
	 * Get the specified lastWillTopic
	 * @return
	 * The Topic to which the lastWillMessage message will be sent.
	 */
	public String getLastWillTopic() {
		return lastWillTopic;
	}
	/**
	 * Get the specified clientType
	 * The type of client you want to create.
	 * For more Information towards specific clients, please refer to the Javadoc of the ClientType Enum and it's individual Types.
	 * @return
	 * The specified clientType
	 */
	public ClientType getClientType() {
		return clientType;
	}

	/**
	 * Specifies a new clientType.
	 * @param clientType
	 * The type of client you want to create.
	 * For more Information towards specific clients, please refer to the Javadoc of the ClientType Enum and it's individual Types.
	 */
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	/**Get the specified qos.
	 * The quality of service for the message that should be used.
	 * 0 for lowest and 2 for highest.
	 * 2 guarantees delivery if the broker is available but costs more resources.
	 * 1 guarantees delivery but can create more network traffic.
	 * 0 does not guarantee delivery.
	 * @return
	 * The specified qos
	 */
	public int getQos() {
		return qos;
	}

	/**Specifies a new value for qos.
	 * @param qos
	 * The quality of service for the message that should be used.
	 * 0 for lowest and 2 for highest.
	 * 2 guarantees delivery if the broker is available but costs more resources.
	 * 1 guarantees delivery but can create more network traffic.
	 * 0 does not guarantee delivery.
	 */
	public void setQos(int qos) {
		if( 0 <= qos && qos <3) {			
			this.qos = qos;
		}
	}
	
	
	
}
