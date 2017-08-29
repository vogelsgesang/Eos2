package de.lathanda.eos.geo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.geom.Tesselation;
import de.lathanda.eos.game.geom.Triangle;

/**
 * Polygonobjekte werden vom Plotter benutzt.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Polygon extends FilledFigure {
	private ArrayList<Point> points;
	private Collection<Triangle> triangles;
	private LinkedList<? extends Point> bound;
	private BalancePoint balance;
	public Polygon() {
		points = new ArrayList<>();
		line.setDrawWidth(0.5f);
	}

	@Override
	protected void drawObject(Picture p) {
		synchronized (points) {
			if (fill.getFillStyle() != FillStyle.TRANSPARENT && bound != null) {
				p.drawPolygon(bound);
			} else {
				for (int i = 1; i < points.size(); i++) {
					p.drawLine(points.get(i - 1), points.get(i));
				}
			}
		}
	}
	@Override
	protected void scaleInternal(double factor) {
		for (int i = points.size(); i-- > 0;) {
			points.get(i).scale(factor);
		}
		updatePolygon();
	}
	
	@Override
	protected BalancePoint getBalancePoint() {
		return balance;
	}
	
	public boolean isValid() {
		return points.size() > 1;
	}

	public void addPoint(Point p) {
		synchronized (points) {
			Point copy = new Point(p);
			copy.move(-getX(), -getY());
			points.add(copy);
		}
		updatePolygon();
		fireLayoutChanged();
	}
	private void updatePolygon() {
		if (fill.getFillStyle() != FillStyle.TRANSPARENT) {
			Tesselation tess = new Tesselation(points);
			tess.tesselate();
			bound = tess.getBorder();
			triangles = tess.getTriangles();
			double weight = 0;
			double x = 0;
			double y = 0;
			for (Triangle t : triangles) {
				double triangle_weight = t.getArea();
				Point center = t.getCenter();
				x += center.getX() * triangle_weight;
				y += center.getY() * triangle_weight;
				weight += triangle_weight;
			}
			balance = new BalancePoint(weight, new Point(x / weight, y / weight));
		} else {
			bound = null;
			triangles = null;
			double weight = 0;
			double x = 0;
			double y = 0;
			Point last = points.get(points.size() - 1);
			for (Point p : points) {
				double line_weight = new Vector(last, p).getLength() * line.getDrawWidth();
				x += (last.getX() + p.getX()) * line_weight / 2;
				y += (last.getY() + p.getY()) * line_weight / 2;
				weight += line_weight;
			}
			balance = new BalancePoint(weight, new Point(x / weight, y / weight));			
		}
	}
	@Override
	protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
		Transform t = base.transform(own);
		BoundingBox bound = new BoundingBox();
		for (Point p:points) {
			bound.add(t.transform(p));
		}
		return bound;
	}
    @Override
    public Figure copy() {
    	Polygon poly = (Polygon)super.copy();
    	poly.points = new ArrayList<>();
    	for(Point p : points) {
    		poly.points.add(new Point(p));
    	}
    	poly.updatePolygon();
        return poly;
    }
	@Override
	public String toString() {
		return "Polygon{" + points + '}';
	}
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);    	
    	StringBuilder coords = new StringBuilder();
    	for (Point p : points) {
    		coords.append(p.toString());
    	}
    	attributes.add(new Attribut("points", coords));
	}  
}
