package de.lathanda.eos.gui.classchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.Type;

public class ClassDiagram extends JPanel {
	private static final long serialVersionUID = -5709882169808097527L;
	
	private ClassUnit unit;
	private static final float SPACE = 5;
	private final Drawing d;
	public ClassDiagram() {
		super();
		setBackground(Color.WHITE);
		d = new Drawing();		
	}
	public void setData(Type data) {
		unit = new ClassUnit(data);
		setPreferredSize(layout(d));	
		revalidate();
		repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}
    public void render(Graphics2D g) {
        d.init(g);
        render(d);
    }
	public BufferedImage export(float dpi) {
		Drawing drawing = new Drawing(300);
		Dimension dim = layout(drawing);
		BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, dim.width, dim.height);
		drawing.init(g);
		render(drawing);
		return image;

	}
	private Dimension layout(Drawing d) {
		unit.layout(d);
		return new Dimension(d.convertmm2pixel(unit.getWidth() + SPACE * 2), d.convertmm2pixel(unit.getHeight() + SPACE * 2));
	}
	private void render(Drawing d) {
		if (unit == null) return;
		d.pushTransform();
		d.translate(SPACE, SPACE);
		d.setDrawWidth(0.5f);
		d.setColor(Color.BLACK);
		unit.draw(d);
		d.popTransform();
	}	
}
