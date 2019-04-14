package com.stackfull.workshop.monitor.display.model;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

public interface PhysicsEngine {

    void update(Collection<DeviceNode> nodes, Duration timePassed);

    void bounds(int width, int height);
}
