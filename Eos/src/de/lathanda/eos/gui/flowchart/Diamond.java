package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.gui.diagram.Drawing;

/**
 * Rautensymbol.
 * Es hat vier Verbindungsstellen.<br>
 * 0 links<br>
 * 1 oben<br>
 * 2 rechts<br>
 * 3 unten<br>
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Diamond extends ConnectedUnit {
    private float textx;
    private float texty;
    private final String label;
    private final float[] borderX = new float[4];
    private final float[] borderY = new float[4]; 
    
    public Diamond(String label) {
        this.label = label;
        font = STANDARD_FONT;
    }
    @Override
	public void drawUnit(Drawing d) {
        d.drawPolygon(borderX, borderY, 4);
        d.drawString(label, textx, texty);
    }

    @Override
	public void layoutUnit(Drawing d) {
        float labelwidth = d.stringWidth(label);
        
        width = (labelwidth + 2 * BORDER) * 1.5f;
        height = (2*d.getHeight()) + 2 * BORDER;
        textx = 0.5f * (width - labelwidth);
        texty = BORDER + d.getHeight()/2 + d.getAscent();
        borderX[0] = 0;
        borderX[1] = width / 2;
        borderX[2] = width;
        borderX[3] = width / 2;
        borderY[0] = height / 2;
        borderY[1] = 0;
        borderY[2] = height / 2;
        borderY[3] = height;    
    }
    float getX(int nr) {
        return borderX[nr] + getOffsetX();
    }
    float getY(int nr) {
        return borderY[nr] + getOffsetY();
    }
}
