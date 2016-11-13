package de.lathanda.eos.robot.gui;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.HashMap;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import de.lathanda.eos.robot.geom3d.Face;
import de.lathanda.eos.robot.geom3d.Polyhedron;

public class GLObjectBuffer {
	// ***************** factory *****************
	private static HashMap<Polyhedron, GLObjectBuffer> glbuffer = new HashMap<>();	
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
	private VertexObject vo;

	private GLObjectBuffer(Polyhedron poly) {
		this.data = poly;
	}
	public void destroy(GL gl) {
		//free resources on graphic card
	}	
	public void render(Color base, GL2 gl) {
		if (vo == null) {
			vo = VertexObject.create(gl, data);
		}
		vo.render(gl, base);
	}

	private static abstract class VertexObject {
		static VertexObject create(GL2 gl, Polyhedron poly) {
			// Check For VBO support
	        final boolean VBOsupported = gl.isFunctionAvailable("glGenBuffersARB") &&
	                gl.isFunctionAvailable("glBindBufferARB") &&
	                gl.isFunctionAvailable("glBufferDataARB") &&
	                gl.isFunctionAvailable("glDeleteBuffersARB");
	        if (VBOsupported) {
	        	return new VertexBufferObject(gl, poly);
	        } else {
	        	return new VertexArray(gl, poly);
	        }
		}
        // Mesh Data
        private int vertexCount;
        private FloatBuffer vertices;
        private FloatBuffer texCoords;
        private FloatBuffer normals;
        private int[] textureId = new int[1];  // Texture ID
		Polyhedron data;
        VertexObject(Polyhedron poly) {
        	this.data = poly;
        	vertexCount = poly.faces.size() * 3;
        	vertices  =  Buffers.newDirectFloatBuffer(vertexCount * 3);
        	texCoords =  Buffers.newDirectFloatBuffer(vertexCount * 2);
        	normals   =  Buffers.newDirectFloatBuffer(vertexCount * 3);
        	for(Face f : poly.faces) {
        		for(int i = 0; i < 3; i++) {
        			vertices.put(f.v[i].x);
        			vertices.put(f.v[i].y);
        			vertices.put(f.v[i].z);
        			if (f.vt != null) {
        				texCoords.put(f.vt[i].u);
        				texCoords.put(f.vt[i].v);
        			}
        			normals.put(f.vn[i].dx);
        			normals.put(f.vn[i].dy);
        			normals.put(f.vn[i].dz);
        		}

        	}
        }
		abstract void render(GL2 gl, Color base);
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
	}
	private static class VertexArray extends VertexObject {
		public VertexArray(GL2 gl, Polyhedron poly) {
			super(poly);
		}

		public void render(GL2 gl, Color base) {
			data.faces.stream().forEach(f -> renderFace(f, base, gl));			
		}
	}
	private static class VertexBufferObject extends VertexObject {
		public VertexBufferObject(GL2 gl, Polyhedron poly) {
			super(poly);
		}

		public void render(GL2 gl, Color base) {
			data.faces.stream().forEach(f -> renderFace(f, base, gl));
		}
	}
	
}
