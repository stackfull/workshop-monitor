package com.stackfull.workshop.monitor.display.view;

import com.stackfull.workshop.monitor.display.model.DeviceNode;
import com.stackfull.workshop.monitor.display.model.Network;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Manages the rendering of the monitor display.
 */
public class BasicNetworkView {


    private final PGraphics graphics;
    private final PFont font;
    private final NodeShape nodeShape;
    private final Network model;

    public BasicNetworkView(PGraphics graphics, PFont font, Network model) {

        this.graphics = graphics;
        this.font = font;
        this.nodeShape = new NodeShape(graphics);
        this.model = model;
    }

    public void draw() {
        model.nodes().forEach(node -> {
            drawNode(node);
            drawVector(node);
        });
    }

    private void drawNode(DeviceNode node) {
        graphics.fill(8);
        graphics.stroke(255);
        graphics.shape(nodeShape, node.left(), node.top());
        graphics.textFont(font);
        graphics.textAlign(PConstants.LEFT, PConstants.CENTER);
        graphics.fill(200);
        graphics.text(node.info.id, node.left() + 5, node.position.y - 16);
        graphics.text(node.info.deviceClass, node.left() + 5, node.position.y - 2);
        graphics.text(node.info.firmware, node.position.x + 5, node.position.y - 2);
        graphics.text(node.info.ip, node.left() + 5, node.position.y + 12);
    }

    private void drawVector(DeviceNode node) {
        graphics.stroke(255.0f, 0, 0);
        PVector head = node.position.copy().add(node.velocity);
        graphics.line(node.position.x, node.position.y, head.x, head.y);
        PVector arrow = node.velocity.copy().normalize().mult(3.0f).rotate(PConstants.PI * 2.0f / 3.0f);
        graphics.line(head.x, head.y, head.x + arrow.x, head.y + arrow.y);
        arrow.rotate(PConstants.HALF_PI);
        graphics.line(head.x, head.y, head.x + arrow.x, head.y + arrow.y);
    }
}
