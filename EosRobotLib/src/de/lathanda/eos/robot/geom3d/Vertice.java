package de.lathanda.eos.robot.geom3d;
/**
 * Ecke.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Vertice {
	public final float x;
	public final float y;
	public final float z;
	public Vertice(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vertice(float[] xyz) {
		this.x = xyz[0];
		this.y = xyz[1];
		this.z = xyz[2];
	}	
}
