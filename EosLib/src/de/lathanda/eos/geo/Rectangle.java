package de.lathanda.eos.geo;

import de.lathanda.eos.base.layout.BalancePoint;

import java.util.LinkedList;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;

/**
 * Rechteck.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Rectangle extends FilledFigure {
    protected double width;
    protected double height;
    public Rectangle() {
        super();
        width  = 20;
        height = 20;
    }

    @Override
    protected void drawObject(Picture p) {
        p.drawRect(width, height);
    }
	@Override
	protected void scaleInternal(double factor) {
		width *= factor;
		height *= factor;
	}

    public void setWidth(double width) {
        this.width = width;
        fireLayoutChanged();
    }
    public double getWidth() {
        return width;
    }
    public void setHeight(double height) {
        this.height = height;
        fireLayoutChanged();
    }
    public double getHeight() {
        return height;
    }
    public void setCorners(double left, double top, double right, double bottom) {
        Box b = this.new Box();
        b.left   = left;
        b.top    = top;
        b.right  = right;
        b.bottom = bottom;
        b.writeBack();
        fireDataChanged();
    }
    public void setLeftTop(double left, double top) {
        moveTo(left + width / 2, top - height / 2);
        fireLayoutChanged();
    }
    public void setRightBottom(double right, double bottom) {
    	moveTo(right - width / 2, bottom + height / 2);
        fireLayoutChanged();
    }

    public double getLeft() {
        return getX() - width / 2;
    }
    public double getRight() {
        return getX() + width / 2;
    }
    public double getTop() {
        return getY() + height / 2;
    }
    public double getBottom() {
        return getY() - height / 2;
    }
    public void setLeft(double left) {
        Box b = this.new Box();
        b.setLeft(left);
        b.writeBack();
    }
     public void setRight(double right) {
        Box b = this.new Box();
        b.setRight(right);
        b.writeBack();
    }    
    public void setTop(double top) {
        Box b = this.new Box();
        b.setTop(top);
        b.writeBack();
    }    
    public void setBottom(double bottom) {
        Box b = this.new Box();
        b.setBottom(bottom);
        b.writeBack();
    }   
   
    private class Box {
        private double left;
        private double right;
        private double top;
        private double bottom;
        Box() {
            left   = getX() - width / 2;
            right  = getX() + width / 2;
            top    = getY() + height / 2;
            bottom = getY() - height /2;
        }
        
        public void setLeft(double left) {
    		this.left = left;
        	if (left > right) {
        		right = left + width;
        	}
		}

		public void setRight(double right) {
			this.right = right;
			if (left > right) {
				left = right - width;
			}
		}

		public void setTop(double top) {
			this.top = top;
			if (bottom > top) {
				bottom = top - height;
			}
		}

		public void setBottom(double bottom) {
			this.bottom = bottom;
			if (bottom > top) {
				top = bottom + height;
			}
		}

		public void writeBack() {
    		width  = Math.abs(right - left);
            height = Math.abs(top - bottom);
            Rectangle.this.moveTo((right + left)/2,(top + bottom)/2);
        }
    }

    @Override
    protected BalancePoint getBalancePoint() {
        return new BalancePoint(width*height, getX(), getY());
    }   
    @Override
    protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
        Transform t = base.transform(own);
        BoundingBox bound = new BoundingBox();
        bound.add(t.transform(+ width / 2, + height / 2));
        bound.add(t.transform(- width / 2, + height / 2));
        bound.add(t.transform(- width / 2, - height / 2));
        bound.add(t.transform(+ width / 2, - height / 2));       
        return bound;
    }
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
    	attributes.add(new Attribut("width", getWidth()));
        attributes.add(new Attribut("height", getHeight()));
    	attributes.add(new Attribut("left", getLeft()));
    	attributes.add(new Attribut("top", getTop()));
        attributes.add(new Attribut("right", getRight()));
        attributes.add(new Attribut("bottom", getBottom()));
	} 
}
