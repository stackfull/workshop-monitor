package com.stackfull.workshop.monitor.display.model;

import com.google.common.collect.Streams;
import com.stackfull.workshop.monitor.model.DeviceInfo;
import lombok.var;
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

    private final Clock clock;
    private final PhysicsEngine physics;
    private final ConcurrentMap<String, DeviceNode> nodes;
    private final Random internalRandom;
    private Instant last;

    public Network(PhysicsEngine physics, Clock clock, int width, int height) {
        this.physics = physics;
        this.clock = clock;
        this.nodes = new ConcurrentHashMap<>();
        this.internalRandom = new Random();
    }

    public void bounds(int width, int height) {
        physics.bounds(width, height);
    }

    public void update(DeviceInfo d) {
        String key = d.deviceClass + d.id;
        DeviceNode n = nodes.computeIfAbsent(key, k -> newNode(d));
        n.info = d;
        System.out.println(String.format("New node: %s", nodes.toString()));
    }

    public void tick() {
        // Need a stable order
        ArrayList<DeviceNode> nodeList = new ArrayList<>(this.nodes.values());
        Instant now = clock.instant();
        if (last != null) {
            float timePassed = Duration.between(last, now).toMillis() / 1000.0f;
            var accelerations = physics.calcAccelerations(nodeList, now);
            var velocities = updateVelocities(nodeList, accelerations, timePassed);
            updatePositions(nodeList, timePassed);
        }
        last = now;
    }

    private DeviceNode newNode(DeviceInfo info) {
        float x = random(DeviceNode.CELL_CENTRE.x, physics.width - DeviceNode.CELL_CENTRE.x);
        float y = random(DeviceNode.CELL_CENTRE.y, physics.height - DeviceNode.CELL_CENTRE.y);
        return DeviceNode.builder()
            .info(info)
            .updated(clock.instant())
            .position(new PVector(x, y))
            .size(DeviceNode.CELL_SIZE.copy())
            .velocity(new PVector())
            .build();
    }

    private float random(float start2, float stop2) {
        return start2 + (stop2 - start2) * internalRandom.nextFloat();
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


    public Iterable<DeviceNode> nodes() {
        return nodes.values();
    }
}
