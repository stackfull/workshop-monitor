package com.stackfull.workshop.monitor.mqtt;

import com.stackfull.workshop.monitor.model.DeviceInfo;
import com.stackfull.workshop.monitor.model.DeviceInfoSource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.util.Strings;
import processing.data.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttDeviceInfoSource implements DeviceInfoSource {

    public static final Pattern TOPIC_PATTERN =
        Pattern.compile("^/device-class/(?<classID>[^/]+)(?:/device/(?<deviceID>[^/]+))?(?<remainder>.*)");

    private final MqttClient client;
    private List<Consumer<DeviceInfo>> consumers = new ArrayList<>();

    public MqttDeviceInfoSource() {
        try {
            client = new MqttClient("tcp://workshopi.local.:1883",
                "workshop-monitor",
                new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(Consumer<DeviceInfo> c) {
        consumers.add(c);
        try {
            client.subscribe("/device-class/#", this::messageArrived);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unsubscribe(Consumer<DeviceInfo> c) {
        consumers.remove(c);
    }


    private void messageArrived(String topic, MqttMessage message) {
        System.out.println("new message: " + topic + " - " + message.toString());
        Matcher match = TOPIC_PATTERN.matcher(topic);
        if (!match.matches()) {
            System.out.println("ignoring topic: " + topic);
            return;
        }

        String classID = match.group("classID");
        String deviceID = match.group("deviceID");
        String subTopic = match.group("remainder");

        if (deviceID == null) {
            System.out.println(String.format("Message for class '%s' at '%s'", classID, subTopic));
            return;
        }

        String identifier = String.format("/device-class/%s/device/%s", classID, deviceID);
        System.out.println(String.format("Message for device '%s' at '%s'", identifier, subTopic));
        if (Strings.isEmpty(subTopic)) {
            try {
                JSONObject status = new JSONObject(new StringReader(message.toString()));
                DeviceInfo info = DeviceInfo.fromJSON(status);
                consumers.forEach(c -> c.accept(info));
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
