package com.stackfull.workshop.monitor.display.view;

import com.stackfull.workshop.monitor.display.model.DeviceNode;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

class NodeShape extends PShape {

    NodeShape(PGraphics g) {
        super(g, PConstants.GROUP);
        g.noStroke();
        g.fill(0, 200);
        PShape background = g.createShape(PConstants.RECT, 0, 0, DeviceNode.CELL_SIZE.x, DeviceNode.CELL_SIZE.y, 5);
        addChild(background);
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
        addChild(lines);
        PShape corner = g.createShape(PConstants.ARC, 5, DeviceNode.CELL_SIZE.y-5, 10, 10, PConstants.HALF_PI, PConstants.PI);
        addChild(corner);
        corner = g.createShape(PConstants.ARC, DeviceNode.CELL_SIZE.x-5, 5, 10, 10, PConstants.PI+ PConstants.HALF_PI, 2* PConstants.PI);
        addChild(corner);
    }
}
