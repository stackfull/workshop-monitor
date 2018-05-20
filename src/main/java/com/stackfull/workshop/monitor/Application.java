package com.stackfull.workshop.monitor;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import processing.core.PApplet;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Application extends PApplet {

    private MqttClient client;

    @Override
    public void settings() {
        fullScreen();
        try {
            client = new MqttClient("tcp://workshopi.local.:1883",
                "workshop-monitor",
                new MemoryPersistence());
            client.connect();
            client.subscribe("#", (String topic, MqttMessage message) -> {
                println("new message: " + topic + " - " + message.toString());
            });
        } catch (MqttException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public void mouseClicked() {
        try {
            client.publish("/device/monitor/mouse", "clicked".getBytes(UTF_8), 2, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application instance = new Application();
        PApplet.runSketch(new String[]{"WorkshopMonitor"}, instance);
    }
}
