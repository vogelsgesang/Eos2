package de.lathanda.eos.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;

public class Tesselation {
	//original data
	private double[] x;
	private double[] y;
	private int len;
	//simple concave polygon representing outer boarder
	private LinkedList<Vertice> p = new LinkedList<>();
	//final triangulation
	private LinkedList<Triangle> t;
	public Tesselation(double[] x, double[] y) {
		if (x.length != y.length) throw new ArrayIndexOutOfBoundsException();
		this.x = x;
		this.y = y;
		this.len = x.length;
	}
	public Collection<Triangle> tesselate() {
		convertToSimplePolygon(); //x,y -> p
		earCut();                 //p -> t
		return t;
	}
	private void convertToSimplePolygon() {		
		// --- create edges and determine start edge ---
		LinkedList<Edge> edges = new LinkedList<>();		
		Edge start = createEdges(edges);
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
			Edge xEdge = xHits.pollFirst().getSource();
			Edge yEdge = yHits.pollFirst().getSource();
			while (xEdge != null && yEdge != null) {
				if (xEdge == yEdge) {
					//bounding boxes overlap
					hits.add(xEdge);
					xEdge = xHits.pollFirst().getSource();
					yEdge = yHits.pollFirst().getSource();
				} else if ( xEdge.compareTo(yEdge) > 0) {
					xEdge = xHits.pollFirst().getSource();
				} else {
					yEdge = yHits.pollFirst().getSource();
				}
			}
			// check all potential crossing edge if they actually do intersect
			CrossingPoint best = null; 
			for (Edge e : hits) {
				if (e.id == act.id) continue; //ignore the self intersection
				CrossingPoint inter = act.getIntersection(e);
				if (inter != null) {
					if (best == null || best.compareTo(inter) > 0) {
						best = inter;
					}
				}
			}			
			p.add(new Vertice(act.x1, act.y1));
			act = best.edge;						
		} while (act.id != start.id && act.x1 == start.x1 && act.y1 == start.y1);
	}
	private void earCut() {
		//determine concave points and link points
		Vertice prev = p.getLast();
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
			Vertice last = null;
			for(Iterator<Vertice> i = concaves.iterator(); i.hasNext(); ) {
				Vertice v = i.next();
				if (v.pre.isEar(concaves)) {
					t.add(v.pre.cutEar());
					last = v;
					if (!v.isConcave()) {
						i.remove();
					}
				} else if (v.suc.isEar(concaves)) {
					t.add(v.suc.cutEar());
					last = v;
					if (!v.isConcave()) {
						i.remove();
					}
				}
			}
			while (last.pre.pre.pre != last) { //no triangle
				t.add(last.pre.cutEar());
			}
			t.add(last.cutEar());
		}
	}
	/**
	 * Erzeugt Kanten
	 * @param edges
	 * @return Anfangskante
	 */
	private Edge createEdges(LinkedList<Edge> edges) {
		// fill edges
		edges.add(new Edge(x[len-1], y[len-1], x[0], y[0], 0));
		for(int i = 1; i < len; i++) {
			edges.add(new Edge(x[i-1],y[i-1], x[i], y[i], i));
		}
		double minX = Double.POSITIVE_INFINITY;
		Edge s1 = null;
		Edge s2 = null;
		Edge prev = edges.getLast();
		for (Edge e : edges) {
			if (e.x1 < minX) {
				s1 = prev;
				s2 = e;
			}
		}
		// check angle between both potential edges
		if ( (s1.x1-s2.x1)/(s1.y1-s2.y1) < (s2.x2-s2.x1)/(s2.y2-s2.y1) ) {
			//s2
			return s2;
		} else {
			s1.invert();
			return s1;
		}
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
			pre.suc = suc;
			suc.pre = pre;
			pre.v = new Vector(pre, suc);
			return new Triangle(pre.x, pre.y, x, y, suc.x, suc.y); 
		}
		public boolean isEar(Vertice p) {
			Vector cv = new Vector(pre.x - suc.x, pre.y - suc.y);
			Vector a = new Vector(this, p);
			Vector b = new Vector(suc, p);
			Vector c = new Vector(pre, p);
			return v.crossproduct(a) < 0 || cv.crossproduct(c) < 0 || pre.v.crossproduct(b) < 0;
		}
		public boolean isEar(Collection<Vertice> c) {
			for(Vertice v : c) {
				if (!isEar(v)) {
					return false;
				}
			}
			return true;			
		}
		public boolean isConcave() {
			return pre.v.crossproduct(v) < 0;
		}
	}
	private static class Edge implements Comparable<Edge> {
		private double x1, y1;
		private double x2, y2;
		private int id;
		private BoundingBox<Edge> b;
		public Edge(double x1, double y1, double x2, double y2, int id) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.b = new BoundingBox<>(x1, y1, x2, y2, this);
			this.id = id;
		}

	    public CrossingPoint getIntersection(Edge b) {
			//Cramer's rule
			double n  = (b.y2 - y2)*(b.x1 - x1) - (b.x2 - x2)*(b.y1 - y1);
			double za = (b.x2 - x2)*(y1 - y2) - (b.y2 - y2)*(x1 - x2);
			double zb = (b.x1 - x1)*(y1 - y2) - (b.y1 - y1)*(x1 - x2);
			
			if (n == 0) {
				if (za == 0) {
					//same line
					double ub = (x2 - b.x1) / (b.x2 - b.x1);
					if (ub >= 0 && ub <= 1) {
						//b is longer, add it at the end
						return new CrossingPoint(this, b, 1d);
					} else {
						//b is part of this, ignore it
						return null;
					}
				} else {
					return null;
				}
			} else {			
				double ua = za / n;
				double ub = zb / n;
				if (ua >= 0 && ub >= 0 && ua <= 1 && ub <= 1) {
					return new CrossingPoint(this, b, ua);
				} else {
					return null;
				}
			}    	
	    }	
	    public Vector getVector() {
	    	return new Vector(x2 - x1, y2 - y1);
	    }
	    public void invert() {
	    	double tmp = x1;
	    	x1 = x2;
	    	x2 = tmp;
	    	tmp = y1;
	    	y1 = y2;
	    	y2 = tmp;
	    }
		@Override
		public int compareTo(Edge o) {
			return b.getSource().compareTo(o.b.getSource());
		}
	}	
	private static class CrossingPoint implements Comparable<CrossingPoint> {
		private double lambda;
		private double angle;
		private Vertice p;
		private Edge edge;
		public CrossingPoint(Edge a, Edge b, double lambda) {
			Vector av = a.getVector();
			this.lambda = lambda;			
			if (lambda == 0) {
				p = new Vertice(a.x1, a.y1);
			} else if (lambda == 1) {
				p = new Vertice(a.x2, a.y2);
			} else {
				p = new Vertice(a.x1 + lambda * (a.x2 - a.x1), a.y1 + lambda * (a.y2 - a.y1));
				Vector bv = b.getVector();
				double cp = av.crossproduct(bv);
				if (cp < 0) {
					edge = new Edge(p.getX(), p.getY(), b.x1, b.y1, b.id);					
				} else {
					edge = new Edge(p.getX(), p.getY(), b.x2, b.y2, b.id);
				}				
			}
			angle = edge.getVector().getAngle() - av.getAngle(); 
		}
		@Override
		public int compareTo(CrossingPoint b) {
			int res = Double.compare(lambda,  b.lambda);
			if (res == 0) {
				res = Double.compare(angle, b.angle);
			}
			return res;
		}
	}
}
