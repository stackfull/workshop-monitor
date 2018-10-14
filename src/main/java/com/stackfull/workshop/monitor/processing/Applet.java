package com.stackfull.workshop.monitor.processing;

import com.google.common.collect.Streams;
import com.stackfull.workshop.monitor.DeviceInfo;
import com.stackfull.workshop.monitor.DeviceInfoSource;
import lombok.var;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Applet extends PApplet {

    private final DeviceInfoSource infoSource;
    private PFont font;
    private ConcurrentMap<String, DeviceNode> nodes = new ConcurrentHashMap<>();
    private Clock clock = Clock.systemDefaultZone();
    private Instant last = null;
    private DeviceNodeFactory deviceNodeFactory;
    private PhysicsMachine physicsMachine;


    public Applet(DeviceInfoSource infoSource, PhysicsMachine physicsMachine) {
        this.infoSource = infoSource;
        this.physicsMachine = physicsMachine;
    }

    private DeviceNode newNode(DeviceInfo info) {
        float x = map(random(1), 0, 1, DeviceNode.CELL_CENTRE.x, width - DeviceNode.CELL_CENTRE.x);
        float y = map(random(1), 0, 1, DeviceNode.CELL_CENTRE.y, height - DeviceNode.CELL_CENTRE.y);
        return deviceNodeFactory.create(info, x, y);
    }


    @Override
    public void settings() {
        if (displayHeight > 280) {
            size(320, 280);
        } else {
            fullScreen();
        }
        physicsMachine.bounds(width, height);
    }

    @Override
    public void setup() {
        stroke(255);
        smooth();
        String[] FONTS = new String[] { "Roboto-Regular", "PTSans-Regular", "Droid Sans Fallback", "FreeSans" };
        for( String f: FONTS) {
            font = createFont(f, 12);
            if (font != null) break;
        }

        deviceNodeFactory = new DeviceNodeFactory(this.getGraphics(), font);

        infoSource.subscribe(d -> {
            System.out.println(String.format("New node: %s", nodes.toString()));
            nodes.computeIfAbsent(d.deviceClass + d.id, (String x) -> newNode(d));
        });
    }

    @Override
    public void draw() {
        background(0);

        // Need a stable order
        ArrayList<DeviceNode> nodeList = new ArrayList<>(this.nodes.values());
        Instant now = clock.instant();
        if (last != null) {
            float timePassed = Duration.between(last, now).toMillis() / 1000.0f;
            var accelerations = physicsMachine.calcAccelerations(nodeList, now);
            var velocities = updateVelocities(nodeList, accelerations, timePassed);
            updatePositions(nodeList, timePassed);
        }
        last = now;
        nodeList.forEach(node -> node.draw(g));
    }

    private List<DeviceNode> updateVelocities(List<DeviceNode> nodes, List<PVector> accelerations, float timePassed) {
        return Streams.zip(nodes.stream(), accelerations.stream(), (node, a) -> {
            float mass = 3.0f;
            node.velocity.add(a.copy().mult(mass));
            return node;
        }).collect(Collectors.toList());
    }

    private void updatePositions(Collection<DeviceNode> nodes, float timePassed) {
        nodes.forEach(node -> {
            node.position.add(node.velocity.copy().mult(timePassed));
        });
    }


}
