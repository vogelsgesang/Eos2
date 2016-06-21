package de.lathanda.eos.interpreter;

import de.lathanda.eos.interpreter.javacc.Token;

/**
 * Bereich im Quellcode. Wird verwendet um einen Befehl
 * im Quellcode zu lokalisieren. 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.6
 */

public class Marker {
    private int beginLine = Integer.MAX_VALUE;
    private int endLine = Integer.MIN_VALUE;
    private int beginPosition = Integer.MAX_VALUE;
    private int endPosition = Integer.MIN_VALUE;
    private Node node;
    public Marker() {
    }
    
    public Marker(Node source, Node target) {
		super();
		Marker marker = source.getMarker();
		this.beginLine = marker.beginLine;
		this.endLine = marker.endLine;
		this.beginPosition = marker.beginPosition;
		this.endPosition = marker.endPosition;
		this.node = target;
	}

	public Marker(Token token) {
        beginPosition = token.beginColumn;
        beginLine     = token.beginLine;
        endPosition   = token.endColumn;
        endLine       = token.endLine;        
    }
    public final void begin(Token t) {
        beginPosition = t.beginColumn;
        beginLine   = t.beginLine;
    }
    public final void end(Token t) {
        endPosition   = t.endColumn;
        endLine     = t.endLine;
    }
    
    public int getBeginLine() {
        return beginLine;
    }
    public int getEndLine() {
        return endLine;
    }
    public int getBeginPosition() {
        return beginPosition;
    }
    public int getEndPosition() {
        return endPosition;
    }
    public int getLength() {
        return endPosition - beginPosition + 1;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Marker{line = " + beginLine + " - " + endLine + " position = " + beginPosition + " - " + endPosition + '}';
    }
	public void extend(Marker marker) {
        beginPosition = Math.min(marker.beginPosition, beginPosition);
        beginLine     = Math.min(marker.beginLine, beginLine);
        endPosition   = Math.max(marker.endPosition, endPosition);
        endLine       = Math.max(marker.endLine, endLine); 
		
	}
}
