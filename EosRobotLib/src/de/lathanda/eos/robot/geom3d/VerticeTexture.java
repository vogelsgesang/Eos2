package de.lathanda.eos.robot.geom3d;
/**
 * Texturkoordinate einer Ecke.
 * Die Koordinaten 0&le;u&le;1 und 0&le;v&le;1.
 * Geben an, welcher Punkt der Textur der Ecke entspricht.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class VerticeTexture {
	public final float u;
	public final float v;
	public VerticeTexture(float u, float v) {
		this.u = u;
		this.v = v;
	}
	public VerticeTexture(float[] uv) {
		this.u = uv[0];
		this.v = uv[1];
	}
}
