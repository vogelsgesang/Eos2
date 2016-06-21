package de.lathanda.eos.robot;

/**
 * x/y Koordinate in der Welt
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Coordinate implements Comparable<Coordinate> {
    private final int x;
    private final int y;
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Coordinate o) {
        if (o.x != x) {
            return o.x - x;
        } else {
            return o.y - y;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}
