package com.stackfull.workshop.monitor.processing;

import com.stackfull.workshop.monitor.DeviceInfo;
import lombok.Builder;
import lombok.Data;
import processing.core.*;

import java.time.Instant;

@Data
@Builder
class DeviceNode {

    static final PVector CELL_SIZE = new PVector(120, 60);
    static final PVector CELL_CENTRE = CELL_SIZE.copy().div(2);

    final DeviceInfo info;
    final Instant updated;
    final PVector position;
    final PVector size;
    final PVector velocity;

    PFont font;
    private final PShape cell;

    void draw(PGraphics g) {
        g.fill(8);
        g.stroke(255);
        g.shape(cell, position.x - CELL_CENTRE.x, position.y - CELL_CENTRE.y);
        g.textFont(font);
        g.textAlign(PConstants.LEFT, PConstants.CENTER);
        g.fill(200);
        g.text(info.id, position.x + 5 - CELL_CENTRE.x, position.y - 16);
        g.text(info.deviceClass, position.x + 5 - CELL_CENTRE.x, position.y - 2);
        g.text(info.ip, position.x + 5 - CELL_CENTRE.x, position.y + 12);
        g.stroke(255.0f, 0, 0);
        PVector head = position.copy().add(velocity);
        g.line(position.x, position.y, head.x, head.y);
        PVector arrow = velocity.copy().normalize().mult(3.0f).rotate(PConstants.PI * 2.0f / 3.0f);
        g.line(head.x, head.y, head.x + arrow.x, head.y + arrow.y);
        arrow.rotate(PConstants.HALF_PI);
        g.line(head.x, head.y, head.x + arrow.x, head.y + arrow.y);

    }

    public boolean collides(DeviceNode n) {
        return false;
    }
}
