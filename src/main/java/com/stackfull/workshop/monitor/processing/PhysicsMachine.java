package com.stackfull.workshop.monitor.processing;

import processing.core.PVector;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhysicsMachine {

    private float width;
    private float height;

    public List<PVector> calcAccelerations(List<DeviceNode> nodes, Instant now) {
        return nodes.stream().map(n -> {
            PVector f = new PVector();
            nodes.forEach(m -> {
                if (m == n) return;
                PVector g = m.position.copy().sub(n.position);
                if (m.collides(n)) {
                    g.normalize().mult(100.0f);
                } else {
                    g.normalize().mult(1.0f / g.magSq());
                }
                f.add(g);
            });
            return f;
        }).collect(Collectors.toList());
    }

    public void bounds(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
