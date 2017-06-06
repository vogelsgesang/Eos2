package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.interpreter.Command;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Basisklasse aller Syntaxbaumknoten
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.2
 */
public abstract class Node extends MarkedNode {
    protected static final ResourceBundle LABEL = ResourceBundle.getBundle("text.label");
    public abstract void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception;
    public abstract void resolveNamesAndTypes(Expression with, Environment env);
    public Node() {
    	marker = new Marker();
    }

    /**
     * Erzeugt eine Textlesbare Version dieses Knotens für Struktogramme.
     * @return
     */
    public abstract String getLabel();
    protected final String createText(String id, Object... args) {
        return MessageFormat.format(LABEL.getString(id), args);
    }
}
