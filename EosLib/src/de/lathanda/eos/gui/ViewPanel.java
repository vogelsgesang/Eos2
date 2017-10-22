package de.lathanda.eos.gui;

import de.lathanda.eos.base.Picture2D;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.geo.Window;
import de.lathanda.eos.gui.event.CursorListener;
import de.lathanda.eos.gui.event.FigureListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import javax.swing.JPanel;


/**
 * Anzeige für die Eos-Figuren.
 *
 * @author Peter (Lathanda) Schneider
 */
public class ViewPanel extends JPanel {
    private static final long serialVersionUID = -5101516605708249038L;
    
    private final CursorMulticaster cursorMulticaster;
    private final Window window;
    private final Picture2D gmm;
    private int prefHeight;
    private int prefWidth;
    public ViewPanel(Window window) {
        this.window = window;
        gmm = new Picture2D(200, 200, this);
        cursorMulticaster = this.new CursorMulticaster();
        prefHeight = gmm.mm2pixel(200);
        prefWidth  = gmm.mm2pixel(200);
        window.addFigureListener(this.new DataListener());
        ViewMouseListener vml = this.new ViewMouseListener();
        addMouseMotionListener(vml);
        addMouseListener(vml);
        addComponentListener(this.new ResizeListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        AffineTransform af = ((Graphics2D)g).getTransform();
        gmm.setGraphics((Graphics2D)g);
        gmm.drawCoordinateSystem();
        drawSetting();
        ((Graphics2D)g).setTransform(af);
    }
    public int mm2pixel(double mm) {
    	return gmm.mm2pixel(mm);
    }
    public double pixel2mm(int pixel) {
    	return gmm.pixel2mm(pixel);
    }
    private void drawSetting() {
        window.draw(gmm);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prefWidth, prefHeight);
    }
    public void zoomIn() {
        gmm.setScale(gmm.getScale() * 1.1d);
        repaint();
    }

    public void zoomOut() {
        gmm.setScale(gmm.getScale() / 1.1d);
        repaint();        
    }

    public void zoom100() {
        gmm.setScale(1d);
        repaint();        
    }
    public void setZoom(double zoom) {
    	gmm.setScale(zoom);
    	repaint();
    }
    public void setCenter(double x, double y) {
    	gmm.setCenter(x, y);
    	repaint();
    }
    public void addCursorListener(CursorListener cl) {
        cursorMulticaster.add(cl);
    }
    public void removeCursorListener(CursorListener cl) {
        cursorMulticaster.remove(cl);
    }
    public void moveView(int dx, int dy) {
        gmm.drag(dx, dy);
        repaint();
    }

    void setGridColor(Color color) {
        gmm.setGridColor(color);
        repaint();
    }

    Color getGridColor() {
        return gmm.getGridColor();
    }

    void setHeightMM(double height) {
        prefHeight = gmm.mm2pixel(height);
    }

    void setWidthMM(double width) {
        prefWidth = gmm.mm2pixel(width);        
    }
    double getHeightMM() {
        return gmm.pixel2mm(prefHeight);
    }

    double getWidthMM() {
    	return gmm.pixel2mm(prefWidth);        
    }
    void setGridWidth(double gridwidth) {
        gmm.setGridWidth(gridwidth);
        repaint();
    }

    double getGridWidth() {
        return gmm.getGridWidth();
    }

    void setGridVisible(boolean b) {
        gmm.setGridVisible(b);
        repaint();
    }
    boolean getGridVisible() {
        return gmm.getGridVisible();
    }
    protected class DataListener implements FigureListener {
        @Override
        public void dataChanged() {
            repaint();
        }
    }

    
    protected class ViewMouseListener implements MouseMotionListener, MouseListener {
        int x;
        int y;
        @Override
        public void mouseDragged(MouseEvent e) {
        	if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
        		moveView(x - e.getX(), y - e.getY());
        		x = e.getX();
        		y = e.getY();
        	}
        }

        @Override
        public void mouseMoved(MouseEvent e) {  
            cursorMulticaster.fireCursorMoved(
                gmm.pointFromPixel(e.getX(), e.getY())
            );          
        }

        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(MouseEvent e) {
        	if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
        		x = e.getX();
        		y = e.getY();
        	} 
        	if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
        		cursorMulticaster.fireCursorDown(gmm.pointFromPixel(e.getX(), e.getY()));
        	}
        }

        @Override
        public void mouseReleased(MouseEvent e) { 
        	if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
        		cursorMulticaster.fireCursorUp(gmm.pointFromPixel(e.getX(), e.getY()));
        	}        	
        }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }
    protected class CursorMulticaster {
        private final LinkedList<CursorListener> cursorListener;
        protected CursorMulticaster() {
            cursorListener = new LinkedList<>();
        }
        void add(CursorListener cl) {
            cursorListener.add(cl);
        }
        void remove(CursorListener cl) {
            cursorListener.remove(cl);
        }
        void fireCursorMoved(Point p) {
            cursorListener.forEach((cl) -> {
                cl.cursorMoved(p);
            });
        }
        void fireCursorDown(Point p) {
            cursorListener.forEach((cl) -> {
                cl.cursorUp(p);
            });
        }
        void fireCursorUp(Point p) {
            cursorListener.forEach((cl) -> {
                cl.cursorDown(p);
            });
        }
    }
    protected class ResizeListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            gmm.setSize(getWidth(), getHeight());
            prefHeight = getWidth();
            prefWidth = getHeight();
            repaint();
        }

        @Override
        public void componentMoved(ComponentEvent e) { }

        @Override
        public void componentShown(ComponentEvent e) { }

        @Override
        public void componentHidden(ComponentEvent e) { }
        
    }
}
