package de.lathanda.eos.game.geom.tesselation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.game.geom.BoundingBox;
import de.lathanda.eos.game.geom.Triangle;
import de.lathanda.eos.game.geom.Tesselation;
import de.lathanda.eos.util.IntervalTree;
/**
 * \brief Tesselation
 * 
 * Diese Klasse dient dazu aus einem Polygonzug die äußere Grenze und eine zum Ausfüllen 
 * geeignete Menge von Dreieck zu berechnen.
 * Die äußere Grenze wird wird im Uhrzeigersinn angegeben, ausgehend vom linken Rand.
 * Aufgrund numerischer Effekte muss ein Kompromiss bei der Genauigkeit eingegangen werden.
 * Die Punkte des berechneten Randpolygons haben nur eine Genauigkeit von etwa 7 Stellen.
 * Spalte innerhalb des Polygons, welche unterhalb dieser Genauigkeit liegen, können verschwinden
 * oder auch nicht, je nachdem wie die Rundungsfehler zuschlagen.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class BorderWalkTesselation implements Tesselation {
	//original data
	private LinkedList<Vertice> v = new LinkedList<>() ;
	//simple concave polygon representing outer boarder
	private LinkedList<Vertice> p = new LinkedList<>();
	//final triangulation
	private LinkedList<Triangle> t = new LinkedList<>();
	//array of all incoming edges
	private ArrayList<Edge> edges;
	//dirty?
	private boolean triangleReady = false;
	private boolean borderReady   = false;
	public BorderWalkTesselation() {
	}
	public BorderWalkTesselation(double[] x, double[] y) {
		if (x.length != y.length) throw new ArrayIndexOutOfBoundsException();
		for(int i = 0; i < x.length; i++) {
			v.add(new Vertice(x[i], y[i]));
		}
	}
	public BorderWalkTesselation(Collection<? extends Point> points) {
		for(Point p : points) {
			v.add(new Vertice(p.getX(), p.getY()));
		}
	}
	public BorderWalkTesselation(Collection<? extends Point> points, double raster) {
		for(Point p : points) {
			v.add(new Vertice(((int)(raster*p.getX()))/raster, ((int)(raster*p.getY()))/raster));
		}
	}
	public void addVertice(double x, double y) {
		v.add(new Vertice(x, y));
		triangleReady = false;
		borderReady   = false;
	}
	public void tesselate() throws TesselationFailedException {
		p.clear();
		t.clear();
		switch (v.size()) {
		case 0:
			break;
		case 1:
		case 2:
			p.addAll(v);
			break;
		default:
			Edge start = createEdges();    //v -> edges
			convertToSimplePolygon(start); //edges -> p
			earCut();                      //p -> t
		}
		triangleReady = true;
		borderReady   = true;
	}
	public void calculateBorder() throws TesselationFailedException {
		p.clear();
		t.clear();
		switch (v.size()) {
		case 0:
			break;
		case 1:
		case 2:
			p.addAll(v);
			break;
		default:
			Edge start = createEdges();    //v -> edges
			convertToSimplePolygon(start); //edges -> p
	
		}
		borderReady   = true;
	}
	public Collection<Triangle> getTriangles() throws TesselationFailedException {
		if (!triangleReady && ! borderReady ) { 
			tesselate(); 
		} else if (!triangleReady && borderReady) {
			earCut();
		}
		return t;
	}

	@Override
	public LinkedList<? extends Point> getOuterBorder() throws TesselationFailedException {
		if (!borderReady) {
			calculateBorder();
		}
		return p;
	}

	public LinkedList<? extends Point> getPolygon() {
		return v;
	}
	private void convertToSimplePolygon(Edge start) throws TesselationFailedException {		
		//prepare search structure
		IntervalTree<BoundingBox<Edge>.xInterval> xRange = new IntervalTree<>();
		IntervalTree<BoundingBox<Edge>.yInterval> yRange = new IntervalTree<>();
		for (Edge e : edges) {
			xRange.insert(e.b.new xInterval());
			yRange.insert(e.b.new yInterval());
		}
		// --- run clockwise searching intersections changing edge ---
		Edge act = start;
		int failed = v.size() * 4; //counter prevents unlimited loop caused by numerical error
		do {
			if (failed -- == 0) { // calculation failed
				throw new TesselationFailedException();
			}
			// check for potential crossing edges using bounding boxes
			TreeSet<BoundingBox<Edge>.xInterval> xHits = new TreeSet<>();
			TreeSet<BoundingBox<Edge>.yInterval> yHits = new TreeSet<>();
			xRange.seek(act.b.new xInterval(), xHits); //the result is sorted by natural edge order!
			yRange.seek(act.b.new yInterval(), yHits);
			// as the sets are sorted, we can easily check for items that are in both sets 
			TreeSet<Edge> hits = new TreeSet<>();		
			BoundingBox<Edge>.xInterval xInt = xHits.pollFirst();
			BoundingBox<Edge>.yInterval yInt = yHits.pollFirst();
			while (xInt != null && yInt != null) {
				if (xInt.getSource() == yInt.getSource()) {
					//bounding boxes overlap
					hits.add(xInt.getSource());
					xInt = xHits.pollFirst();
					yInt = yHits.pollFirst();
				} else if ( xInt.getSource().compareTo(yInt.getSource()) < 0) {
					xInt = xHits.pollFirst();
				} else {
					yInt = yHits.pollFirst();
				}
			}
			// check all potential crossing edges, if they actually do intersect
			CrossingPoint best = null;
			for (Edge e : hits) {
				if (e.id == act.id) {
					continue; //ignore intersection with identical edge
				}
				CrossingPoint inter = act.getIntersection(e, edges);
				//edges not chosen in the previous step are ignored (same starting point)
				if (inter != null && (inter.edge.x1 != act.x1 || inter.edge.y1 != act.y1)) {
					if (best != null) {
						//remark: mathematically inter.angle > 2 is equal to act.v.crossproduct(inter.edge.v) > 0
						if (inter.lambda < best.lambda && act.v.crossproduct(inter.edge.v) > 0) {
							//better lambda 
							best = inter; 
						} else {
							if (inter.lambda == best.lambda) {
								//same crossing point determine better
								if (best.angle < inter.angle) {
									//better angle
									best = inter;
								} else if (best.angle == inter.angle) {
									//same angle choose longer
									if (best.edge.v.getSquareLength() < inter.edge.v.getSquareLength()) {
										best = inter;
									}
								}
							}
						}
					} else {
						if (inter.lambda == 1.0 || act.v.crossproduct(inter.edge.v) > 0) {
							best = inter;
						}
					}
				}
			}	

			act = best.edge;
			p.add(new Vertice(act.x1, act.y1));
		} while (act.id != start.id);
		p.removeLast();
	}
	private void earCut() throws TesselationFailedException {
		//determine concave points and link points
		Vertice prev = p.getLast();
		Vertice[] last = new Vertice[] {prev}; //call by ref parameter
		for(Vertice a : p) {
			a.linkPredecessor(prev);
			prev = a;			
		}
		LinkedList<Vertice> concaves = new LinkedList<>();
		for(Vertice a: p) {
			if (a.isConcave()) {
				concaves.add(a);
			}
		}
		boolean ok = true;
		while (!concaves.isEmpty() && ok) {
			ok = false;
			for(Iterator<Vertice> i = concaves.iterator(); i.hasNext(); ) {
				Vertice v = i.next();
				if (v.suc == null || !v.isConcave()) {
					i.remove();
					ok = true;
				} else {
					if (v.pre.cutEmpty(last)) {
						ok = true;
					} else if (v.suc.cutEmpty(last)) {
						ok = true;
					} else if (v.pre.isEar(concaves)) {
						t.add(v.pre.cutEar(last));
						ok = true;
					} else if (v.suc.isEar(concaves)) {
						t.add(v.suc.cutEar(last));
						ok = true;
					}
				}
			}
		}
		if (!ok) {
			throw new TesselationFailedException();
		}
		while (last[0].pre.pre != last[0]) { //no triangle
			t.add(last[0].cutEar(last));
		}
	}
	/**
	 * Erzeugt Kanten
	 * @param edges
	 * @return Anfangskante
	 */
	private Edge createEdges() {
		edges = new ArrayList<>();
		// fill edges
		Vertice v1 = v.get(v.size() - 1);
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		for(Vertice v2 : v) {
			if (v1.isDifferent(v2)) {
				edges.add(new Edge(v1, v2, edges.size()));
				v1 = v2;
				if (v2.getX() < minX) {
					minX = v2.getX();
					minY = v2.getY();
				}
			}
		}
		Edge start = new Edge(minX, minY, edges.size());
		edges.add(start);
		return start;
	}
	private static class Vertice extends Point {
		private Vector v;
		private Vertice pre;
		private Vertice suc;
		public Vertice(double x, double y) {
			super(x,y);
		}
		public boolean isDifferent(Vertice v2) {
			return x != v2.x || y != v2.y;
		}
		public void linkPredecessor(Vertice predecessor) {
			this.pre = predecessor;
			predecessor.suc = this;
			v = new Vector(pre, this);
		}
		public Triangle cutEar(Vertice[] last) {
			if (this == last[0]) {
				last[0] = pre;
			}
			Triangle t = new Triangle(pre.x, pre.y, x, y, suc.x, suc.y);
			suc.linkPredecessor(pre);
			pre = null;
			suc = null;			
			return t; 
		}
		public boolean cutEmpty(Vertice[] last) {
			if (v.crossproduct(suc.v) != 0) {
				if (this == last[0]) {
					last[0] = pre;
				}
				suc.linkPredecessor(pre);
				pre = null;
				suc = null;
				return true;
			} else {
				return false;
			}
		}
		public boolean isEar(Vertice p) {
			Vector cv = new Vector(pre.x - suc.x, pre.y - suc.y);
			Vector a = new Vector(this, p);
			Vector b = new Vector(suc, p);
			Vector c = new Vector(pre, p);
			return suc.v.crossproduct(a) >= 0 || cv.crossproduct(b) >= 0 || v.crossproduct(c) >= 0;
		}
		public boolean isEar(Collection<Vertice> c) {
			if (isConcave()) return false;
			for(Vertice v : c) {
				if (!isEar(v)) {
					return false;
				}
			}
			return true;			
		}
		public boolean isConcave() {
			return v.crossproduct(suc.v) > 0;
		}
		@Override
		public String toString() {
			return super.toString();
		}		
	}
	private static class Edge implements Comparable<Edge> {
		/** Anfangspunkt */
		private final double x1, y1;
		/** Endpunkt */
		private final double x2, y2;
		/** Index der ursprünglichen Kante */
		private final int id;
		/** Richtung der Kante */
		private final Vector v;
		/** Boundingbox der Kante*/
		private final BoundingBox<Edge> b;
		/** Winkel der Kante (Maximum Metrik)*/
		private final double angle;
		/**
		 * Anfangskante von links
		 * @param x x Koordinate des linkesten Punkts des Polygons
		 * @param y y Koordinate des linkesten Punkts des Polygons
		 * @param id Index der ursprünglichen Kante
		 */
		public Edge(double x, double y, int id) {
			this.x1 = Double.NEGATIVE_INFINITY;
			this.y1 = round(y);
			this.x2 = round(x);
			this.y2 = round(y);
			this.b = new BoundingBox<>(x2, y2, x2, y2, this);
			this.id = id;
			this.v = new Vector(1, 0);
			this.angle = v.getAngleOrdernumber();
		}
		public Edge(double x1, double y1, double x2, double y2, int id) {
			this.x1 = round(x1);
			this.y1 = round(y1);
			this.x2 = round(x2);
			this.y2 = round(y2);
			this.b = new BoundingBox<>(this.x1, this.y1, this.x2, this.y2, this);
			this.id = id;
			this.v = new Vector(this.x2 - this.x1, this.y2 - this.y1);
			this.angle = v.getAngleOrdernumber();
		}
		public Edge(Vertice v1, Vertice v2, int id) {
			this(v1.getX(), v1.getY(), v2.getX(), v2.getY(), id);
		}
		private final static double HALF = Double.longBitsToDouble(0x0000000000080000l);
		private static double round(double a) {
			//the following line will floor 20 bits to 0
			//this is approximately a loose of precision of 6 digits
			return Double.longBitsToDouble(Double.doubleToRawLongBits(a + HALF) & 0xFFFFFFFFFFF00000l);
		}
		
		/**
		 * Umgedrehte Kante
		 * @param e
		 */
		public Edge(Edge e) {
			this.x1 = e.x2;
			this.y1 = e.y2;
			this.x2 = e.x1;
			this.y2 = e.y1;
			this.b  = e.b;
			this.id = e.id;
			this.v = e.v.negative();
			this.angle = (e.angle >= 2)?e.angle - 2:e.angle + 2;
		}

	    public CrossingPoint getIntersection(Edge b, ArrayList<Edge> edges) {
	    	/* preface:
	    	 * In order to calculate crossing points, we do calculate lambda or my.
	    	 * We can do this using x or y.
	    	 * In algebra the choice doesn't matter.
	    	 * In numerical algebra it does.
	    	 * Depending on our choice the results will differ.
	    	 * We need to keep divisors as big as possible.
	    	 * In order to avoid accumulated errors
	    	 * we will use the original master edges as far as possible.
	    	 * The shortened edge will only be used in the last step.
	    	 */
	    	//check identical point first, this avoids numerical errors
	    	if (x2 == b.x1 && y2 == b.y1) {
	    		// continuous polygon
	    		return new CrossingPoint(this, b, false, true);
	    	}
	    	if (x2 == b.x2 && y2 == b.y2) {
	    		// inverted continuous polygon
	    		return new CrossingPoint(this, b, true, true);	    		
	    	}
	    	if (x1 == b.x1 && y1 == b.y1) {
	    		// alternative edge (will be ignored later)
	    		return new CrossingPoint(this, b, false, false);
	    	}
	    	if (x1 == b.x2 && y1 == b.y2) {
	    		// inverted alternative edge (will be ignored later)
	    		return new CrossingPoint(this, b, true, false);	    		
	    	}
	    	//Lookup original master edges
	    	Edge am, bm;
	    	if (id < b.id) {
	    		am = edges.get(id);
	    		bm = edges.get(b.id);
	    	} else {
	    		am = edges.get(b.id);
	    		bm = edges.get(id);	    		
	    	}
			//Cramer's rule
	    	Vector base = new Vector(bm.x1 - am.x1, bm.y1 - am.y1);
	    	double n = am.v.crossproduct(bm.v);
	    	double za = base.crossproduct(bm.v);			
			if (n == 0) { //parallel
				if (za == 0) {
					//same line
					return CrossingPoint.createCrossingPoint(this, b, x2, y2);
				} else {
					// no intersection
					return null;
				}
			} else {
				double lambdaM = za / n; //lambda original edge
				double lambdaM2 = 1 - lambdaM;
				double px = am.x1 * lambdaM2 + lambdaM * am.x2;
				double py = am.y1 * lambdaM2 + lambdaM * am.y2;
				return CrossingPoint.createCrossingPoint(this, b, round(px), round(py));
			}
	    }	
	    public Edge invert() {
	    	return new Edge(this);
	    }
		@Override
		public int compareTo(Edge o) {
			//return b.getSource().compareTo(o.b.getSource());
			return o.id - id;
		}
		@Override
		public String toString() {
			return MessageFormat.format("id {4,number,#} ({0,number,#.##}/{1,number,#.##}) -({5,number,#.###})-> ({2,number,#.##}/{3,number,#.##})", x1, y1, x2, y2, id, angle);
		}
		
	}	
	private static class CrossingPoint  {
		private final double lambda;
		private final double angle;
		private final Edge edge;
		private CrossingPoint(Edge edge, double lambda, double angle) {
			this.edge = edge;
			this.angle = angle;
			this.lambda = lambda;
		}
		/**
		 * Erzeugt einen Schnittpunkt an den Enden der Kante
		 * @param a Kante die geschnitten wird
		 * @param b scheidende Kante
		 * @param inverted Invertieren?
		 * @param continues Am Ende Anfügen
		 */
		public CrossingPoint(Edge a, Edge b, boolean inverted, boolean continues) {
			if (continues) {
				lambda = 1;
			} else {
				lambda = 0;
			}
			if (inverted) {
				edge = b.invert();				
			} else {
				edge = b;
			}		
			double diffA = 2 + edge.angle - a.angle;
			if (diffA < 0) {
				this.angle = diffA + 4;
			} else if (diffA >= 4) {
				this.angle = diffA - 4;
			} else {
				this.angle = diffA;
			}
		}

		private static CrossingPoint createCrossingPoint(Edge a, Edge b, double px, double py) {
			double lambda, my;
			//use the bigger delta, 
			//this avoids dividing by zero and is less vulnerable to rounding problems
			if (Math.abs(a.x2 - a.x1) > Math.abs(a.y2 - a.y1)) {
				lambda = (px - a.x1) / (a.x2 - a.x1);					
			} else {
				lambda = (py - a.y1) / (a.y2 - a.y1);
			}
			if (Math.abs(b.x2 - b.x1) > Math.abs(b.y2 - b.y1)) {
				my = (px - b.x1) / (b.x2 - b.x1);
			} else {
				my = (py - b.y1) / (b.y2 - b.y1);
			}
			//detected values that are near the corners, and move them
			if (lambda < 0.0000001 && lambda > -0.0000001) {
				lambda = 0;
				px = a.x1;
				py = a.y1;
			} else if (lambda > 0.9999999 && lambda < 1.0000001) {
				lambda = 1;
				px = a.x2;
				py = a.y2;
			}
			if (my < 0.0000001 && my > -0.0000001) {
				my = 0;
			} else if (my > 0.9999999 && my < 1.0000001) {
				my = 1;
			}
			//check if crossing point is within the edge
			if (lambda >= 0 && my >= 0 && lambda <= 1 && my <= 1) {
				//determine the orientation				
				Edge edge;
				if (my == 0) {
					//edge starts at crossing point
					edge = b;
				} else if (my == 1) {
					//edge ends at crossing point
					edge = b.invert();
				} else {
					//edge is on both sides ...
					double c = a.v.crossproduct(b.v);
					//...choose the edge half that turns left
					if (c > 0) {
						edge = new Edge(px, py, b.x2, b.y2, b.id);					
					} else if (c < 0) {
						edge = new Edge(px, py, b.x1, b.y1, b.id);
					} else {
						//...overlapping edges => choose the edge direction that continues
						if (b.v.dotProduct(b.v) > 0) {
							edge = new Edge(px, py, b.x2, b.y2, b.id);
						} else {
							edge = new Edge(px, py, b.x1, b.y1, b.id);
						}
					}
				}
				//determine the crossing angle order
				double diffA = 2 + edge.angle - a.angle;
				double angle;
				if (diffA < 0) {
					angle = diffA + 4;
				} else if (diffA >= 4) {
					angle = diffA - 4;
				} else {
					angle = diffA;
				}
				return new CrossingPoint(edge, lambda, angle);
			} else {
				return null;
			}			
		}
		@Override
		public String toString() {
			return edge + " " + MessageFormat.format("lambda = {0,number,#.##}, angle = {1,number,#.###}", lambda, angle);
		}
		
	}
}
