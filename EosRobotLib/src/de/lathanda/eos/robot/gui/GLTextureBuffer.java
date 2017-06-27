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
	private static HashMap<Material, GLTextureBuffer> texturebuffer = new HashMap<>();	

	public static synchronized GLTextureBuffer get(Material m, GL gl) {
		GLTextureBuffer texture = texturebuffer.get(m);
		if (texture == null) {
			texture = new GLTextureBuffer(m);
			texturebuffer.put(m, texture);
		}
		return texture;    
	}
	public static synchronized void clear(GL gl) {
		for(GLTextureBuffer buffer : texturebuffer.values()) {
			buffer.destroy(gl);
		}
	}	
	
	//****************** class *******************	
	private final Material m;
	private HashMap<GL, Texture> tbuffer = new HashMap<>();
	public GLTextureBuffer(Material m) {
		this.m = m;
	}
	public void destroy(GL gl) {
		Texture t = tbuffer.get(gl); 
        //free resources on graphic card
        if (t != null) {
            t.destroy(gl);
            tbuffer.remove(gl);
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
				Texture t = getTexture(gl);
				t.enable(gl);
				t.bind(gl);
				gl.glEnable(GL.GL_TEXTURE);
				gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
			} catch (GLException gle) {
				System.err.println(gle.getMessage());
			}
		}
	}

	private Texture getTexture(GL2 gl) {
		Texture t = tbuffer.get(gl);
		if (t == null) {
			AWTTextureData atd = new AWTTextureData(GLProfile.get(GLProfile.GL2), GL.GL_RGBA, GL.GL_RGBA, false,
					m.image);
			t = new Texture(gl, atd);
			t.setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			t.setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			tbuffer.put(gl, t);
		}
		return t;
	}

	public void closeMaterial(GL2 gl) {
		if (m.image != null) {
			Texture t = getTexture(gl);
			t.disable(gl);
			gl.glDisable(GL.GL_TEXTURE);
		}
	}
	
}
