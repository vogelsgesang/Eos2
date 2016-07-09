package de.lathanda.eos.gui;

import static de.lathanda.eos.base.ResourceLoader.*;
import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.geo.Window;
import de.lathanda.eos.gui.event.CursorListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;


/**
 * Fenster zum Anzeigen der Eos-Figuren.
 *
 * @author Peter (Lathanda) Schneider
 */
public class ViewFrame extends JFrame {
    private static final long serialVersionUID = 8808554555627848478L;
    private static final ResourceBundle VIEW   = ResourceBundle.getBundle("text.view");
    private static final ImageIcon ZOOM_IN     = loadIcon(VIEW.getString("View.Icon.ZoomIn"));
    private static final ImageIcon ZOOM_OUT    = loadIcon(VIEW.getString("View.Icon.ZoomOut"));
    private static final ImageIcon ZOOM_100    = loadIcon(VIEW.getString("View.Icon.Zoom100"));
    private static final Image     LOGO        = loadImage(VIEW.getString("View.Icon.Logo"));
    
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
        zoomIn  = new JButton(ZOOM_IN);
        zoomOut = new JButton(ZOOM_OUT);
        zoom100 = new JButton(ZOOM_100);
        status  = new JLabel(" ");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        view.setFocusable(false);

        toolbar.setFloatable(false);
        toolbar.add(zoomIn);
        toolbar.add(zoom100);
        toolbar.add(zoomOut);

        zoomIn.addActionListener(ae -> view.zoomIn());
        zoom100.addActionListener(ae -> view.zoom100());
        zoomOut.addActionListener(ae -> view.zoomOut());

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
}
