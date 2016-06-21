package de.lathanda.eos.geo;
import java.util.LinkedList;

import de.lathanda.eos.base.FillStyle;
/**
 * Rechteck mit Bild als Füllung.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9
 */
import de.lathanda.eos.base.Image;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.Scaling;
/**
 * Bildrechteck.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Graphic extends Rectangle {
	private Scaling mode;
	private Image image;
	private String filename;
	
	public Graphic() {
		mode = Scaling.FIT;
		setFillStyle(FillStyle.TRANSPARENT);
		setLineStyle(LineStyle.INVISIBLE);
		image = null;
	}
    @Override
    protected void drawObject(Picture p) {
        super.drawObject(p);
        if (image != null) {
        	p.drawImage(image, -width/2, -height/2, width, height, mode);
        }
    }
    public void loadImage(String filename) {
    	image = new Image(filename);
    	this.filename = filename;
    	fireDataChanged();
    }
    public void setScalingMode(Scaling mode) {
    	this.mode = mode;
    	fireDataChanged();
    }
    public Scaling getScalingMode() {
    	return mode;
    }
    public void stretchImage() {
    	this.mode = Scaling.STRETCH;
    	fireDataChanged();
    }
    public void cutImage() {
    	this.mode = Scaling.CUT;
    	fireDataChanged();
    }
    public void fitImage() {
    	this.mode = Scaling.FIT;
    	fireDataChanged();
    }
    @Override
	public void getAttributes(LinkedList<Attribut> attributes) {
    	super.getAttributes(attributes);
        attributes.add(new Attribut("mode", mode));
        attributes.add(new Attribut("image", filename));
	}     
}
