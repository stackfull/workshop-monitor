package com.stackfull.workshop.monitor.display.model;

import com.stackfull.workshop.monitor.model.DeviceInfo;
import lombok.Builder;
import lombok.Data;
import processing.core.PVector;

import java.time.Instant;

@Data
@Builder
public class DeviceNode {

    public static final PVector CELL_SIZE = new PVector(120, 60);
    public static final PVector CELL_CENTRE = CELL_SIZE.copy().div(2);

    public DeviceInfo info;
    public final Instant updated;
    public final PVector position;
    public final PVector size;
    public final PVector velocity;

    public float left() {
        return position.x - CELL_CENTRE.x;
    }

    public float right() {
        return position.x + CELL_CENTRE.x;
    }

    public float top() {
        return position.y - CELL_CENTRE.y;
    }

    public float bottom() {
        return position.y + CELL_CENTRE.y;
    }

    public boolean collides(DeviceNode n) {
        return right() > n.left() &&
            left() < n.right() &&
            bottom() > n.top() &&
            top() < n.bottom();
    }

}
