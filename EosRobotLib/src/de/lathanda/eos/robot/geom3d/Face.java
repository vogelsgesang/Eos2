package de.lathanda.eos.robot.geom3d;

/**
 * Räumliche Fläche.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Face {
	public final Vertice[] v;
	public final VerticeTexture[] vt;
	public final VerticeNormal[] vn;
	public final Material m;

    public Face(Vertice[] v, VerticeTexture[] vt, VerticeNormal[] vn, Material m) throws NFaceException {
        this.v = v;
        this.vt = vt;
        this.vn = vn;
        this.m = m;
        if (v.length != 3) {
        	throw new NFaceException(v.length);
        }
    }
}
