package de.lathanda.eos.base;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Helferklasse zum Laden und Finden von Ressourcen.
 * Diese Klasse erlaubt es, sowohl Dateien aus einem Projekt, wie auch Dateien aus dem Jar zu verwenden.
 * Alle Daten die durch diese Klasse geladen werden, müssen als nicht vertrauenswürdig betrachet werden,
 * da ein Benutzer diese jederzeit durch Anlegen einer lokalen Dateien austauschen kann. 
 *
 * @author Peter (Lathanda) Schneider
 */
public class ResourceLoader {
	private static String workingDirectory;
	/**
	 * Bild laden.
	 * @param name Dateiname
	 * @return Bild
	 */
    public static BufferedImage loadImage(String name) {
        try (InputStream in = getResourceAsStream(name)){
            return ImageIO.read(in);
        } catch (IOException ioe) {
            return createErrorImage(ioe);
        }
    }
    /**
     * Arbeitsverzeichnis festlegen. Dieses wird
     * in alle Suchen mit einbezogen.
     * @param workingDirectory Arbeitsverzeichnis
     */
    public static void setWorkingDirectory(String workingDirectory) {
    	if (workingDirectory.endsWith("/")) {
    		ResourceLoader.workingDirectory = workingDirectory;
    	} else {
    		ResourceLoader.workingDirectory = workingDirectory + "/";
    	}
    }
    /**
     * Icon laden.
     * @param name Dateiname
     * @return Icon
     */
    public static ImageIcon loadIcon(String name) {
        return new ImageIcon(loadImage(name));
    }
    /**
     * Fehlerbild erzeugen.
     * @param e Ausnahme
     * @return Bild
     */
    private static BufferedImage createErrorImage(Exception e) {
        BufferedImage err = new BufferedImage(256, 256, TYPE_INT_RGB);
        err.createGraphics().drawString(e.getLocalizedMessage(), 5, 30);
        return err;
    }
    /**
     * Öffnet eine Ressource als Datenstrom
     * Der Aufrufer ist dafür verantwortlich den Datenstrom zu schließen.
     * @param filename Name der Datei
     * @return
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("resource")
	public static InputStream getResourceAsStream(String filename) throws FileNotFoundException {
        InputStream is = null;
        //1. try as file
        try {
            is = new FileInputStream(filename);
        } catch (FileNotFoundException fnfe) {
            //nothing to do
        }
        //2. try thread class loader
        if (is == null) {
        	is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        }        
        //3. try resource class loader
        if (is == null) {
            is = ResourceLoader.class.getClassLoader().getResourceAsStream(filename);
        }
        //4. try system class loader
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(filename);
        }
        //5. try working directory
        if (is == null) {
            try {
                is = new FileInputStream(workingDirectory + filename);
            } catch (FileNotFoundException fnfe) {
                //nothing to do
            }        	
        }
        //6. give up
        if (is == null) {
            throw new FileNotFoundException(filename);
        }

        return is;
    }
    /**
     * Schließt den Datenstrom, wobei Fehler verworfen werden.
     * Dies ist sinnvoll, wenn man auf den Fehler sowieso nicht sinnvoll reagieren kann.
     * 
     * @param input
     */
    public static void closeQuietly(InputStream input) {

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
