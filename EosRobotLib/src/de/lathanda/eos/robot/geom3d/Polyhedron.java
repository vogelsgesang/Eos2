package de.lathanda.eos.robot.geom3d;

import java.util.LinkedList;

/**
 * Polyeder.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Polyhedron {
    public final LinkedList<Face> faces = new LinkedList<>();
    public Polyhedron() {
    }
}
