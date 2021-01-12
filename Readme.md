## Java Home Mqtt Interface JHMI

Diese Bibliothek ist ein Java Interface für Smart Home durch MQTT. Sie ist auf Paho als MQTT Client und Homey als Hub zur Kontrolle des Smart Homes ausgelegt. Mögliche Erweiterungen für weitere Clients oder Smart Home Hub Systeme sind aber mit in die Architektur einbezogen worden.

JHMI baut auf MQTT auf und bietet keine Funktionen, an welche nicht auch durch Beispielsweise den Paho Client möglich währen. Das Ziel von JHMI ist es die Integration von Software in die Smart Home Umgebung zu erleichtern und bietet dafür eine leicht zu verwendende Bibliothek an welche einige Quality of Live Funktionalitäten für die Kommunikation mit dem Smart Home Hub bietet. Sofern Erweiterungen für weitere Clients und Hubs zur Verfügung stehen ist durch diese Bibliothek auch der Austausche der MQTT Clients und der Smart Home Hubs möglich ohne den Java Code anpassen zu müssen.

## Verwenden der Biblithek

JHMI verfügt über drei Kern Funktionalitäten die in diesem Abschnitt erläutert werden. Mit ihnen ist es möglich Nachrichten zu empfangen, zu senden und eine DeviceRepresentation zu erstellen welche den Zustand eines Gerätes im Smart Home darstellen soll.

**MqttSenderManager**

Die Standardmethode zum Senden von Nachrichten mit dem MqttSenderManager wird im folgenden Code Snippet dargestellt.

```Java
ConnectionOptions options = new ConnectionOptions();
MqttSenderManager sender = new MqttSenderManager("tcp://127.0.0.1:1883");
String client = sender.addMqttClient(options);
String result1 = sender.sendMessage("testopic/test", client, "Die erste Nachricht");

String result2 = sender.sendMessage("testopic/test", client, "Die zweite Nachricht");
    
sender.closeMqttClient(client);
```

Durch diesen, wie oben dargestellten Code, wird ein MqttManager erstellt welcher Clients verwalten soll die sich zu dem Broker unter der Adresse tcp://127.0.0.1:1883 verbinden sollen. Für diesen Manager wird daraufhin ein Client erstellt welcher zwei Nachrichten schickt. Zuerst wird die erste Nachricht an das Topic "testopic/test" geschickt und das Ergebnis in result1 gespeichert, die Zweite Nachricht wird an dasselbe Topic gesendet und das Ergebnis des Sendens der zweiten Nachricht wird in result2 gespeichert. Nach dem Senden der beiden Nachrichten wird der erstellt Client geschlossen.

Im Folgenden wird der Code Zeile für Zeile beschrieben.


```Java
ConnectionOptions options = new ConnectionOptions();
 ```
In dieser Zeile wird ein neues ConnectionOptions Objekt erstellt. In diesem Objekt werden die verschiedenen Optionen gespeichert welche für die Verbindung zwischen dem Client und dem Broker verwendet werden können. Dazu gehört Beispielsweise, ob sich der Client automatisch wieder mit dem Broker verbinden soll, wenn die Verbindung abgebrochen ist. Weiter Informationen zu den Optionen können in den Javadocs gefunden werden.

**Wichtig!** Über die ConnectionOptions werden auch der Nutzername und das Passwort für den Broker gesetzt zu dem sich der Client Verbinden soll.

```Java
MqttSenderManager sender = new MqttSenderManager("tcp://127.0.0.1:1883");
 ```
In dieser Zeile wird ein neuer MqttSenderManager erstellt. Jeder Client der durch diesen Manager erstellt wird, verbindet sich zu dem Broker der unter 127.0.0.1:1883
läuft.

```Java
String client = sender.addMqttClient(options);
 ```
In dieser Zeile wird ein neuer Client für den MqttSenderManager "sender" erstellt. Dieser Client verwendet die ConnectionOptions "options" für seine Verbindung zum Broker. Beim Erstellen des Client wird für den Client ein Name generiert, welcher in der Variable Client gespeichert wird. Dieser Name wird beim Senden benötigt, um ein Client zu spezifizieren, welcher die Nachricht an das spezifiziert Topic sendet.

```Java
String result1 = sender.sendMessage("testopic/test", client, "Die erste Nachricht");
String result2 = sender.sendMessage("testopic/test", client, "Die zweite Nachricht");
 ```
Durch diese beiden Zeilen wird jeweils eine Nachricht gesendet. Die Nachricht "Die erste Nachricht" wird an das Topic "testopic/test". Das Ergebnis des Sendens der Nachricht wird in result1 gespeichert. Dasselbe gilt für die zweite Nachricht "Die zweite Nachricht".

