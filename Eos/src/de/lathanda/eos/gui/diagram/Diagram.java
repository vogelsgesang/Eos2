package de.lathanda.eos.gui.diagram;

import de.lathanda.eos.interpreter.BackgroundCompiler;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Basisklasse für alle Diagramme.
 *
 * @author Peter (Lathanda) Schneider
 */
public abstract class Diagram extends JPanel {
	private static final long serialVersionUID = 8035403439280864410L;
	/**
	 * Fenstertitel
	 */
	private final String title;
	/**
	 * Erzeugt eine neue Diagrammkomponente
	 * @param title
	 */
    public Diagram(String title) {
        this.title = title;
        setBackground(Color.WHITE);
    }

    /**
     * Erzeugt eine Bitmap des Diagramms.
     * Die Bitmap muss vom Aufrufer zerstört werden wenn sie nicht mehr benötigt wird.
     * @param dpi Auflösung
     * @return 
     */
    public abstract BufferedImage export(float dpi);
 
    /**
     * Bereitet das Zeichnen vor und registriert Ereignisse.
     * @param bc Compiler
     */
    public abstract void init(BackgroundCompiler bc);
    /**
     * Entfernt alle Ereignisse.
     * @param bc Compiler
     */
    public abstract void deinit(BackgroundCompiler bc);
    /**
     * Name des Diagramms
     * @return 
     */
    public final String getTitle() {
        return title;
    }
}
