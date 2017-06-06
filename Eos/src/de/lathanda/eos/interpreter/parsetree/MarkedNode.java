package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.common.interpreter.ProgramNode;

public abstract class MarkedNode implements ProgramNode {
	protected Marker marker;
    protected Type type = Type.getVoid();

	public MarkedNode() {
		super();
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
    public final Type getType() {
        return type;
    }
}