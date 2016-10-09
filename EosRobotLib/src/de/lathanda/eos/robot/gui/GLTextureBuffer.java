package de.lathanda.eos.robot.gui;

import java.awt.Color;
import java.util.HashMap;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureData;

import de.lathanda.eos.robot.geom3d.Material;

public class GLTextureBuffer {
	// ***************** factory *****************
	private static HashMap<Material, GLTextureBuffer> glbuffer = new HashMap<>();	

	public static synchronized GLTextureBuffer get(Material m) {
		GLTextureBuffer buffer = glbuffer.get(m);
		if (buffer == null) {
			buffer = new GLTextureBuffer(m);
			glbuffer.put(m, buffer);
		}
		return buffer;    
	}
	public static synchronized void clear(GL gl) {
		for(GLTextureBuffer buffer : glbuffer.values()) {
			buffer.destroy(gl);
		}
	}	
	
	//****************** class *******************	
	private final Material m;
	private Texture texture;
	public GLTextureBuffer(Material m) {
		this.m = m;
	}
	public void destroy(GL gl) {
		if (texture != null) {
			texture.destroy(gl);
		}
	}
	public void openMaterial(Color base, GL2 gl) {
		gl.glColor4ub((byte) base.getRed(), (byte) base.getGreen(), (byte) base.getBlue(), (byte) base.getAlpha());

		if (m.ka != null) {
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, m.ka, 0);
		} else {
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, base.getComponents(null), 0);
		}
		if (m.kd != null) {
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, m.kd, 0);
		} else {
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, base.getComponents(null), 0);
		}
		if (m.ks != null) {
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, m.ks, 0);
		} else {
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, base.getComponents(null), 0);
		}
		if (m.image != null) {
			try {
				prepareTexture(gl);
				texture.enable(gl);
				texture.bind(gl);
				gl.glEnable(GL.GL_TEXTURE);
				gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
			} catch (GLException gle) {
				System.err.println(gle.getMessage());
			}
		}
	}

	private void prepareTexture(GL2 gl) {
		if (texture == null) {
			AWTTextureData atd = new AWTTextureData(GLProfile.get(GLProfile.GL2), GL.GL_RGBA, GL.GL_RGBA, false,
					m.image);
			texture = new Texture(gl, atd);
			texture.setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			texture.setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		}
	}

	public void closeMaterial(GL2 gl) {
		if (m.image != null) {
			prepareTexture(gl);
			texture.disable(gl);
			gl.glDisable(GL.GL_TEXTURE);
		}
	}
	
}
