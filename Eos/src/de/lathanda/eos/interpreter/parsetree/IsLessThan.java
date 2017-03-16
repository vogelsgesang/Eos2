package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.commands.LT;
import java.util.ArrayList;

/**
 * Speichert und behandelt einen weniger als Ausdruck.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class IsLessThan extends Expression {

    private final Expression left;
    private final Expression right;

    public IsLessThan(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        type = Type.getBoolean();
        prio = 3;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        left.compile(ops, autoWindow);
        right.compile(ops, autoWindow);
        ops.add(new LT());
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
        return "(" + left + " < " + right + ")";
    }
    @Override
    public String getLabel() {
        return createText("LessThan.Label", getLabelLeft(left), getLabelRight(right));
    }    
}
