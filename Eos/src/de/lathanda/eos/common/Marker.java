package de.lathanda.eos.common;

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
    private ProgramNode node;
    public Marker() {
    }
    
    public Marker(ProgramNode source, ProgramNode target) {
		super();
		Marker marker = source.getMarker();
		this.beginLine = marker.beginLine;
		this.endLine = marker.endLine;
		this.beginPosition = marker.beginPosition;
		this.endPosition = marker.endPosition;
		this.node = target;
	}

	public Marker(int beginColumn, int beginLine, int endColumn, int endLine) {
		this.beginPosition = beginColumn;
		this.beginLine     = beginLine;
		this.endPosition   = endColumn;
        this.endLine       = endLine;        
    }
    public final void begin(int beginColumn, int beginLine) {
    	this.beginPosition = beginColumn;
    	this.beginLine     = beginLine;
    }
    public final void end(int endColumn, int endLine) {
    	this.endPosition = endColumn;
    	this.endLine     = endLine;
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

    public ProgramNode getNode() {
        return node;
    }

    public void setNode(ProgramNode node) {
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
