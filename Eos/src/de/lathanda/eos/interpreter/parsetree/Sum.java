package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.commands.AddD;
import de.lathanda.eos.interpreter.commands.AddI;
import de.lathanda.eos.interpreter.commands.Concatenate;

/**
 * Speichert und behandelt eine Plus Operation.
 * Abhängig von den Typen der beiden Operanden wird dies entweder als 
 * Plusrechnen oder als Verbinden von Zeichenketten ausgewertet.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Sum extends Expression {
    private final Expression left;
    private final Expression right;
    private boolean isConcatenate = false;
    public Sum(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        prio = 4;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        left.compile(ops, autoWindow);
        right.compile(ops, autoWindow);
        if (isConcatenate) {
            ops.add(new Concatenate());   
        } else if (type.isInteger()) {
        	ops.add(new AddI());
        } else {
        	ops.add(new AddD());
        }
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        left.resolveNamesAndTypes(with, env);
        right.resolveNamesAndTypes(with, env);
        if (!left.getType().isNumber() || !right.getType().isNumber()) {
            isConcatenate = true;
            type = Type.getString();
        } else {
            isConcatenate = false;
            type = left.getType().mergeTypes(right.getType());
        }
    } 
    @Override
    public String toString() {
        return "(" + left + "+" + right + ")";
    } 
    @Override
    public String getLabel() {
        return createText("Sum.Label", getLabelLeft(left), getLabelRight(right));
    }    
}
