package de.lathanda.eos.common.gui;

import java.awt.Toolkit;
import java.util.ResourceBundle;

/**
 * Sammlung von häufig verwendeten Konstanten für die
 * grafische Oberfläche.
 *
 * @author Peter (Lathanda) Schneider
 */
public interface GuiConstants {
	/**
	 * Oberflächentexte.
	 */
    ResourceBundle GUI   = ResourceBundle.getBundle("text.gui");

    /**
     * Die vermutliche Bildschirmauflösung.
     * 
     * Die Betriebssysteme kennen die exakte Bildschirmauflösung
     * nur ungefähr und ab und zu wird absichtlich gelogen. 
     * (zB Dynamic Super Resolution).
     */
    int DPI = Toolkit.getDefaultToolkit().getScreenResolution();
    /**
     * Millimeter in Pixel umrechnen.
     * 
     * @param mm
     * @return
     */
    public static int mm2pixel(double mm) {
        return (int)(DPI / 25.4f * mm);
    }
    /**
     * Pixel in Millimeter umrechnen.
     * 
     * @param pixel
     * @return
     */
    public static double pixel2mm(int pixel) {
        return pixel * 25.4f / DPI;
    }
}
