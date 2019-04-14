package com.stackfull.workshop.monitor.display.model;

import processing.core.PVector;

import java.time.Duration;
import java.util.Collection;

public class JustVelocityPhysics implements PhysicsEngine {

    static final float REPULSIVE_V = 100.0f;
    private int width = Integer.MAX_VALUE;
    private int height = Integer.MAX_VALUE;


    @Override
    public void update(Collection<DeviceNode> nodes, Duration timePassed) {
        float deltaT = timePassed.toMillis() / 1000.0f;
        nodes.forEach(n -> {
            PVector v = calcVelocity(nodes, n);
            n.velocity.set(v);
            n.position.add(v.copy().mult(deltaT));
            clip(n);
        });
    }

    private void clip(DeviceNode n) {
        if (n.left() <= 0) {
            n.left(0);
        } else if (n.right() >= width) {
            n.right(width);
        }
        if (n.top() <= 0) {
            n.top(0);
        } else if (n.bottom() >= height) {
            n.bottom(height);
        }
    }

    private PVector calcVelocity(Collection<DeviceNode> nodes, DeviceNode n) {
        return nodes.stream()
            .filter(m -> m != n)
            .map(m -> {
                if (n.collides(m)) {
                    PVector g = n.position.copy().sub(m.position);
                    g.normalize();
                    g.mult(REPULSIVE_V);
                    return g;
                } else {
                    return new PVector();
                }
            })
            .reduce((x, y) -> x.add(y))
            .orElseGet(PVector::new);
    }

    @Override
    public void bounds(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
