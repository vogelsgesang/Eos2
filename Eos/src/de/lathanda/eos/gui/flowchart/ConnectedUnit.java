package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Unit;

/**
 * Basisklasse für alle Kontrollflussdiagrammteile.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public abstract class ConnectedUnit extends Unit {
    protected boolean needsIncomingArrow = true;
    protected boolean neverEnds = false;    
}
