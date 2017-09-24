package de.lathanda.eos.game.geom;

import java.util.Arrays;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;
/**
 * \brief Triangle
 * 
 * Diese Klasse ist eine Speicherstruktur für Dreiecke
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class Triangle {
	private double[] x;
	private double[] y;
	public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		x = new double[] {x1, x2, x3};
		y = new double[] {y1, y2, y3};
	}
	public Triangle(double[] x, double[] y) {
		this.x = x;
		this.y = y;
	}
	public double getX(int i) {
		return x[i];
	}
	public double getY(int i) {
		return y[i];
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("Triangle ");
		for(int i = 0; i < 3; i ++) {
			s.append("("+x[i]+"/"+y[i]+")");
		}
		return "Triangle [x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + "]";
	}
	public double getArea() {
		return new Vector(x[1]-x[0], y[1]-y[0]).crossproduct(new Vector(x[1]-x[2], y[1]-y[2]))/2;
	}	
	public Point getCenter() {
		return new Point((x[0]+x[1]+x[2])/3, (y[0]+y[1]+y[2])/3);
	}
	public void move(double dx, double dy) {
		for(int i = 0; i < 3; i++) {
			x[i] += dx;
			y[i] += dy;
		}
		
	}
}
