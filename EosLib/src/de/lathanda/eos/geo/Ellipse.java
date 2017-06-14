package de.lathanda.eos.geo;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;
import static java.lang.Math.*;

import java.util.LinkedList;

/**
 * Ellipse.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Ellipse extends FilledFigure {
    private double radiusX;
    private double radiusY;
    public Ellipse() {
        super();
        radiusX = 10;
        radiusY = 10;
        
    }
    @Override
    protected void drawObject(Picture p) {
        p.drawEllipse(radiusX, radiusY);
    }
	@Override
	protected void scaleInternal(double factor) {
		radiusX *= factor;
		radiusY *= factor;
	}
    public void setCenter(double x, double y) {
        moveTo(x,y);
    }
    public void setRadiusX(double radiusX) {
        this.radiusX = radiusX;
        fireLayoutChanged();
    }
    public double getRadiusX() {
        return radiusX;
    }
    public void setRadiusY(double radiusY) {
        this.radiusY = radiusY;
        fireLayoutChanged();
    }
    public double getRadiusY() {
        return radiusY;
    }
    public void setCenterX(double x) {
        setX(x);
    }
    public double getCenterX() {
        return getX();
    }
    public void setCenterY(double y) {
        setY(y);
    }
    public double getCenterY() {
        return getY();
    } 

    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(radiusX*radiusY*Math.PI, getX(), getY()); 
    }

    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);

        double rx = t.transform(radiusX);
        double ry = t.transform(radiusY);
        double phi = getRotationInternal() + t.getAngle();
        double ux = rx * cos(phi);
        double uy = rx * sin(phi);
        double vx = ry * cos(phi+PI/2);
        double vy = ry * sin(phi+PI/2);

        double w = sqrt(ux*ux + vx*vx);
        double h = sqrt(uy*uy + vy*vy); 

        return new BoundingBox(t.getdx() - w, t.getdx() + w, t.getdy() - h, t.getdy() + h);
    }
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("centerx", getCenterX()));
        attributes.add(new Attribut("centery", getCenterY()));        
    	attributes.add(new Attribut("radiusx", getRadiusX()));
        attributes.add(new Attribut("radiusy", getRadiusY()));
	}    
}