```Java
sender.closeMqttClient(client);
 ```
Die letzte Zeile schließt den spezifizierten Client und macht ihn unverwundbar. Um neue Nachrichten zu schicken, muss ein anderer Client verwendet werden.

**MqttReceiverManager**

Der ReceiverManager verhält sich ähnlich wie der SenderManager. Wie ein ReceiverManager erstellt und verwendet werden kann wird im Folgenden dargestellt.

```Java
ConnectionOptions options2 = new ConnectionOptions();
MqttReceiverManager receiver = new MqttReceiverManager("tcp://127.0.0.1:1883");
String receiverClient = receiver.newSubscriber("testopic/test", options2, new IMqttReceiver() {
    @Override
    public void messageReceived(String topic, String messageString, int messageId) {
        //Do Something here
    }
});
```

Wie auch beim Sender muss beim Receiver ein Client erstellt werden, dem Optionen übergeben werden müssen, welche die Verbindung mit dem Broker beschreiben. Anders als beim Senden kann ein Client nur ein Topic Abonnieren. Wenn ein neues Topic abonniert werden soll, muss also auch ein neuer Client über den Befehl newSubscriber erstellt werden. Eine empfangene Nachricht wird über das IMqttReceiver Interface verarbeitet welches beim Erstellen des Clients übergeben werden muss.

```Java
String receiverClient = receiver.newSubscriber("testopic/test", options2, new IMqttReceiver() {
    @Override
    public void messageReceived(String topic, String messageString, int messageId) {
        //Do Something here
    }
});
```

Dieses Interface stellt eine Funktion bereit welcher drei Parameter übergeben werden. Die messageId welche die empfangene Nachricht identifiziert, der messageString welcher die empfangene Nachricht enthält und das Topic zu welchem die Nachricht gesendet wurde.

**DeviceRepresentationManager**

Anders als wie bei dem SenderManager und ReceiverManager verwaltet der DeviceRepresentationManager keine ClientInterfaces direkt. Über den DeviceRepresentationManager werden DeviceRepresentations verwaltet welche ClientInterfaces haben. Im Folgenden wird beschreiben wie ein DeviceRepresentationManager erstellt wird.

```Java
DeviceRepresentationManager devMan = new DeviceRepresentationManager("tcp://127.0.0.1:1883");
devMan.addDeviceRepresentation("representedDeviceName", options, "testopic/testDevice");
```

Zuerst muss wie auch bei den anderen Managern der Manager selbst mit der Adresse des Brokers erstellt werden. Für die Erstellung einer DeviceRepresentation muss die addDeviceRepresentation Funktion mit dem Namen der DeviceRepresentation (Am besten der Name des repräsentierten Gerätes), den Optionen für die Verbindung und dem Ziel Topic, verwendet werden. Auf die erstellte DeviceRepresentation kann über die getDeviceRepresentation Funktion des DeviceRepresentationManagers Zugegriffen werden.

```Java
devMan.getDeviceRepresentation("representedDeviceName");
```
Eine Device Repräsentation verfügt über Funktionen zum Senden und zum Empfangen von Nachrichten. Dafür können, wie beim SenderManager, Nachrichten gespeichert werden. Jede erstellte DeviceRepresentation abonniert automatisch auf das übergebene Topic mit einer Wildcard. Dadurch werden alle Nachrichten die unter diesem Topic eingehen gelesen und in ein Array geschrieben. Dadurch enthält der Array den aktuellen Status des Gerätes das er repräsentiert. Die wichtigsten der verfügbaren Funktionen können wie folgt verwendet werden.

Ein weiteres Abonnent wird durch addCallback erstellt.
```Java
devMan.getDeviceRepresentation("representedDeviceName").addCallback(topic, IMqttReceiver());
```
Auf die aktuellen Daten des Gerätes wird mit getDeviceValue zugegriffen.
```Java
devMan.getDeviceRepresentation("representedDeviceName").getDeviceValue(topic);
```
Eine Nachricht kann mit registerMessage gespeichert werden.
```Java
devMan.getDeviceRepresentation("representedDeviceName").registerMessage(name, topic, value);
```

Wenn der verwendete Smart Home Hub von JHMI unterstütz wird, gibt kann auch eine Spezialisierte DeviceRepresentation verwendet werden. Diese ermöglicht gegebenfalls das verwendet von Funktionen die nur von diesem Smart Home Hub angeboten werden. Durch das Verwenden dieser spezialisierten DeviceRepresentations ist der Aufwand beim Wechseln des Smart Home Hubs allerdings größer da die DeviceRepresentations angepasst werden müssen, wenn sie diese spezialisierten Funktionen verwenden.
