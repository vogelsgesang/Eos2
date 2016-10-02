package de.lathanda.eos.robot.gui;

import java.awt.Color;
import java.util.TreeMap;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import de.lathanda.eos.robot.geom3d.Face;
import de.lathanda.eos.robot.geom3d.Polyhedron;

public class GLObjectBuffer {
	// ***************** factory *****************
	private static TreeMap<Polyhedron, GLObjectBuffer> glbuffer = new TreeMap<>();	
	public static synchronized GLObjectBuffer get(Polyhedron poly) {
		GLObjectBuffer buffer = glbuffer.get(poly);
		if (buffer == null) {
			buffer = new GLObjectBuffer(poly);
			glbuffer.put(poly, buffer);
		}
		return buffer;    
	}
	public static synchronized void clear(GL gl) {
		for(GLObjectBuffer buffer : glbuffer.values()) {
			buffer.destroy(gl);
		}
	}		
	
	//****************** class *******************
	private final Polyhedron data;

	private GLObjectBuffer(Polyhedron poly) {
		this.data = poly;
	}
	public void destroy(GL gl) {
		//free resources on graphic card
	}	
	public void render(Color base, GL2 gl) {
		data.faces.stream().forEach(f -> renderFace(f, base, gl));
	}


	private void renderFace(Face f, Color base, GL2 gl) {
		GLTextureBuffer texture = GLTextureBuffer.get(f.m); 
		texture.openMaterial(Color.GREEN, gl);
		gl.glBegin(f.type);
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
	
}
