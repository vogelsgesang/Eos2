package de.lathanda.eos.robot.geom3d;

import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * Material einer Fläche.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Material implements Comparable<Material> {
	private static int nextID = 0;
	public BufferedImage image;
	private final int id;
	public float[] ka;
	public float[] kd;
	public float[] ks;

	public Material() {
		id = nextID++;
	}
	public Material(Color c) {
		id = nextID++;
		ka = c.getComponents(null);
		kd = c.getComponents(null);
		ks = c.getComponents(null);
	}
	public Material(BufferedImage image) {
		this(Color.BLACK);
		this.image = image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public void setKD(float[] kd) {
		this.kd = Color.WHITE.getComponents(null);
		for(int i = 0; i < kd.length; i++) {
			this.kd[i] = kd[i];
		}
	}
	public void setKA(float[] ka) {
		this.ka = Color.WHITE.getComponents(null);
		for(int i = 0; i < ka.length; i++) {
			this.ka[i] = ka[i];
		}
	}
	public void setKS(float[] ks) {
		this.ks = Color.WHITE.getComponents(null);		
		for(int i = 0; i < ks.length; i++) {
			this.ks[i] = ks[i];
		}
	}
	@Override
	public int compareTo(Material m) {
		return m.id - id;
	}
}
