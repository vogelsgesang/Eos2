package de.lathanda.eos.robot.geom3d;
/**
 * Normale einer Ecke.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class VerticeNormal {
	public final float dx;
	public final float dy;
	public final float dz;
	public VerticeNormal(float dx, float dy, float dz) {
		super();
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	public VerticeNormal(float[] dxyz) {
		this.dx = dxyz[0];
		this.dy = dxyz[1];
		this.dz = dxyz[2];
	}		
}
