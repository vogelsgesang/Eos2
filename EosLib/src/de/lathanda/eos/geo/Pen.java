package de.lathanda.eos.geo;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.layout.Transform;
import java.awt.Color;
import java.util.LinkedList;

/**
 * Hilfsklasse für den Plotter (Turtle).
 * Sie repräsentiert den Zeichenstift.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Pen extends FilledFigure {
	private final static LinkedList<Point> PEN;

	static {
		PEN = new LinkedList<>();
		PEN.add(new Point(-5, 10));
		PEN.add(new Point(20, 0));
		PEN.add(new Point(-5, -10));
	}

	public Pen() {
		line.setColor(Color.BLACK);
		fill.setColor(new Color(255,0,0,128));
		fill.setFillStyle(FillStyle.FILLED);
	}

	@Override
	protected void drawObject(Picture p) {
		p.drawPolygon(PEN);
	}
	@Override
	protected void scaleInternal(double factor) {
		//pen is no real figure, so scaling it doesn't do anything 
	}

	@Override
	protected void fireLayoutChanged() {
		// PEN is not considered for layouting
		// so we do NOT call super
		fireDataChanged();
	}

	@Override
	protected BalancePoint getBalancePoint() {
		return new BalancePoint(0, getX(), getY());
	}

	public Point getPoint() {
		return new Point(getX(), getY());
	}

	@Override
	protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
		Transform t = base.transform(own);
		return new BoundingBox(t.getdx(), t.getdx(), t.getdy(), t.getdy());
	}
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	//helper for Plotter, internal informations are secret
    	//plotter will read fields external
	} 	
}
