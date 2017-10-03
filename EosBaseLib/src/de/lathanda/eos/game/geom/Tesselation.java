package de.lathanda.eos.game.geom;

import java.util.Collection;
import java.util.LinkedList;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.game.geom.tesselation.BorderWalkTesselation;
import de.lathanda.eos.game.geom.tesselation.TesselationFailedException;

public interface Tesselation {
	public void addVertice(double x, double y);
	public default void addVertices(Collection<? extends Point> points) {
		for (Point p : points) {
			addVertice(p.getX(), p.getY());
		}
	};
	public Collection<Triangle> getTriangles() throws TesselationFailedException;
	public LinkedList<? extends Point> getOuterBorder() throws TesselationFailedException;;
	public static Tesselation getDefaultTesselation() {
		return new BorderWalkTesselation();
		//return new NoTesselationAvailable();
	}
	public static class NoTesselationAvailable implements Tesselation {

		@Override
		public void addVertice(double x, double y) {}

		@Override
		public Collection<Triangle> getTriangles() throws TesselationFailedException {
			throw new TesselationFailedException();			
		}

		@Override
		public LinkedList<? extends Point> getOuterBorder() throws TesselationFailedException {
			throw new TesselationFailedException();			
		}		
	}
}
