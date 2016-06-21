package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.commands.DivideD;
import de.lathanda.eos.interpreter.commands.DivideI;

/**
 * Speichert und behandelt einen Divisions-Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Division extends Expression {

    private final Expression left;
    private final Expression right;

    public Division(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        prio = 5;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        left.compile(ops, autoWindow);
        right.compile(ops, autoWindow);
        if (type.isInteger()) {
        	ops.add(new DivideI());
        } else {
        	ops.add(new DivideD());
        }
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        left.resolveNamesAndTypes(with, env);
        right.resolveNamesAndTypes(with, env);
        if (!left.getType().isNumber() || !right.getType().isNumber()) {
            env.addError(marker, "NumberType", left.getType(), right.getType());
        }
        type = left.getType().mergeTypes(right.getType());
    }

    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }
    @Override
    public String getLabel() {
        return createText("Division.Label", getLabelLeft(left), getLabelRight(right));
    }

}
