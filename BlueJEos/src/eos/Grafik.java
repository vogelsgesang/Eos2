package eos;

import de.lathanda.eos.geo.FilledFigure;
import de.lathanda.eos.geo.Graphic;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse GRAFIK.
 * Diese leitet alle Methodenafurufe an die Klasse {@link de.lathanda.eos.geo.Graphic} weiter.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class Grafik  extends Rechteck {
	private final Graphic graphic;
    public Grafik() {
    	graphic = new Graphic();
    }
    @Override
    protected FilledFigure getFigure() {
        return graphic;
    }
    public void ladeBild(String dateiname) {
    	graphic.loadImage(dateiname);
    }
    public void bildStrecken() {
    	graphic.stretchImage();
    }
    public void bildAnpassen() {
    	graphic.fitImage();
    }
    public void bildAbschneiden() {
    	graphic.cutImage();
    }
}
