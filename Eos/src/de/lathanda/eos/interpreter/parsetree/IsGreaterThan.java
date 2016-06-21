package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.commands.GT;
import java.util.ArrayList;

/**
 * Speichert und behandelt einen gröser als Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class IsGreaterThan extends Expression {

    private final Expression left;
    private final Expression right;

    public IsGreaterThan(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        type = Type.getBoolean();
        prio = 3;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        left.compile(ops, autoWindow);
        right.compile(ops, autoWindow);
        ops.add(new GT());
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        left.resolveNamesAndTypes(with, env);
        right.resolveNamesAndTypes(with, env);
        if (!left.getType().isNumber() || !right.getType().isNumber()) {
            env.addError(marker, "CompareType", left.getType(), right.getType());
        }
    }

    @Override
    public String toString() {
        return "(" + left + " > " + right + ")";
    }
    @Override
    public String getLabel() {
        return createText("GreaterThan.Label", getLabelLeft(left), getLabelRight(right));
    }
}
