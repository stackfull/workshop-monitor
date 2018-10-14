package com.stackfull.workshop.monitor;

import com.stackfull.workshop.monitor.mock.MockDeviceInfoSource;
import com.stackfull.workshop.monitor.processing.Applet;
import com.stackfull.workshop.monitor.processing.PhysicsMachine;

public class Application {

    public static void main(String[] args) {
        System.out.println("Starting...");
        //DeviceInfoSource infoSource = new MqttDeviceInfoSource();
        DeviceInfoSource infoSource = new MockDeviceInfoSource();
        PhysicsMachine physicsMachine = new PhysicsMachine();
        Applet instance = new Applet(infoSource, physicsMachine);
        Applet.runSketch(new String[]{"WorkshopMonitor"}, instance);
    }

}
