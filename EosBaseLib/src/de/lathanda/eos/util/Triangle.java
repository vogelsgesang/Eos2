package de.lathanda.eos.util;

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
	
}
