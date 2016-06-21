package de.lathanda.eos.geo;

import java.awt.Color;
import java.util.LinkedList;

import de.lathanda.eos.base.FillDescriptor;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineDescriptor;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.math.Point;

/**
 * Der Plotter (Turtle) dient dazu Polygone zu zeichnen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Plotter extends Group {

	Pen pen;

	private boolean isPlotting = true;
	private Polygon polygon;

	public Plotter() {
		pen = new Pen();
		addFigure(pen);
		startPlot();
	}

	@Override
	protected void drawObject(Picture p) {
		super.drawObject(p);
		pen.draw(p);
	}

	public void turnLeft(double angle) {
		pen.rotate(angle);
	}

	public void turnRight(double angle) {
		pen.rotate(-angle);
	}

	public void turnTo(double angle) {
		pen.setRotation(angle);
	}

	private synchronized void appendPosition() {
		if (isPlotting) {
			polygon.addPoint(pen.getPoint());
		}
	}

	private void finishPlot() {
		isPlotting = false;
		if (!polygon.isValid()) {
			this.removeFigure(polygon);
		}
		fireDataChanged();
	}

	private void startPlot() {
		if (polygon == null) {
			polygon = new Polygon();
			polygon.setLineColor(Color.BLACK);
			polygon.setFillStyle(FillStyle.TRANSPARENT);
		} else {
			Polygon newPolygon = new Polygon();
			newPolygon.fill = new FillDescriptor(polygon.fill);
			newPolygon.line = new LineDescriptor(polygon.line);
			polygon = newPolygon;
		}
		addFigure(polygon);
		isPlotting = true;
		appendPosition();
	}
	public double getPenX() {
		return pen.getAbsolutePosition().getX();
	}
	public double getPenY() {
		return pen.getAbsolutePosition().getY();
	}
	public void setPenX(double x) {
		Point loc = pen.getRelativePosition(new Point(x, pen.getAbsolutePosition().getY()));
		pen.moveTo(loc.getX(), loc.getY());
		appendPosition();
	}
	public void setPenY(double y) {
		Point loc = pen.getRelativePosition(new Point(pen.getAbsolutePosition().getX(), y));
		pen.moveTo(loc.getX(), loc.getY());
		appendPosition();
	}

	public void movePenTo(double x, double y) {
		Point loc = pen.getRelativePosition(new Point(x, y));
		pen.moveTo(loc.getX(), loc.getY());
		appendPosition();
	}

	public double getAngle() {
		return pen.getRotation();
	}

	public void setAngle(double angle) {
		pen.setRotation(angle);
		fireDataChanged();
	}

	public void moveForward(double length) {
		pen.move(Math.cos(pen.getRotationInternal()) * length, Math.sin(pen.getRotationInternal()) * length);
		appendPosition();
	}

	public void moveBackward(double length) {
		moveForward(-length);
	}

	public void startPlotting() {
		if (!isPlotting) {
			startPlot();
		}
	}

	public void stopPlotting() {
		if (isPlotting) {
			finishPlot();
		}
	}

	public void setPenVisible(boolean b) {
		pen.setVisible(b);
		fireDataChanged();
	}

	public boolean getPenVisible() {
		return pen.getVisible();
	}

	public void setPenColor(Color c) {
		pen.setLineColor(c);
	}
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
        attributes.add(new Attribut("isplotting", isPlotting));
    	attributes.add(new Attribut("penx", pen.getX()));
        attributes.add(new Attribut("peny", pen.getY()));
        attributes.add(new Attribut("penangle", pen.getRotation()));
        attributes.add(new Attribut("penvisible", pen.getVisible()));
        attributes.add(new Attribut("pencolor", polygon.getLineColor()));               	
	}
    @Override
    public Figure copy() {
        Plotter g = (Plotter)super.copy();
        g.polygon = (Polygon)polygon.copy();
        return g;
    }
	@Override
	public void setLineColor(Color color) {
		startPlot();
		polygon.setLineColor(color);
	}

	public Color getLineColor() {
		return polygon.getLineColor();
	}

	@Override
	public void setLineStyle(LineStyle linestyle) {
		startPlot();
		polygon.setLineStyle(linestyle);
	}

	@Override
	public void setLineWidth(double linewidth) {
		startPlot();
		polygon.setLineWidth(linewidth);
	}
	public double getLineWidth() {
		return polygon.getLineWidth();
	}
	@Override
	public void setFillStyle(FillStyle fillstyle) {
		startPlot();
		polygon.setFillStyle(fillstyle);
	}

	@Override
	public void setFillColor(Color color) {
		startPlot();
		polygon.setFillColor(color);
	}    
}
