package com.stackfull.workshop.monitor;

import com.stackfull.workshop.monitor.display.model.*;
import com.stackfull.workshop.monitor.display.view.BasicNetworkView;
import com.stackfull.workshop.monitor.mock.MockDeviceInfoSource;
import com.stackfull.workshop.monitor.model.DeviceInfoSource;
import processing.core.PApplet;
import processing.core.PFont;

import java.time.Clock;

public class ProcessingApplet extends PApplet {

    private final DeviceInfoSource infoSource;
    private final Network model;
    private BasicNetworkView screen;


    public static void main(String[] args) {
        System.out.println("Starting...");
        //DeviceInfoSource infoSource = new MqttDeviceInfoSource();
        DeviceInfoSource infoSource = new MockDeviceInfoSource();
        //PhysicsEngine physics = new SimpleInvSquareForcePhysics();
        PhysicsEngine physics = new JustVelocityPhysics();
        Clock clock = Clock.systemDefaultZone();
        Network model = new Network(physics, clock, 320, 280);
        ProcessingApplet instance = new ProcessingApplet(infoSource, model);
        ProcessingApplet.runSketch(new String[]{"WorkshopMonitor"}, instance);
    }

    public ProcessingApplet(DeviceInfoSource infoSource, Network model) {
        this.infoSource = infoSource;
        this.model = model;
    }


    @Override
    public void settings() {
        if (displayHeight > 280) {
            size(320, 280);
        } else {
            fullScreen();
        }
        model.bounds(width, height);
    }

    @Override
    public void setup() {
        stroke(255);
        smooth();
        String[] FONTS = new String[] { "Roboto-Regular", "PTSans-Regular", "Droid Sans Fallback", "FreeSans" };
        PFont font = null;
        for( String f: FONTS) {
            font = createFont(f, 12);
            if (font != null) break;
        }
        assert font != null;

        screen = new BasicNetworkView(getGraphics(), font, model);

        infoSource.subscribe(model::update);
    }

    @Override
    public void draw() {
        background(0);

        model.tick();
        screen.draw();

    }

}
