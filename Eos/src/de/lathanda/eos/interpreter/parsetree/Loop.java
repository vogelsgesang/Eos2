package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Node;

/**
 * Gemeinsame Klasse aller Schleifen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public abstract class Loop  extends Node {
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
    public abstract boolean isPre();
}
