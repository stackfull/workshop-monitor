package com.stackfull.workshop.monitor.processing;

import com.stackfull.workshop.monitor.DeviceInfo;
import processing.core.*;

import java.time.Clock;

public class DeviceNodeFactory {

    private final PFont font;
    private final PShape cell;
    private final Clock clock = Clock.systemDefaultZone();

    public DeviceNodeFactory(PGraphics g, PFont font) {
        this.font = font;
        cell = g.createShape(PConstants.GROUP);
        g.noStroke();
        g.fill(0, 200);
        PShape background = g.createShape(PConstants.RECT, 0, 0, DeviceNode.CELL_SIZE.x, DeviceNode.CELL_SIZE.y, 5);
        cell.addChild(background);
        g.noFill();
        g.strokeWeight(2);
        g.stroke(100);
        PShape lines = g.createShape();
        lines.beginShape(PConstants.LINES);
        lines.vertex(0, DeviceNode.CELL_SIZE.y-10);
        lines.vertex(0, DeviceNode.CELL_SIZE.y-6);
        lines.vertex(6, DeviceNode.CELL_SIZE.y);
        lines.vertex(10, DeviceNode.CELL_SIZE.y);
        lines.vertex(DeviceNode.CELL_SIZE.x-10, 0);
        lines.vertex(DeviceNode.CELL_SIZE.x-6, 0);
        lines.vertex(DeviceNode.CELL_SIZE.x, 6);
        lines.vertex(DeviceNode.CELL_SIZE.x, 10);
        lines.endShape();
        cell.addChild(lines);
        PShape corner = g.createShape(PConstants.ARC, 5, DeviceNode.CELL_SIZE.y-5, 10, 10, PConstants.HALF_PI, PConstants.PI);
        cell.addChild(corner);
        corner = g.createShape(PConstants.ARC, DeviceNode.CELL_SIZE.x-5, 5, 10, 10, PConstants.PI+ PConstants.HALF_PI, 2* PConstants.PI);
        cell.addChild(corner);
    }

    public DeviceNode create(DeviceInfo info, float x, float y) {
        return DeviceNode.builder()
            .info(info)
            .updated(clock.instant())
            .position(new PVector(x, y))
            .size(DeviceNode.CELL_SIZE.copy())
            .velocity(new PVector())
            .font(font)
            .cell(cell)
            .build();
    }
}
