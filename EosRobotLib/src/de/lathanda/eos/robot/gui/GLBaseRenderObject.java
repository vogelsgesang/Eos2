package de.lathanda.eos.robot.gui;

import java.awt.Color;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import de.lathanda.eos.robot.geom3d.Face;
import de.lathanda.eos.robot.geom3d.Polyhedron;

public class GLBaseRenderObject implements GLRenderObject {
	private final Polyhedron data;
	public GLBaseRenderObject(Polyhedron data) {
		this.data = data;
	}
	public void render(GL2 gl, Color base) {
		data.faces.stream().forEach(f -> renderFace(f, base, gl));			
	}
	protected void renderFace(Face f, Color base, GL2 gl) {
		GLTextureBuffer texture = GLTextureBuffer.get(f.m); 
		texture.openMaterial(base, gl);
		gl.glBegin(GL.GL_TRIANGLES);
		for (int i = 0; i < f.v.length; i++) {
			if (f.vn != null) {
				gl.glNormal3f(f.vn[i].dx, f.vn[i].dy, f.vn[i].dz);
			}
			if (f.vt != null) {
				gl.glTexCoord2f(f.vt[i].u, 1 - f.vt[i].v); // I have no idea why
															// jogamp is
															// inverted
			}
			gl.glVertex3f(f.v[i].x, f.v[i].y, f.v[i].z);

		}
		gl.glEnd();
		texture.closeMaterial(gl);
	}
	@Override
	public void destroy(GL gl) {
		// free data?		
	}
}
