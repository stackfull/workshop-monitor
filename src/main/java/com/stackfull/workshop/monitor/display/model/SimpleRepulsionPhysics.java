package com.stackfull.workshop.monitor.display.model;

import processing.core.PVector;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleRepulsionPhysics extends ForceOnlyPhysics {

    @Override
    public Stream<PVector> calcForces(Collection<DeviceNode> nodes) {
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
        });
    }

}
