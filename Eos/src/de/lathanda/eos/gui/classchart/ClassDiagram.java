package de.lathanda.eos.gui.classchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.interpreter.Type;

public class ClassDiagram extends JPanel {
	private static final long serialVersionUID = -5709882169808097527L;
	
	private ClassUnit unit;
	private static final float SPACE = 5;
	private final Drawing d;
	private Type type;
	public ClassDiagram() {
		super();
		setBackground(Color.WHITE);
		d = new Drawing();
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	public void setData(Type data) {
		this.type = data;
		unit = new ClassUnit(data);
		setPreferredSize(layout(d));	
		revalidate();
		repaint();
	}
	
	@Override
	public String getToolTipText(MouseEvent me) {
		if (unit != null) {
			return unit.getToolTipText(d.convertpixel2mm(me.getX())- SPACE, d.convertpixel2mm(me.getY()) - SPACE);
		} else {
			return "";
		}
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
		if (type == null) return new BufferedImage(0,0, BufferedImage.TYPE_INT_ARGB);
		ClassUnit unit = new ClassUnit(type);
		Drawing drawing = new Drawing(300);
		unit.layout(drawing);
		Dimension dim = new Dimension(drawing.convertmm2pixel(unit.getWidth()), drawing.convertmm2pixel(unit.getHeight()));;
		BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, dim.width, dim.height);
		drawing.init(g);
		drawing.pushTransform();
		drawing.setDrawWidth(0.5f);
		drawing.setColor(Color.BLACK);
		unit.draw(drawing);
		drawing.popTransform();
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
