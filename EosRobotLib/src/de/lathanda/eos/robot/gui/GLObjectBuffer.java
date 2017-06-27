package de.lathanda.eos.robot.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import de.lathanda.eos.robot.geom3d.Polyhedron;

public class GLObjectBuffer {
    // ***************** factory *****************
    private static HashMap<Polyhedron, GLObjectBuffer> objbuffer = new HashMap<>();    
    public static synchronized GLObjectBuffer get(Polyhedron poly) {        
        GLObjectBuffer obj = objbuffer.get(poly);
        if (obj == null) {
        	obj = new GLObjectBuffer(poly);
            objbuffer.put(poly, obj);
        }
        return obj;    
    }
    public static synchronized void clear(GL gl) {
        for(GLObjectBuffer obj : objbuffer.values()) {
            obj.destroy(gl);
        }
    }        
    
    //****************** class *******************
    private final Polyhedron data;
    private HashMap<GL , LinkedList<GLRenderObject> > robuffer = new HashMap<>();

    private GLObjectBuffer(Polyhedron poly) {
        this.data = poly;
    }
    public void destroy(GL gl) {
    	LinkedList<GLRenderObject> ro = robuffer.get(gl); 
        //free resources on graphic card
        if (ro != null) {
            ro.forEach( r->r.destroy(gl));
            robuffer.remove(gl);
        }
    }    
    public void render(Color base, GL2 gl) {
    	LinkedList<GLRenderObject> ro = robuffer.get(gl); 
        if (ro == null) {
            ro = new LinkedList<>(); 
            final boolean VBOsupported = gl.isFunctionAvailable("glGenBuffersARB") &&
                    gl.isFunctionAvailable("glBindBufferARB") &&
                    gl.isFunctionAvailable("glBufferDataARB") &&
                    gl.isFunctionAvailable("glDeleteBuffersARB");
            if (VBOsupported) {
                ro = VertexBufferObject.create(data);
            } else {
                ro = new LinkedList<>();
                ro.add(new GLBaseRenderObject(data));
            }    
            robuffer.put(gl,  ro);
        }
        ro.forEach( r -> r.render(gl, base));
    }

}
