package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.commands.Negate;
import java.util.ArrayList;

/**
 * Speichert und behandelt ein Vorzeichenminus.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Negative extends Expression {

    private final Expression exp;

    public Negative(Expression exp) {
        this.exp = exp;
        prio = 6;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        exp.compile(ops, autoWindow);
        ops.add(new Negate());
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        exp.resolveNamesAndTypes(with, env);
        if (!exp.getType().isNumber()) {
            env.addError(marker, "InvalidNumber", exp.getType());
        }
        type = exp.getType();
    }

    @Override
    public String toString() {
        return "-" + exp;
    }
    @Override
    public String getLabel() {
        return createText("Negative.Label", getLabelRight(exp));
    } 
}
