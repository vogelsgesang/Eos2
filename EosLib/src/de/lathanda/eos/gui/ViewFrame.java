package de.lathanda.eos.gui;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.geo.Window;
import de.lathanda.eos.gui.event.CursorListener;
import de.lathanda.eos.util.GuiToolkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;


/**
 * Fenster zum Anzeigen der Eos-Figuren.
 *
 * @author Peter (Lathanda) Schneider
 */
public class ViewFrame extends JFrame implements WindowListener {
    private static final long serialVersionUID = 8808554555627848478L;
    private static final ResourceBundle VIEW   = ResourceBundle.getBundle("text.view");
    private static final Image     LOGO        = ResourceLoader.loadImage("icon/eos.png");
    private ViewPanel view;
    private final JToolBar toolbar;
    private final JButton zoomIn;
    private final JButton zoomOut;
    private final JButton zoom100;
    private final JLabel  status;
    
    public ViewFrame(Window window) {
        super(VIEW.getString("View.Title"));
        this.setIconImage(LOGO);
        view    = new ViewPanel(window);
        toolbar = new JToolBar();
        zoomIn  = GuiToolkit.createButton("icons/document_zoom_in.png", null, ae -> view.zoomIn());
        zoomOut = GuiToolkit.createButton("icons/document_view.png", null, ae -> view.zoom100());
        zoom100 = GuiToolkit.createButton("icons/document_zoom_out.png", null, ae -> view.zoomOut());
        status  = GuiToolkit.createLabel(" ");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        view.setFocusable(false);

        toolbar.setFloatable(false);
        toolbar.add(zoomIn);
        toolbar.add(zoom100);
        toolbar.add(zoomOut);

        getContentPane().add(view,    BorderLayout.CENTER);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(status,  BorderLayout.SOUTH);

        addKeyListener(this.new ViewKeyListener());
        view.addCursorListener(this. new ViewListener());
        
        pack();
        this.setLocation(
        		Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth(), 
        		0
        );
    }

    public void setGridColor(Color color) {
        view.setGridColor(color);
    }

    public Color getGridColor() {
        return view.getGridColor();
    }

    public void setBackgroundColor(Color color) {
        view.setBackground(color);
    }

    public Color getBackgroundColor() {
        return view.getBackground();
    }

    public void setHeightMM(double height) {
        view.setHeightMM(height);
        pack();
    }

    public void setWidthMM(double width) {
        view.setWidthMM(width);
        pack();
    }
    public double getHeightMM() {
        return view.getHeightMM();
    }

    public double getWidthMM() {
    	return view.getWidthMM();
    }
    public void setTop(double top) {
        Rectangle r = getBounds();
        r.y = view.mm2pixel(top);
        this.setBounds(r);
    }

    public double getTop() {
        Rectangle r = getBounds();
        return view.pixel2mm(r.y);
    }

    public void setLeft(double left) {
        Rectangle r = getBounds();
        r.x = view.mm2pixel(left);
        this.setBounds(r);
    }

    public double getLeft() {
        Rectangle r = getBounds();
        return view.pixel2mm(r.x);    }

    public void setGridWidth(double gridwidth) {
        view.setGridWidth(gridwidth);
    }

    public double getGridWidth() {
        return view.getGridWidth();
    }

    public void move(double dx, double dy) {
        Rectangle r = getBounds();
        r.x += view.mm2pixel(dx);
        r.y += view.mm2pixel(dy);
        this.setBounds(r);
    }
    public void setCenter(double x, double y) {
    	view.setCenter(x, y);    	
    }
    public void setZoom(double zoom) {
    	view.setZoom(zoom);
    }
    public void setGridVisible(boolean b) {
        view.setGridVisible(b);
    }
    public boolean getGridVisible() {
        return view.getGridVisible();
    }
    private class ViewKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                case '+':
                    view.zoomIn();
                    break;
                case '-':
                    view.zoomOut();
                    break;
                case '=':
                    view.zoom100();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    private class ViewListener implements CursorListener {
        Point p;
        @Override
        public void cursorMoved(Point p) {
            this.p = p;
            update();        
        }

        private void update() {
            MessageFormat msg;
            if (p != null) {
                msg = new MessageFormat(VIEW.getString("View.Status.Cursor1"));
            } else {
                msg = new MessageFormat(VIEW.getString("View.Status.Cursor2"));
            }
            status.setText(msg.format(new Object[]{p.getX(), p.getY()}));
        }        
    }
	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		setState(ICONIFIED);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}

