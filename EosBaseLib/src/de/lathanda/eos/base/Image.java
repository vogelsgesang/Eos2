package de.lathanda.eos.base;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Bild.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Image {
	private BufferedImage image;
	private int imageX;
	private int imageY;
	private int imageWidth;
	private int imageHeight;
	
	public Image(String source) {
		try (BufferedInputStream bi = new BufferedInputStream(ResourceLoader.getResourceAsStream(source))){
			image = ImageIO.read(bi);
		} catch (IOException fnfe) {
			image = null;
			return;
		}
		this.imageX = 0;
		this.imageY = 0;
		this.imageWidth = image.getWidth();
		this.imageHeight = image.getHeight();
	}
	
	public Image(BufferedImage image, int imageX, int imageY, int imageWidth, int imageHeight) {
		super();
		this.image = image;
		this.imageX = imageX;
		this.imageY = imageY;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	protected BufferedImage getImage() {
		return image;
	}
	protected int getImageX() {
		return imageX;
	}

	public int getImageY() {
		return imageY;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
	
}
