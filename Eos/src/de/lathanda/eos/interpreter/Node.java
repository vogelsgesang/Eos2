package de.lathanda.eos.interpreter;

import de.lathanda.eos.common.Marker;
import de.lathanda.eos.gui.diagram.ProgramNode;
import de.lathanda.eos.interpreter.parsetree.Expression;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Basisklasse aller Syntaxbaumknoten
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.2
 */
public abstract class Node implements ProgramNode {
    protected static final ResourceBundle LABEL = ResourceBundle.getBundle("text.label");
    protected Type type = Type.getVoid();
    protected Marker marker;
    public abstract void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception;
    public abstract void resolveNamesAndTypes(Expression with, Environment env);
    public Node() {
    	marker = new Marker();
    }
    public final Type getType() {
        return type;
    }

    public final Marker getMarker() {
        return marker;
    }

    public final void setMarker(Marker cr) {
        marker = cr;
        marker.setNode(this);
    }
    public final void sameMarker(Node node) {
    	marker.extend(node.marker);
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
