package de.lathanda.eos.robot;

/**
 * Ein Eingang durch den ein Roboter die Welt betreten kann.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Entrance {
    public final int x;
    public final int y;
    public final int z;
    public Direction d;
   
    public Entrance(int x, int y, int z, Direction d) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.d = d;
    }

	public void rotate() {
		d = d.getRight();		
	}  
}
