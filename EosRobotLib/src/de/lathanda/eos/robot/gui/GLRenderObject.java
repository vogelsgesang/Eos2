package de.lathanda.eos.robot.gui;

import java.awt.Color;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public interface GLRenderObject {
	void render(GL2 gl, Color base);

	void destroy(GL gl);
	
}
