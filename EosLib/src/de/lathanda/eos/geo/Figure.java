package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.base.math.Point;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.Readout;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;

/**
 * Basisklasse aller sichtbaren Objekte.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Figure implements Cloneable, Readout {
	private boolean visible;    
    private FigureGroup group;
    protected Transform transform;    
    private BoundingBox bound = new BoundingBox();
    public Figure() {
        transform = Transform.ID;
        group = null;
        visible = true;
    }
//***********
//* drawing *
//***********    
    public void draw(Picture g) {
        if (!visible) return;
        synchronized (this) {
        	g.pushTransform();
        	beforeDrawing(g);
        	g.applyTransform(transform);
        	drawObject(g);
        	g.restoreTransform();
        }
    }
    protected void beforeDrawing(Picture p) {};    
    protected abstract void drawObject(Picture p);
//*******************
//* basic abilities *
//*******************  
    public void setVisible(boolean visible) {
        this.visible = visible;
        fireDataChanged();
    }

    public boolean getVisible() {
        return visible;
    }
 
    public Figure copy() {
        try {
            return (Figure) this.clone();
        } catch (CloneNotSupportedException ex) {
            System.exit(1);
            return null;
        }
    }
    /*package*/ void replaceGroup(FigureGroup group) {
        if (this.group != null) {
            this.group.removeFigure(this);
        }
        this.group = group;
    }
    /**
     * Wird von Figuren aufgerufen, wenn sich ihr Aussehen verändert hat.
     */
    public void fireDataChanged() {
        if (group != null) {
            group.fireDataChanged();
        }
    }
    /**
     * Wird von Figuren aufgerufen, wenn sich die Form verändert hat und
     * es notwendig ist Gruppen etc. neu zu zentrieren, 
     * impliziert wird auch neu gezeichnet.
     */
    protected void fireLayoutChanged() {
        bound = null;
        if (group != null) {
            if (group.getGroup() != null) {
                group.getGroup().fireLayoutChanged();
            }
        }
        fireDataChanged();
    }

    /**
     * Bereinigt dieses Objekt, um es endgültig freizugeben.
     */
    public void free() {
        visible = false;
        if (group != null) {
            group.removeFigure(this);
        }
        group = null;
    }

//**************************
//* transformation methods *
//************************** 
    protected void moveToInternal(double x, double y) {
        transform = transform.setTranslation(x, y);
        //no update!
    }
    protected void moveInternal(double x, double y) {
        transform = transform.translate(x, y);
        //no update!
    }    
    protected void resetTransformation() {
    		transform = Transform.ID;
    		//no update!
    }
    public void move(double dx, double dy) {
        transform = transform.translate(dx, dy);
        fireLayoutChanged();        
    }
     
    public void moveTo(double x, double y) {
        transform = transform.setTranslation(x, y);
        fireLayoutChanged();
    }
    public double getX() {
        return transform.getdx();
    }
    public void setX(double x) {
        transform = transform.setdx(x);
        fireLayoutChanged();
    }
    public double getY() {
        return transform.getdy();
    }
    public void setY(double y) {
        transform = transform.setdy(y);
        fireLayoutChanged();
    }

    public void rotate(double angle) {
        transform = transform.rotate(angle/180*Math.PI);
        fireDataChanged();
    }
    public void setRotation(double angle) {
        transform = transform.setAngle(angle/180*Math.PI);
        fireDataChanged();
    }
    public double getRotation() {
        return transform.getAngle()/Math.PI*180;
    }
    protected double getRotationInternal() {
        return transform.getAngle();
    }
    public void rotateAround(double x, double y, double angle) {
        transform = transform.rotate(x,y,angle/180*Math.PI);
        fireLayoutChanged();        
    }

    public void scaleAt(double x, double y, double factor) {
        transform = transform.scalePositionAt(x,y,factor);
        scaleInternal(factor);
        fireLayoutChanged();        
    }
    public void scale(double factor) { 
        scaleInternal(factor);    	
        fireLayoutChanged();        
    }

    /**
     * Eine Streckung findet statt. Interne Maße müssen an die neue Streckung angepasst werden.
     * @param factor
     */
    protected abstract void scaleInternal(double factor);
    protected void transformMirrorX() {
        transform = transform.mirrorX();
    }
    protected void transformMirrorY() {
        transform = transform.mirrorY();
    }
    protected Point getAbsolutePosition() {
    	return getAbsolutePosition(new Point(0,0));//transform.getdx(),transform.getdy()));
    }
    private Point getAbsolutePosition(Point p) {
        if ( group != null) {
            Group g = group.getGroup();
            if ( g != null) {
                return ((Figure)g).getAbsolutePosition(transform.transform(p));
            }
        }
        return transform.transform(p);
    }
    protected Point getTransformedPosition(Point point) {
    		return transform.transform(point);
    }
    protected Point getRelativePosition(Point absolute) {
    	if (group != null) {
            Group g = group.getGroup();
            if ( g != null) {
                Point p = g.getRelativePosition(absolute);
            	return (((Figure)g).transform).transformBack(p);
            } else {
            	return absolute;
            }
    	} else {
    		return absolute;
    	}
    }
    protected void changeTransformation(Figure relativeTo) {
        if (relativeTo.group != null) {
            Figure parent = relativeTo.group.getGroup();
            if (parent != null) {
                changeTransformation(parent);
            }
        }
        transform = relativeTo.transform.transformBack(transform);
    }
    protected void restoreTransformation(Group relativeTo) {
        Group parent = relativeTo; 
        while (parent != null) {
        	transform = transform.transform(relativeTo.transform);
        	parent = parent.getGroup();
        }
    }    
    //******************
    //* Layout Methods *
    //******************
    protected abstract BalancePoint getBalancePoint();

    //*********************
    //* BoundingBox logic *
    //*********************
    protected BoundingBox calculateBoundingBox(Transform base) {
        return calculateBoundingBox(base, transform);
    };    
    protected abstract BoundingBox calculateBoundingBox(Transform base, Transform own);    
    public BoundingBox getBound() {
        if (bound == null) {
            bound = calculateBoundingBox(Transform.ID, transform);
        }
        return bound;
    }
    public void alignLeftTop(double left, double top) {
        BoundingBox b = getBound();
        move(left-b.getLeft(), top-b.getTop());
        fireLayoutChanged();
    }
    public void alignRightBottom(double right, double bottom) {
        BoundingBox b = getBound();
        move(right-b.getRight(), bottom-b.getBottom());
        fireLayoutChanged();
    }

    public void alignLeft(double left) {
        BoundingBox b = getBound();
        move(left-b.getLeft(), 0);
        fireLayoutChanged();
    }
    public void alignRight(double right) {
        BoundingBox b = getBound();
        move(right-b.getRight(), 0);
        fireLayoutChanged();
    }
    public void alignTop(double top) {
        BoundingBox b = getBound();
        move(0, top-b.getTop());
        fireLayoutChanged();
    }
    public void alignBottom(double bottom) {
        BoundingBox b = getBound();
        move(0, bottom-b.getBottom());
        fireLayoutChanged();
    }   
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
        attributes.add(new Attribut("visible", visible));
        attributes.add(new Attribut("x", transform.getdx()));
        attributes.add(new Attribut("y", transform.getdy()));
        double angle = transform.getAngle()/Math.PI*180 % 360d;
        if (angle < 0) { angle += 360d; }
        attributes.add(new Attribut("angle", Math.round(angle)));
        attributes.add(new Attribut("mirrored", transform.getMirrorX()));		
	}
}
