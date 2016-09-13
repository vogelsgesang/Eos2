package de.lathanda.eos.gui.flowchart;
import java.util.ArrayList;

import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.gui.diagram.Drawing;

/**
 * Sequenz von Kontrollflusselementen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Sequence extends ConnectedUnit {
    private ArrayList<ConnectedUnit> units;
    Sequence(ProgramSequence programSequence) {
        units = new ArrayList<>();
        if (programSequence != null) {
            programSequence.getInstructions().stream().forEachOrdered((n) -> {
                units.add(Toolkit.create(n));
            });
        }
    }
    @Override
	public void drawUnit(Drawing d) {
        float cx = width / 2;
        float cy = 0;
        ConnectedUnit previous = null;
        for(ConnectedUnit u: units) {
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
	public void layoutUnit(Drawing d) {
        units.forEach(p -> p.layout(d));
        float maxw = 0;
        float h = 0;
        for(ConnectedUnit u: units) {            
            if (maxw < u.getWidth()) {
                maxw = u.getWidth();
            }
            u.setOffsetY(h);
            h = h + u.getHeight() + SPACE;
        }
        height = h - SPACE;
        width = maxw;
        units.stream().forEach((u) -> {
            u.centerX(width);
        });
    }
}
