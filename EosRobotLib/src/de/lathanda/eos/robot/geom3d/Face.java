package de.lathanda.eos.robot.geom3d;

import com.jogamp.opengl.GL2;

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
	public final int TYPE;
    public Face(Vertice[] v, VerticeTexture[] vt, VerticeNormal[] vn, Material m) {
        this.v = v;
        this.vt = vt;
        this.vn = vn;
        this.m = m;
        switch (v.length) {
        case 3: TYPE = GL2.GL_TRIANGLES; break;
        case 4: TYPE = GL2.GL_QUADS; break;
        default: TYPE = GL2.GL_POLYGON;
        }
    }
}
