package com.stackfull.workshop.monitor.display.model;

import com.stackfull.workshop.monitor.model.DeviceInfo;
import processing.core.PVector;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Network {

    public final Clock clock;
    private final PhysicsEngine physics;
    private final ConcurrentMap<String, DeviceNode> nodes;
    private final Random internalRandom;
    private Instant last;
    private float width;
    private float height;

    public Network(PhysicsEngine physics, Clock clock, int width, int height) {
        this.physics = physics;
        this.clock = clock;
        this.width = width;
        this.height = height;
        this.nodes = new ConcurrentHashMap<>();
        this.internalRandom = new Random();
    }

    public void bounds(int width, int height) {
        this.width = width;
        this.height = height;
        physics.bounds(width, height);
    }

    public void update(DeviceInfo d) {
        String key = d.deviceClass + d.id;
        DeviceNode n = nodes.computeIfAbsent(key, k -> newNode(d));
        n.update(d, clock.instant());
        System.out.println(String.format("New node: %s", nodes.toString()));
    }

    public void tick() {
        Instant now = clock.instant();
        this.nodes.entrySet().forEach(e -> {
            if (Duration.between(e.getValue().updated, now).toMillis() > 5000.0 ) {
                this.nodes.remove(e.getKey());
            }
        });
        if (last != null) {
            physics.update(this.nodes.values(), Duration.between(last, now));
        }
        last = now;
    }

    private DeviceNode newNode(DeviceInfo info) {
        float x = random(DeviceNode.CELL_CENTRE.x, width - DeviceNode.CELL_CENTRE.x);
        float y = random(DeviceNode.CELL_CENTRE.y, height - DeviceNode.CELL_CENTRE.y);
        return DeviceNode.builder()
            .info(info)
            .updated(clock.instant())
            .position(new PVector(x, y))
            .build();
    }

    private float random(float start2, float stop2) {
        return start2 + (stop2 - start2) * internalRandom.nextFloat();
    }


    public Iterable<DeviceNode> nodes() {
        return nodes.values();
    }
}
