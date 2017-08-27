package de.lathanda.eos.game.geom;

import java.util.Arrays;
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
}
