package de.lathanda.eos.base;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * In Spalten und Zeilen organisierte Bildersammlung.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class TileSet {
	private BufferedImage image;
	private int tileWidth;
	private int tileHeight;
	private int columns;
	/**
	 * Erzeugt aus einem Bild eine Bildserie.
	 * @param source Dateiname
	 * @param tileWidth Anzahl der waagerechten Bilder
	 * @param tileHeight Anzahl der senkrechten Bilder
	 */
	public TileSet(String source, int tileWidth, int tileHeight) {
		try {
			image = ImageIO.read(ResourceLoader.getResourceAsStream(source));
		} catch (IOException fnfe) {
			image = null;
			return;
		}

		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.columns = image.getWidth() / tileWidth;
	}
	/**
	 * Liefert ein Teilbild.
	 * Die Nummerierung startet bei 0 links oben.
	 * Die Bilder werden Zeile für Zeile nummeriert.
	 * Das letzte Bild ist als rechts unten.
	 * @param nr Nummer des Bildes
	 * @return Teilbild
	 */
	public Image getTile(int nr) {
		int row = nr / columns;
		int column = nr % columns;
		return new Image(image,tileWidth * column, tileHeight * row, tileWidth, tileHeight);
	}
	/**
	 * Liefert ein Teilbild.
	 * @param row Zeile 0,1,2,3,...
	 * @param column Spalte  0,1,2,3,...
	 * @return Teilbild
	 */
	public Image getTile(int row, int column) {
		return new Image(image,tileWidth * column, tileHeight * row, tileWidth, tileHeight);
	}
}
