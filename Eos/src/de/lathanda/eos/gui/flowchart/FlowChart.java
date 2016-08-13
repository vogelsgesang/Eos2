package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.common.AbstractProgram;
import de.lathanda.eos.common.BackgroundCompiler;
import de.lathanda.eos.common.CompilerListener;
import de.lathanda.eos.common.ErrorInformation;
import de.lathanda.eos.common.GuiConstants;
import de.lathanda.eos.common.ProgramUnit;
import de.lathanda.eos.gui.diagram.Diagram;
import de.lathanda.eos.gui.diagram.Drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Kontrollflussdiagramm.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class FlowChart extends Diagram implements CompilerListener {
	private static final long serialVersionUID = 401580908976907379L;
	private final Drawing d;
    private final LinkedList<Procedure> procedures;
    private static final float SPACE = 10;

    public FlowChart() {
        super(GuiConstants.GUI.getString("FlowChart.Title"));
        procedures = new LinkedList<>();
        d = new Drawing();        
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D)g);
    }
    @Override
    public void init(BackgroundCompiler bc) {
        bc.addCompilerListener(this);
    }

    @Override
    public void deinit(BackgroundCompiler bc) {
        bc.addCompilerListener(this);
    }

    @Override
    public void compileComplete(LinkedList<ErrorInformation> errors, AbstractProgram program) {
        procedures.clear();
        //create units
        procedures.add(new Procedure(Unit.lm.getLabel("Main"), program.getProgram()));
        for (ProgramUnit pu : program.getSubPrograms()) {
            procedures.add(new Procedure(pu.getName(), pu.getSequence()));
        }

        this.setPreferredSize(layout(d));
        revalidate();
        repaint();
    }

    @Override
    public BufferedImage export(float dpi) {
        Drawing drawing = new Drawing(300);
        Dimension dim = layout(drawing);
        BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(255,255,255,0));
        g.fillRect(0, 0, dim.width, dim.height);
        drawing.init(g);
        render(drawing);
        return image;
    }
    private Dimension layout(Drawing d) {
        procedures.forEach(p -> p.layout(d));
        float h = 0;
        float w = 0;        
        for (Unit u : procedures) {
            u.setOffsetY(h);
            h = h + u.getHeight() + SPACE;
            if (w < u.getWidth()) {
                w = u.getWidth();
            }
        }
        return new Dimension(            
            d.convertmm2pixel(w + SPACE * 2),
            d.convertmm2pixel(h + SPACE * 2)
        );
    }
    private void render(Drawing d) {
        d.pushTransform();
        d.translate(SPACE, SPACE);
        d.setDrawWidth(0.5f);
        procedures.stream().forEachOrdered(p -> p.draw(d));  
        d.popTransform();
    }

    public void render(Graphics2D g) {
        d.init(g);
        render(d);
    }
}
