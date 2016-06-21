package de.lathanda.eos.gui.flowchart;

import java.awt.Color;
import java.util.ArrayList;

import de.lathanda.eos.gui.diagram.Drawing;

/**
 * Sequenz von Kontrollflusselementen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Sequence extends Unit {
    private ArrayList<Unit> units;
    Sequence(de.lathanda.eos.interpreter.parsetree.Sequence s) {
        units = new ArrayList<>();
        if (s != null) {
            s.getInstructions().stream().forEachOrdered((n) -> {
                units.add(Unit.create(n));
            });
        }
    }
    @Override
    protected void drawUnit(Drawing d) {
        float cx = width / 2;
        d.setColor(Color.BLACK);  
        float cy = 0;
        Unit previous = null;
        for(Unit u: units) {
            if (previous == null) {
                needsIncomingArrow = u.needsIncomingArrow;
            } else if (!previous.neverEnds) {
                
                if (u.needsIncomingArrow) {
                    d.drawArrow(cx, cy, cx, cy + SPACE, 3);
                } else {
                    d.drawLine(cx, cy, cx, cy + SPACE);
                }
            }
            previous = u;
            u.draw(d);
            cy = u.getOffsetY() + u.getHeight();
        }
        if (previous != null) {
            neverEnds = previous.neverEnds;
        }
    }

    @Override
    protected void layout(Drawing d) {
        units.forEach(p -> p.layout(d));
        float maxw = 0;
        float h = 0;
        for(Unit u: units) {            
            if (maxw < u.getWidth()) {
                maxw = u.getWidth();
            }
            u.setOffsetY(h);
            h = h + u.getHeight() + SPACE;
        }
        height = h - SPACE;
        width = maxw;
        units.stream().forEach((u) -> {
            u.center(width);
        });
    }
}
