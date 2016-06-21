package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Node;
import java.util.ArrayList;

/**
 * Speichert und behandelt einen <b>für</b> Bereich.
 * Jede Ausdruck innerhalb des Bereichs wird den Ausdruck als Prefix verwenden,
 * wenn der Ausdruck ohne illegal wäre.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class With extends Node {
    private final Expression with;
    private final Sequence sequence;
    public With(Expression with, Sequence sequence) {
        this.with = with;
        this.sequence = sequence;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        sequence.compile(ops, autoWindow);
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        sequence.resolveNamesAndTypes(this.with, env);
    }

    @Override
    public String toString() {
        return "with " + with + "\n" + sequence + "endwith";
    }  

    @Override
    public String getLabel() {
        return null; //no label
    }
}
