package com.stackfull.workshop.monitor.display.model;

import processing.core.PVector;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleForcePhysics extends PhysicsEngine {

    @Override
    public List<PVector> calcAccelerations(List<DeviceNode> nodes, Instant now) {
        return nodes.stream().map(n -> {
            PVector f = new PVector();
            nodes.forEach(m -> {
                if (m == n) return;
                PVector g = n.position.copy().sub(m.position);
                if (m.collides(n)) {
                    g.normalize().mult(10.0f);
                } else {
                    g.normalize();
                    float mag = g.mag();
                    g.mult(1.0f / (mag * mag ));
                }
                f.add(g);
            });
            PVector friction = f.mult(-1.0f);
            return f.add(friction);
        }).collect(Collectors.toList());
    }
}
