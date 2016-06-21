package de.lathanda.eos.base.layout;

/**
 * \brief Ausmaße
 * 
 * Breite und Höhe von geometrischen Objekten. 
 *
 * @author Peter (Lathanda) Schneider
 */
public class Dimension {
    private double width;
    private double height;
    public Dimension() {
        width  = 0;
        height = 0;
    }

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    
}
