package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Node;

/**
 * Gemeinsame abstrakte Klasse aller Ausdrücke.
 * Die Klasse stellt eine Reihe von Hilfsfunktionen zur Verfügung,
 * die in der Grammatik verwendet werden.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public abstract class Expression extends Node {
    protected int prio = 0;
    public final Expression multiply(Expression b) {
        return new Product(this, b);
    }
    public final Expression divide(Expression b) {
        return new Division(this, b);
    }
    public final Expression plus(Expression b) {
        return new Sum(this, b);
    }
    public final Expression minus(Expression b) {
        return new Difference(this, b);
    }
    public final Expression pow(Expression b) {
    	return new Power(this, b);
    }
    public final Expression not() {
        return new LogicalNot(this);
    }
    public final Expression negate() {
        return new Negative(this);
    }
    public final Expression or(Expression b) {
        return new LogicalOr(this, b);
    }
    public final Expression and(Expression b) {
        return new LogicalAnd(this, b);
    }
    public final Expression equals(Expression b) {
        return new IsEqual(this, b);
    }
    public final Expression unequals(Expression b) {
        return new IsNotEqual(this, b);
    }
    public final Expression less(Expression b) {
        return new IsLessThan(this, b);
    }
    public final Expression lessEquals(Expression b) {
        return new IsLessEquals(this, b);
    }
    public final Expression greater(Expression b) {
        return new IsGreaterThan(this, b);
    }
    public final Expression greaterEquals(Expression b) {
        return new IsGreaterEquals(this, b);
    }
    protected String getLabelLeft(Expression e) {
        if (prio > e.prio) {
            return "(" + e.getLabel() + ")";
        } else {
            return e.getLabel();
        }
    }
    protected String getLabelRight(Expression e) {
        if (prio >= e.prio) {
            return "(" + e.getLabel() + ")";
        } else {
            return e.getLabel();
        }
    }
}
