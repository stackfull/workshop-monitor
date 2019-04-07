package com.stackfull.workshop.monitor.display.model;

import processing.core.PVector;

import java.time.Instant;
import java.util.List;

public abstract class PhysicsEngine {

    float width;
    float height;

    abstract public List<PVector> calcAccelerations(List<DeviceNode> nodes, Instant now);

    void bounds(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
