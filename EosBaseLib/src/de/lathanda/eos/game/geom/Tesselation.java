package de.lathanda.eos.game.geom;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
import de.lathanda.eos.util.IntervalTree;
/**
 * \brief Tesselation
 * 
 * Diese Klasse dient dazu aus einem Polygonzug die äußere Grenze und eine zum Ausfüllen 
 * geeignete Menge von Dreieck zu berechnen.
 * Die äußere Grenze wird wird im Uhrzeigersinn angegeben, ausgehend vom linken Rand.
 * @author Peter (Lathanda) Schneider
 *
 */
public class Tesselation {
	//original data
	private LinkedList<Vertice> v = new LinkedList<>() ;
	//simple concave polygon representing outer boarder
	private LinkedList<Vertice> p = new LinkedList<>();
	//final triangulation
	private LinkedList<Triangle> t = new LinkedList<>();
	//array of all incoming edges
	private Edge[] edges;
	public Tesselation() {
	}
	public Tesselation(double[] x, double[] y) {
		if (x.length != y.length) throw new ArrayIndexOutOfBoundsException();
		for(int i = 0; i < x.length; i++) {
			v.add(new Vertice(x[i], y[i]));
		}
	}
	public Tesselation(Collection<? extends Point> points) {
		for(Point p : points) {
			v.add(new Vertice(p.getX(), p.getY()));
		}
	}
	public void addVertice(double x, double y) {
		v.add(new Vertice(x, y));
	}
	public void tesselate() {
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
	}
	public Collection<Triangle> getTriangles() {
		return t;
	}
	public LinkedList<? extends Point> getBorder() {
		return p;
	}
	public LinkedList<? extends Point> getPolygon() {
		return v;
	}
	private void convertToSimplePolygon(Edge start) {		

		//prepare search structure
		IntervalTree<BoundingBox<Edge>.xInterval> xRange = new IntervalTree<>();
		IntervalTree<BoundingBox<Edge>.yInterval> yRange = new IntervalTree<>();
		for (Edge e : edges) {
			xRange.insert(e.b.new xInterval());
			yRange.insert(e.b.new yInterval());
		}
		// --- run clockwise searching insections changing edge ---
		Edge act = start;
		do {
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
			// check all potential crossing edge if they actually do intersect
			CrossingPoint best = null;
			for (Edge e : hits) {
				if (e.id == act.id) continue; //ignore the self intersection
				CrossingPoint inter = act.getIntersection(e, edges);
				//lambda = 0 is the origin and no new crossing, for numerical reasons we check epsilon
				if (inter != null && inter.lambda > 0) {
					if (best != null) {
						if (
							inter.lambda < best.lambda && inter.angle > 2 || 
							inter.lambda == best.lambda && inter.angle > best.angle 
						)
						best = inter;
					} else {
						if (inter.angle > 2 || inter.lambda == 1.0) {
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
	private void earCut() {
		//determine concave points and link points
		Vertice prev = p.getLast();
		Vertice last = prev;
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
		while (!concaves.isEmpty()) {
			for(Iterator<Vertice> i = concaves.iterator(); i.hasNext(); ) {
				Vertice v = i.next();
				if (v.suc == null || !v.isConcave()) {
					i.remove();
				} else {
					if (v.pre.isEar(concaves)) {
						t.add(v.pre.cutEar());
						last = v;
						break;
					} else if (v.suc.isEar(concaves)) {
						t.add(v.suc.cutEar());
						last = v;
						break;
					}
				}
			}
		}
		while (last.pre.pre.pre != last) { //no triangle
			t.add(last.pre.cutEar());
			last = last.pre;
		}
		t.add(last.cutEar());
	}
	/**
	 * Erzeugt Kanten
	 * @param edges
	 * @return Anfangskante
	 */
	private Edge createEdges() {
		edges = new Edge[v.size() + 1];
		// fill edges
		Vertice v1 = v.get(v.size() - 1);
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		int id = 0;
		for(Vertice v2 : v) {
			edges[id] = new Edge(v1, v2, id++);
			v1 = v2;
			if (v2.getX() < minX) {
				minX = v2.getX();
				minY = v2.getY();
			}
		}

		edges[id] = new Edge(minX, minY, id);
		return edges[id];
	}
	private static class Vertice extends Point {
		private Vector v;
		private Vertice pre;
		private Vertice suc;
		public Vertice(double x, double y) {
			super(x, y);
		}
		public void linkPredecessor(Vertice predecessor) {
			this.pre = predecessor;
			predecessor.suc = this;
			v = new Vector(pre, this);
		}
		public Triangle cutEar() {
			Triangle t = new Triangle(pre.x, pre.y, x, y, suc.x, suc.y);
			suc.linkPredecessor(pre);
			pre = null;
			suc = null;
			return t; 
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
		/** bounding box der Kante*/
		private final BoundingBox<Edge> b;
		/**
		 * Anfangskante von links
		 * @param x x Koordinate des linkesten Punkts des Polygons
		 * @param y y Koordinate des linkesten Punkts des Polygons
		 * @param id Index der ursprünglichen Kante
		 */
		public Edge(double x, double y, int id) {
			super();
			this.x1 = Double.NEGATIVE_INFINITY;
			this.y1 = y;
			this.x2 = x;
			this.y2 = y;
			this.b = new BoundingBox<>(x, y, x, y, this);
			this.id = id;
			this.v = new Vector(1, 0);
		}
		public Edge(double x1, double y1, double x2, double y2, int id) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.b = new BoundingBox<>(x1, y1, x2, y2, this);
			this.id = id;
			this.v = new Vector(x2 - x1, y2 - y1);
		}
		public Edge(Vertice v1, Vertice v2, int id) {
			this(v1.getX(), v1.getY(), v2.getX(), v2.getY(), id);
		}
	    public CrossingPoint getIntersection(Edge b, Edge[] edges) {
	    	//check identical point first, this avoids numerical errors
	    	if (x2 == b.x1 && y2 == b.y1) {
	    		// continous polygon
	    		return new CrossingPoint(this, b, false);
	    	}
	    	if (x2 == b.x2 && y2 == b.y2) {
	    		// inverted continous polygon
	    		return new CrossingPoint(this, b, true);	    		
	    	}
	    	Edge am, bm;
	    	if (id < b.id) {
	    		am = edges[id];
	    		bm = edges[b.id];
	    	} else {
	    		am = edges[b.id];
	    		bm = edges[id];	    		
	    	}
			//Cramer's rule
	    	Vector base = new Vector(bm.x1 - am.x1, bm.y1 - am.y1);
	    	double n = am.v.crossproduct(bm.v);
	    	double za = base.crossproduct(bm.v);			
			if (n == 0) {
				if (za == 0) {
					//same line
					double mu = (x2 - b.x1) / (b.x2 - b.x1);
					if (mu >= 0 && mu <= 1) {
						//b is longer, add it at the end
						return new CrossingPoint(this, b, x2, y2, 1d, mu);
					} else {
						//b is part of this, ignore it
						return null;
					}
				} else {
					// no intersection
					return null;
				}
			} else {
				double lambdaM = za / n;
				double px = am.x1 + lambdaM * ( am.x2 - am.x1);
				double py = am.y1 + lambdaM * ( am.y2 - am.y1);
				double lambda, mu;
				if (x1 != x2) {
					lambda = (px - x1) / (x2 - x1);					
				} else {
					lambda = (py - y1) / (y2 - y1);
				}
				if (b.x1 != b.x2) {
					mu = (px - b.x1) / (b.x2 - b.x1);
				} else {
					mu = (py - b.y1) / (b.y2 - b.y1);
				}
				if (lambda >= 0 && mu >= 0 && lambda <= 1 && mu <= 1) {
					return new CrossingPoint(this, b, px, py, lambda, mu);
				} else {
					return null;
				}
			}
	    }	
	    public Edge invert() {
	    	return new Edge(x2, y2, x1, y1, id);
	    }
		@Override
		public int compareTo(Edge o) {
			//return b.getSource().compareTo(o.b.getSource());
			return o.id - id;
		}
		@Override
		public String toString() {
			return "("+x1+"/"+y1+") -> ("+x2+"/"+y2+")";
		}
		
	}	
	private static class CrossingPoint  {
		private double lambda;
		private double angle; //approximated angle from 0 to 4
		private Vertice p;
		private Edge edge;
		public CrossingPoint(Edge a, Edge b, boolean inverted) {
			lambda = 1;
			p = new Vertice(a.x2, a.y2);
			if (inverted) {
				edge = b.invert();				
			} else {
				edge = b;
			}
			angle = edge.v.getAngleOrder() - a.v.negative().getAngleOrder();
			if (angle < 0) {
				angle += 4;
			}			
		}
		public CrossingPoint(Edge a, Edge b, double x, double y, double lambda, double mu) {
			this.lambda = lambda;			
			this.p = new Vertice(x, y);
			if (mu == 0) {
				edge = b;
			} else if (mu == 1) {
				edge = b.invert();
			} else {
				if (a.v.crossproduct(b.v) > 0) {
					edge = new Edge(p.getX(), p.getY(), b.x2, b.y2, b.id);					
				} else {
					edge = new Edge(p.getX(), p.getY(), b.x1, b.y1, b.id);
				}
			}
			angle = edge.v.getAngleOrder() - a.v.negative().getAngleOrder(); 
			if (angle < 0) {
				angle += 4;
			}
		}

		@Override
		public String toString() {
			return p+" lambda=" + lambda+" angle"+angle;
		}
		
	}
}
