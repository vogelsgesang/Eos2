package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.gui.diagram.LoopUnit;

/**
 * Gemeinsame Klasse aller Schleifen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public abstract class Loop  extends Node implements LoopUnit {
    protected final Sequence sequence;
    protected final Expression condition;
    public Loop(Sequence sequence, Expression condition) {
        this.sequence = sequence;
        this.condition = condition;
    }
    public Sequence getSequence() {
        return sequence;
    }
    public Expression getCondition() {
        return condition;
    }
}
