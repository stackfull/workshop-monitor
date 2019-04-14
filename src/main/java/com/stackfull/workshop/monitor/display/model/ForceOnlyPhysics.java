package com.stackfull.workshop.monitor.display.model;

import com.google.common.collect.Streams;
import processing.core.PVector;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Stream;

public abstract class ForceOnlyPhysics implements PhysicsEngine {

    float width;
    float height;
    float nodeMass = 3.0f;

    @Override
    public void bounds(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(Collection<DeviceNode> nodes, Duration timePassed) {
        float deltaT = timePassed.toMillis() / 1000.0f;
        Streams.zip(nodes.stream(), calcForces(nodes), (node, force) -> {
            node.acceleration.set(force.div(nodeMass));
            node.velocity.add(node.acceleration.copy().mult(deltaT));
            node.position.add(node.velocity.copy().mult(deltaT));
            return node;
        }).count();
    }

    abstract Stream<PVector> calcForces(Collection<DeviceNode> nodes);
}
