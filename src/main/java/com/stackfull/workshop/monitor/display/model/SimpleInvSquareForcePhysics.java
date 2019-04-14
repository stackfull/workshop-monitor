package com.stackfull.workshop.monitor.display.model;

import processing.core.PVector;

import java.util.Collection;
import java.util.stream.Stream;

public class SimpleInvSquareForcePhysics extends ForceOnlyPhysics {

    static final float COEFFICIENT = 100.0f;

    @Override
    Stream<PVector> calcForces(Collection<DeviceNode> nodes) {
        return nodes.stream().map(n -> {
            PVector f = nodes.stream()
                .filter(m -> m != n)
                .map(m -> {
                    PVector g = n.position.copy().sub(m.position);
                    g.normalize();
                    g.mult(COEFFICIENT / g.magSq());
                    return g;
                })
                .reduce((x, y) -> x.add(y))
                .orElseGet(PVector::new);
            return (f.mult(-1.0f));
        });
    }


}
