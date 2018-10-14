package com.stackfull.workshop.monitor.processing;

import lombok.val;
import processing.core.PVector;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class InvSqPhysicsMachine {

    private float width;
    private float height;

    public List<PVector> calcAccelerations(List<DeviceNode> nodes, Instant now) {

        return nodes.stream().map(n -> {
            PVector f = new PVector();
            nodes.forEach(m -> {
                PVector g = m.position.copy().sub(n.position);
                if (m.collides(n)) {
                    g.normalize().mult(100.0f);
                } else {
                    g.normalize().mult(1.0f / g.magSq());
                }
                f.add(g);
            });
            return f;
        }).collect(Collectors.toList());
    }

    public void bounds(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private void old(List<DeviceNode> values, float timePassed) {
        int numNodes = values.size();
        if (numNodes < 2) return;
        PVector centre = new PVector(width, height).div(2);
        float[] weights = new float[numNodes];
        PVector[] positions = new PVector[numNodes];
        int idx = 0;
        for (val node: values) {
            positions[idx] = node.position.copy().sub(centre).add(DeviceNode.CELL_CENTRE);
            weights[idx] = 10000.0f;
            idx++;
        }
        // For each node, calc a weighted sum of the vectors to all other nodes.
        idx = 0;
        for (val node: values) {
            System.out.println(String.format("* %s @ %s v %s", node.info.id, node.position, node.velocity));
            PVector f = new PVector();
//            for (float x = 0-centre.x; x < centre.x; x++) {
//                PVector top = new PVector(x, centre.y);
//                PVector bottom = new PVector(x, 0-centre.y);
//                PVector g = positions[idx].copy().sub(top);
//                g.setMag(100.0f / g.magSq());
//                f.add(g);
//                g = positions[idx].copy().sub(bottom);
//                g.setMag(100.0f / g.magSq());
//                f.add(g);
//            }
            //PVector f = positions[idx].copy().mult(-1);
            //f.setMag(1.0f * f.magSq());
            System.out.println(String.format(" F = %s", f));
            //g.line(centre.x, centre.y, centre.x + f.x, centre.y + f.y);
            for (int other = 0; other < numNodes; other++) {
                if (other == idx) continue;
                PVector g = positions[idx].copy().sub(positions[other]);
                g.setMag(weights[other] / g.magSq());
                f.add(g);
            }
            System.out.println(String.format(" F = %s", f));
            PVector a = f.div(3.0f);
            System.out.println(String.format(" a = %s", a));
            node.velocity.add(a.mult(timePassed));
            System.out.println(String.format(" v = %s", node.velocity));
            node.position.add(node.velocity.copy().mult(timePassed));
            System.out.println(String.format(" new = %s", node.position));
            idx++;
        }

    }
}
