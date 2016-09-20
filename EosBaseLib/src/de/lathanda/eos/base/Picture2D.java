package de.lathanda.eos.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.base.math.Point;

/**
 * Rendering mit Swing.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Picture2D extends Picture {

    private Graphics2D g;
    private AffineTransform baseTrans;
    //drawing information
    private Transform transform;
    private final LinkedList<Transform> storedTransform;
    private final LinkedList<AffineTransform> storedAffineTransform;
    //Grid
    private double gridwidth = 10;
    private static double GRID_LINE_WIDTH = 0.25;
    private static double AXIS_LINE_WIDTH = 1;
    private final LineDescriptor gridline = new LineDescriptor(new Color(128,128,128), LineStyle.SOLID, GRID_LINE_WIDTH);
    private final LineDescriptor axesline = new LineDescriptor(new Color(128,128,128), LineStyle.SOLID, AXIS_LINE_WIDTH);    
    private boolean gridvisible = true;
    //coordinate transformation
    protected int pCenterX;
    protected int pCenterY;
    private int pWidth;
    private int pHeight;
    private Component target;
    /**
     * Dieser Wert dient als Vergrösserung für alle Parameter an das Graphics Objekt.
     * Dadurch kann der Makel umgangen werde, das Graphics nur int beherrscht.
     */
    private static double ZOOM = 128d;
    public Picture2D(double width, double height, Component target) {
        super();
        this.target = target;
        font = new Font("Serif", Font.PLAIN, 12);
        transform = Transform.ID;
        storedTransform = new LinkedList<>();
        storedAffineTransform = new LinkedList<>();
        setSize(mm2pixel(width), mm2pixel(height));
    }
    /**
     * Renderingziel setzen
     * @param g Renderingziel
     */
    public void setGraphics(Graphics2D g) {
        this.g = g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); 

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);        
        g.translate(pCenterX, pCenterY);
        g.scale(1/ZOOM, 1/ZOOM);
        g.translate(mm2unit(-centerX), mm2unit(centerY));
        baseTrans = g.getTransform();
    }
    /**
     * Größe des Zeichenbereichs festlegen.
     * @param width Breite [Pixel]
     * @param height Höhe [Pixel]
     */
    public final void setSize(int width, int height) {
    	pWidth = width;
    	pHeight = height;
        this.pCenterX   = width/2;
        this.pCenterY   = height/2;
        this.halfwidth  = pixel2mmInternal(pWidth/2);
        this.halfheight = pixel2mmInternal(pHeight/2);
    }
    /**
     * Skalierung festlegen.
     * @param scale Skalierung
     */
    public void setScale(double scale) {
        this.scale      = scale;
        this.halfwidth  = pixel2mmInternal(pWidth/2);
        this.halfheight = pixel2mmInternal(pHeight/2);
    }    
    /**
     * Skalierung abfragen.
     * @return Skalierung
     */
    public double getScale() {
        return scale;
    }
    /**
     * Bildausschnitt verschieben.
     * @param dx x Verschiebung
     * @param dy y Verschiebung
     */
    public void drag(int dx, int dy) {
        centerX += pixel2mmInternal(dx);
        centerY -= pixel2mmInternal(dy);
    }
    /**
     * Pixel in Millimeter umrechnen inklusive Skalierung.
     * @param pixel Pixel
     * @return Millimeter
     */
    private double pixel2mmInternal(int pixel) {
        return pixel / scale / scaleBase;
    }
    /**
     * Pixel in Millimeter umrechnen.
     * @param pixel Pixel
     * @return Millimeter
     */
    public double pixel2mm(int pixel) {
        return pixel / scaleBase;
    }
    /**
     * Millimeter in PIxel umrechnen.
     * @param mm Millimeter
     * @return Pixel
     */
    public int mm2pixel(double mm) {
        return (int)(mm * scaleBase);
    }
    /**
     * Millimeter in Einheit (int) umrechnen.
     * @param mm Millimeter
     * @return Einheit
     */
    private int mm2unit(double mm) {
        return (int)mm2unitd(mm);
    }
    /**
     * Millimeter in Einheit (float) umrechnen.
     * @param mm Millimeter
     * @return Einheit
     */
    private float mm2unitf(double mm) {
        return (float)mm2unitd(mm);
    }    
    /**
     * Millimeter in Einheit (double) umrechnen.
     * @param mm Millimeter
     * @return Einheit
     */
    
    private double mm2unitd(double mm) {
        return mm * scaleBase * scale * ZOOM;
    } 
    /**
     * Pixelkoordinate in Koordinate umrechnen.
     * @param x x Pixel
     * @param y y Pixel
     * @return Koordinate
     */
    public Point pointFromPixel(int x, int y) {
        return new Point(pixel2mmInternal(x - pCenterX) + centerX, centerY - pixel2mmInternal(y - pCenterY));
    }
    
    @Override
    public void drawLine(double x1, double y1, double x2, double y2) {
        applyLine();
        g.drawLine(mm2unit(x1), -mm2unit(y1), mm2unit(x2), -mm2unit(y2));
    }

    @Override
    public void pushTransform() {
        storedTransform.push(transform);
        storedAffineTransform.push(g.getTransform());
    }
    @Override
    public void restoreTransform() {
        g.setTransform(storedAffineTransform.pop());
        transform = storedTransform.pop();
    }
    @Override
    public void applyTransform(Transform tf) {
        transform = transform.transform(tf);
        g.setTransform(baseTrans);
        g.translate(mm2unitd(transform.getdx()), -mm2unitd(transform.getdy()));
        g.rotate(-transform.getAngle()); 
        g.scale(transform.getScale(), transform.getScale());
        if (transform.getMirrorX()) {
            g.scale(-1,1);
        }
    }
    @Override
    public void translate(double dx, double dy) {
        transform.translate(dx, dy);
        g.setTransform(baseTrans);
        g.translate(mm2unitd(transform.getdx()), -mm2unitd(transform.getdy()));
        g.rotate(-transform.getAngle()); 
        g.scale(transform.getScale(), transform.getScale());
        if (transform.getMirrorX()) {
            g.scale(-1,1);
        }        
    }
    @Override
    public void rotate(double angle) {
        transform.rotate(angle);
        g.setTransform(baseTrans);
        g.translate(mm2unitd(transform.getdx()), -mm2unitd(transform.getdy()));
        g.rotate(-transform.getAngle()); 
        g.scale(transform.getScale(), transform.getScale());
        if (transform.getMirrorX()) {
            g.scale(-1,1);
        }        
    }
    /**
     * Liniensil anwenden.
     * @return sichtbar?
     */
    private boolean applyLine() {
        g.setColor(line.getColor());
        switch (line.getLineStyle()) {
        case DASHED:
            g.setStroke(new BasicStroke(
            	mm2unitf(line.getDrawWidth()),
            	BasicStroke.CAP_ROUND,
            	BasicStroke.JOIN_ROUND,
                1f,
                new float[]{mm2unitf(5), mm2unitf(5)},
                0
            ));
            break;
        case SOLID:
            g.setStroke(new BasicStroke(
           		mm2unitf(line.getDrawWidth()),
            	BasicStroke.CAP_ROUND,
            	BasicStroke.JOIN_ROUND
            ));
            break;
        case DOTTED:
            g.setStroke(new BasicStroke(
            	mm2unitf(line.getDrawWidth()),
            	BasicStroke.CAP_ROUND,
            	BasicStroke.JOIN_ROUND,
                1f,
                new float[]{mm2unitf(0.5), mm2unitf(3)},
                0
            ));
            break;
        case DASHED_DOTTED:
            g.setStroke(new BasicStroke(
            	mm2unitf(line.getDrawWidth()),
            	BasicStroke.CAP_ROUND,
            	BasicStroke.JOIN_ROUND,
                1f,
                new float[]{mm2unitf(5), mm2unitf(1), 
                mm2unitf(0.5), mm2unitf(1)},
                0
            ));
            break;
        case INVISIBLE:  
            return false;
        }
        return true;
    }

    @Override
    public void drawPolygon(double[] x, double[] y) {
        int[] xPoints = new int[x.length]; 
        int[] yPoints = new int[y.length]; 
        for (int i = 0; i < x.length; i++) {
            xPoints[i] = mm2unit(x[i]);
            yPoints[i] = -mm2unit(y[i]);
        }
        drawShape(new Polygon(xPoints, yPoints, x.length));
    }

    @Override
    public void drawRect(double x, double y, double width, double height) {
    	drawShape(new Rectangle2D.Double(
                mm2unitd(x),
                -mm2unitd(y+height),
                mm2unitd(width),
                mm2unitd(height)
    		)
    	);    	
    }

    @Override
    public void drawEllipse(double x, double y, double radiusX, double radiusY) {
    	drawShape(new Ellipse2D.Double(
    			mm2unitd(x-radiusX),
                -mm2unitd(y+radiusY),
                2*mm2unitd(radiusX),
                2*mm2unitd(radiusY)
    		)
    	);
    }    

    @Override
    public void drawStringAt(String text, double x, double y) {
    	applyLine();
        g.setFont(font.deriveFont((float)(font.getSize()*scale*ZOOM)));
        g.drawString(text, mm2unit(x), -mm2unit(y));
    }
    
    @Override
    protected double getStringWidth(String text) {  	
        return pixel2mm(target.getFontMetrics(font).stringWidth(text));
    }

    @Override
    protected double getStringHeight() {
        return pixel2mm(target.getFontMetrics(font).getHeight());
    }

    @Override
    protected double getStringDescent() {
        return pixel2mm(target.getFontMetrics(font).getDescent());
    }
    //********
    //* Grid *
    //********
    /**
     * Koordinatensystem zeichnen.
     */
    public void drawCoordinateSystem() {
        if (!gridvisible) return;
        setLine(gridline);
        line.setDrawWidth(GRID_LINE_WIDTH / scale);
        for(double x = 0; x < getMaxX(); x += gridwidth) {
            drawLine(x, getMinY(), x, getMaxY());
        }
        for(double x = 0; x > getMinX(); x -= gridwidth) {
            drawLine(x, getMinY(), x, getMaxY());
        }
        for(double y = 0; y < getMaxY(); y += gridwidth) {
            drawLine(getMinX(), y, getMaxX(), y);
        }
        for(double y = 0; y > getMinY(); y -= gridwidth) {
            drawLine(getMinX(), y, getMaxX(), y);
        }
        setLine(axesline);
        line.setDrawWidth(AXIS_LINE_WIDTH / scale);
        drawLine(0, getMinY(), 0, getMaxY());
        drawLine(getMinX(), 0, getMaxX(), 0);
    }    
    /**
     * Farbe des Koordiantensystems festlegen.
     * @param color Farbe
     */
    public void setGridColor(Color color) {
        gridline.setColor(color);
        axesline.setColor(color);
    }
    /**
     * Farbe des Koordinatensystems abfragen.
     * @return
     */
    public Color getGridColor() {
        return gridline.getColor();
    }
    /**
     * Koordinatensystem Breite festlegen.
     * @param gridwidth Breite
     */
    public void setGridWidth(double gridwidth) {
        this.gridwidth = gridwidth;
    }
    /**
     * Koordinatensystem Breite abfragen.
     * @return Breite
     */
    public double getGridWidth() {
        return gridwidth;
    }
    /**
     * Koordiantensystem Sichtbarkeit festlegen.
     * @param b sichtbar?
     */
    public void setGridVisible(boolean b) {
        gridvisible = b;
    }
    /**
     * Koordiantensystem Sichtbarkeit abfragen.
     * @return sichtbar?
     */
    public boolean getGridVisible() {
        return gridvisible;
    }
	@Override
	public void drawImage(Image image, double x, double y, double width, double height) {
        g.drawImage(
        		image.getImage(),
        		mm2unit(x), 
        		-mm2unit(y+height), 
        		mm2unit(x+width),
                -mm2unit(y),
                image.getImageX(),
                image.getImageY(),
                image.getImageX()+image.getImageWidth(),
                image.getImageX()+image.getImageHeight(),
                null
            );	
	}
	@Override
	public void drawImage(Image image, double x, double y, double width, double height, Scaling scale) {
		if (scale == Scaling.STRETCH) {
	        g.drawImage(
	        	image.getImage(),
	        	mm2unit(x), 
	        	-mm2unit(y+height), 
	        	mm2unit(x+width),
	        	-mm2unit(y),
	        	image.getImageX(),
	        	image.getImageY(),
	        	image.getImageX()+image.getImageWidth(),
	        	image.getImageY()+image.getImageHeight(),
	        	null
	        );	
		} else {
			double imageProportion = (double)image.getImageWidth() / (double)image.getImageHeight();
			double targetProportion = width / height;
			double s = imageProportion / targetProportion;
			if (scale == Scaling.FIT) {
				if (s > 1d) {
					//image wide, adjust height
			        g.drawImage(
				        	image.getImage(),
				        	mm2unit(x), 
				        	-mm2unit(y+height*(0.5+0.5/s)), 
				        	mm2unit(x+width),
				        	-mm2unit(y+height*(0.5-0.5/s)),
				        	image.getImageX(),
				        	image.getImageY(),
				        	image.getImageX()+image.getImageWidth(),
				        	image.getImageY()+image.getImageHeight(),
				        	null
				        );						
				} else {
			        g.drawImage(
				        	image.getImage(),
				        	mm2unit(x+width*(0.5-s/2)), 
				        	-mm2unit(y+height), 
				        	mm2unit(x+width*(0.5+s/2)),
				        	-mm2unit(y),
				        	image.getImageX(),
				        	image.getImageY(),
				        	image.getImageX()+image.getImageWidth(),
				        	image.getImageY()+image.getImageHeight(),
				        	null
				        );					
				}
			} else if (scale == Scaling.CUT) {
				if (s > 1) {
					g.drawImage(
			        	image.getImage(),
			        	mm2unit(x), 
			        	-mm2unit(y+height), 
			        	mm2unit(x+width),
			        	-mm2unit(y),
			        	image.getImageX()+(int)(image.getImageWidth()*(0.5-0.5/s)),
			        	image.getImageY(),
			        	image.getImageX()+(int)(image.getImageWidth()*(0.5+0.5/s)),
			        	image.getImageY()+image.getImageHeight(),
			        	null
					);				
				} else {
					g.drawImage(
			        	image.getImage(),
			        	mm2unit(x), 
			        	-mm2unit(y+height), 
			        	mm2unit(x+width),
			        	-mm2unit(y),
			        	image.getImageX(),
			        	image.getImageY()+(int)(image.getImageHeight()*(0.5-s/2)),
			        	image.getImageX()+image.getImageWidth(),
			        	image.getImageY()+(int)(image.getImageHeight()*(0.5+s/2)),
			        	null
					);				
				}
			}				
		}
	}

	@Override
	public void drawImage(Image image, double x, double y, double width, double height, boolean mirror,
			double angle) {
		AffineTransform at = g.getTransform();
		g.translate(mm2unit(x+width/2), -mm2unit(y+height/2));
		g.rotate(angle);
		g.scale(-1, 1);
        g.drawImage(
        		image.getImage(),
        		mm2unit(-width/2), 
        		-mm2unit(-height/2), 
        		mm2unit(width/2),
                -mm2unit(height/2),
                image.getImageX(),
                image.getImageY(),
                image.getImageX()+image.getImageWidth(),
                image.getImageX()+image.getImageHeight(),
                null
            );
        g.setTransform(at);
	}
	/**
	 * Zeichenstile wiederherstellen.
	 */
	public void restoreStyles() {
		line = new LineDescriptor();
		fill = new FillDescriptor();
	}	
	/**
	 * Form zeichnen.
	 * @param shape Form
	 */
	private void drawShape(Shape shape) {
		//fill
    	g.setColor(fill.getColor());
        g.setPaint(fill.getColor()); 
        switch (fill.getFillStyle()) {
        case FILLED:
            g.fill(shape);
            break;
        case RULED:
        {
        	Shape old = g.getClip();
        	double linegap = mm2unitd(2);
            g.setClip(shape);
            g.setStroke(new BasicStroke(mm2unitf(0.25f)));
            Rectangle2D border = shape.getBounds2D();
            for( double y = border.getMinY() + border.getWidth() % linegap; y < border.getMaxY(); y += linegap ) {
            	g.drawLine((int)border.getMinX(), (int)y, (int)border.getMaxX(), (int)y);
            }
            g.setClip(old);
            break;
        }            
        case CHECKED: 
        {
        	Shape old = g.getClip();
        	double checksize = mm2unitd(5);
            g.setClip(shape);
            g.setStroke(new BasicStroke(mm2unitf(0.25f)));
            Rectangle2D border = shape.getBounds2D();
            for( double y = border.getMinY() + border.getHeight() % checksize; y < border.getMaxY(); y += checksize ) {
            	g.drawLine((int)border.getMinX(), (int)y, (int)border.getMaxX(), (int)y);
            }
            for( double x = border.getMinX() + border.getWidth() % checksize; x < border.getMaxX(); x += checksize ) {
            	g.drawLine((int)x, (int)border.getMinY(), (int)x, (int)border.getMaxY());
            }            
            g.setClip(old);
            break;
        }
        case TRANSPARENT:
        	//invisible
        }
        //draw line
        if(applyLine()) {
        	g.draw(shape);
        }
	}
}
