package com.stackfull.workshop.monitor.display.model;

import processing.core.PVector;

import java.util.Collection;
import java.util.stream.Stream;

public class SimpleBinaryForcePhysics extends ForceOnlyPhysics {

    static final float REPULSIVE_FORCE = 100.0f;
    static final float ATTRACTIVE_FORCE = 10.0f;

    @Override
    Stream<PVector> calcForces(Collection<DeviceNode> nodes) {
        return nodes.stream().map(n -> {
            return nodes.stream()
                .filter(m -> m != n)
                .map(m -> {
                    PVector g = n.position.copy().sub(m.position);
                    g.normalize();
                    if (n.collides(m)) {
                        g.mult(REPULSIVE_FORCE);
                    } else {
                        g.mult(-ATTRACTIVE_FORCE);
                    }
                    return g;
                })
                .reduce((x, y) -> x.add(y))
                .orElseGet(PVector::new);
        });
    }


}
