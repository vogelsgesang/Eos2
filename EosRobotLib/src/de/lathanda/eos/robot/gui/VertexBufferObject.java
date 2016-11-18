package de.lathanda.eos.robot.gui;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import de.lathanda.eos.robot.geom3d.Face;
import de.lathanda.eos.robot.geom3d.Material;
import de.lathanda.eos.robot.geom3d.Polyhedron;

public class VertexBufferObject implements GLRenderObject{
	private volatile boolean vboReady = false;
	private Material m;
	FloatBuffer vertices;
	FloatBuffer normals;
	FloatBuffer texCoords;
	private int vboId;
	private int vOff, tOff, nOff, size;
	private int vCount;
	private VBORenderer renderer;
	public static LinkedList<GLRenderObject> create(Polyhedron poly) {
		TreeMap<Material, LinkedList<Face> > faceGroups = new TreeMap<>();
		for(Face f: poly.faces) {
			LinkedList<Face> group;
			if ( faceGroups.containsKey(f.m)) {
				group = faceGroups.get(f.m);
			} else {
				group = new LinkedList<Face>();
				faceGroups.put(f.m, group);
			}
			group.add(f);
		}
		
		LinkedList<GLRenderObject> buffers = new LinkedList<>();
		for (Entry<Material, LinkedList<Face> > entry : faceGroups.entrySet()) {
			buffers.add(new VertexBufferObject(entry.getValue(), entry.getKey()));
		}
		return buffers;
	}
	private VertexBufferObject(LinkedList<Face> faces, Material m) {
		this.m = m;
		this.vCount = faces.size() * 3;
        this.vertices = Buffers.newDirectFloatBuffer(vCount * 3);
		this.normals = Buffers.newDirectFloatBuffer(vCount * 3);
		for(Face face: faces) {
			for(int i = 0; i < 3; i++) {
				vertices.put(face.v[i].x);
				vertices.put(face.v[i].y);
				vertices.put(face.v[i].z);
				normals.put(face.vn[i].dx);
				normals.put(face.vn[i].dy);
				normals.put(face.vn[i].dz);
			}
		}

		if (m.image == null) {
			this.renderer = new NoTextureRenderer();
			this.texCoords = null;
		} else {
			this.texCoords = Buffers.newDirectFloatBuffer(vCount * 2);
			for(Face face: faces) {
				for(int i = 0; i < 3; i++) {
					texCoords.put(face.vt[i].u);
					texCoords.put(1 - face.vt[i].v); //Jogamp has inverted coords, i don't know why
				}
			}
			this.renderer = new TextureRenderer();
		}		
	}
	
	private void setupVBO(GL2 gl) {
		vertices.rewind();
		normals.rewind();
		int vSize = vertices.capacity() * 4;
		int nSize = normals.capacity() * 4;
		int tSize;
		if (texCoords != null) {
			texCoords.rewind();
			tSize = texCoords.capacity() * 4;
		} else {
			tSize = 0;
		}
		vOff = 0;
		tOff = vOff + vSize;
		nOff = tOff + tSize;
		size = nOff + nSize;
		
		int[] bufferId = new int[1];
		gl.glGenBuffers(1, bufferId, 0);
		vboId = bufferId[0];
		if (vboId < 1) {
			throw new RuntimeException("aquiring VBO id failed!");
		}
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboId);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, size, null, GL2.GL_STATIC_DRAW);
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, vOff, vSize, vertices);
		if (texCoords != null) {
			gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, tOff, tSize, texCoords);
		}
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, nOff, nSize, normals);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		vboReady = true;
	}
	public void render(GL2 gl, Color base) {
		renderer.render(gl,  base);
	}
	private static interface VBORenderer {
		void render(GL2 gl, Color base);
	}
	private class TextureRenderer implements VBORenderer {
		@Override
		public void render(GL2 gl, Color base) {
			if (!vboReady) {
				setupVBO(gl);
			}
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboId);
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, vOff);
			gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, tOff);
			gl.glNormalPointer(GL.GL_FLOAT,0 , nOff);
			GLTextureBuffer texture = GLTextureBuffer.get(m); 
			texture.openMaterial(base, gl);
			gl.glDrawArrays(GL.GL_TRIANGLES,0, vCount);
			texture.closeMaterial(gl);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		}				
	}
	private class NoTextureRenderer implements VBORenderer {
		@Override
		public void render(GL2 gl, Color base) {
			if (!vboReady) {
				setupVBO(gl);
			}
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboId);
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, vOff);
			gl.glNormalPointer(GL.GL_FLOAT,0 , nOff);
			GLTextureBuffer texture = GLTextureBuffer.get(m); 
			texture.openMaterial(base, gl);
			gl.glDrawArrays(GL.GL_TRIANGLES,0, vCount);
			texture.closeMaterial(gl);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		}						
	}
	@Override
	public void destroy(GL gl) {
		//free vbo ?
	}
	
}
