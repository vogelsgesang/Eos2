package de.lathanda.eos.gui.objectchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;

import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.Drawing;
import de.lathanda.eos.gui.diagram.MemoryEntry;

/**
 * Diese Komponente zeigt eine Liste von Objekten und Variablen an.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ObjectDiagram extends JPanel {
	private static final long serialVersionUID = -8489967668090334257L;
	private LinkedList<Unit> units;
	private final Drawing d;
	private static final float SPACE = 5;
	private LinkedList<MemoryEntry> data;

	public ObjectDiagram() {
		super();
		setBackground(Color.WHITE);
		units = new LinkedList<>();
		d = new Drawing();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	public BufferedImage export(float dpi) {
		if (data == null) return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		 LinkedList<Unit> units = new LinkedList<>();
		for (MemoryEntry v : data) {
			units.add(Toolkit.create(v));
		}
		Drawing drawing = new Drawing(300);
		units.forEach(p -> p.layout(drawing));
		float h = 0;
		float w = 0;
		for (Unit u : units) {
			u.setOffsetY(h);
			h = h + u.getHeight() + SPACE;
			if (w < u.getWidth()) {
				w = u.getWidth();
			}
		}
		h -= SPACE;
		Dimension dim = new Dimension(drawing.convertmm2pixel(w), drawing.convertmm2pixel(h));
		BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, dim.width, dim.height);
        drawing.init(g);
        drawing.setDrawWidth(0.5f);
        drawing.setColor(Color.BLACK);
		units.stream().forEachOrdered(p -> p.draw(drawing));
		return image;

	}
    public void render(Graphics2D g) {
        d.init(g);
        render(d);
    }
	private Dimension layout(Drawing d) {
		units.forEach(p -> p.layout(d));
		float h = 0;
		float w = 0;
		for (Unit u : units) {
			u.setOffsetY(h);
			h = h + u.getHeight() + SPACE;
			if (w < u.getWidth()) {
				w = u.getWidth();
			}
		}
		h -= SPACE;
		return new Dimension(d.convertmm2pixel(w + SPACE * 2), d.convertmm2pixel(h + SPACE * 2));
	}

	private void render(Drawing d) {
		d.pushTransform();
		d.translate(SPACE, SPACE);
		d.setDrawWidth(0.5f);
		d.setColor(Color.BLACK);
		units.stream().forEachOrdered(p -> p.draw(d));
		d.popTransform();
	}

	public void setData(LinkedList<MemoryEntry> data) {
		this.data = data;
		units.clear();
		for (MemoryEntry v : data) {
			units.add(Toolkit.create(v));
		}
		setPreferredSize(layout(d));
		revalidate();
		repaint();
	}

}
