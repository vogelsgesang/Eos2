package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.commands.Or;
import java.util.ArrayList;

/**
 * Speichert und behandelt einen logischen Oder-Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class LogicalOr extends Expression {

    private final Expression left;
    private final Expression right;

    public LogicalOr(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        type = Type.getBoolean();
        prio = 0;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        left.compile(ops, autoWindow);
        right.compile(ops, autoWindow);
        ops.add(new Or());
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        left.resolveNamesAndTypes(with, env);
        right.resolveNamesAndTypes(with, env);
        if (!left.getType().isBoolean() || !right.getType().isBoolean()) {
            env.addError(marker, "BooleanType", left.getType(), right.getType());
        }
    }

    @Override
    public String toString() {
        return "(" + left + " or " + right + ")";
    }
    @Override
    public String getLabel() {
        return createText("Or.Label", getLabelLeft(left), getLabelRight(right));
    }     
}
