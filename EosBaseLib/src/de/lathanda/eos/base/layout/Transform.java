package de.lathanda.eos.base.layout;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.math.Vector;

/**
 * \brief Geometrische Transformation.
 * 
 * Speichert Transformationen von geometrischen Objekten.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Transform {
	private final Vector v;
	private final double angle;
	private final boolean mirrorx;
	private final double scale;
	public static final Transform ID = new Transform();

	private Transform() {
		v = Vector.ZERO;
		angle = 0;
		mirrorx = false;
		scale = 1d;
	}

	private Transform(Vector v, double angle, boolean mirrorx, double scale) {
		this.v = v;
		this.angle = angle;
		this.mirrorx = mirrorx;
		this.scale = scale;
	}

	public Transform rotate(double angle) {
		return new Transform(v, this.angle + angle, false, scale);
	}

	public Transform rotate(double x, double y, double angle) {
		return new Transform(v.substract(x, y).rotate(angle).add(x, y), this.angle + angle, false, scale);
	}

	public Transform mirrorX() {
		return new Transform(v, -angle, !mirrorx, scale);
	}

	public Transform mirrorY() {
		return new Transform(v, Math.PI-angle, !mirrorx, scale);
	}

	public Transform scale(double factor) {
		return new Transform(v, angle, mirrorx, scale * factor);
	}

	public Transform scalePosition(double factor) {
		return new Transform(v.multiply(factor), angle, mirrorx, scale);
	}

	public Transform scalePositionAt(double x, double y, double factor) {
		return new Transform(v.substract(x, y).multiply(factor).add(x, y), angle, mirrorx, scale);
	}

	public Transform translate(double dx, double dy) {
		if (mirrorx) {
			return new Transform(v.add(-dx, dy), angle, mirrorx, scale);
		} else {
			return new Transform(v.add(dx, dy), angle, mirrorx, scale);
		}
	}

	public Transform translate(Vector v) {
		if (mirrorx) {
			return new Transform(this.v.add(v.invertX()), angle, mirrorx, scale);
		} else {
			return new Transform(this.v.add(v), angle, mirrorx, scale);
		}
	}

	public Transform transform(Transform child) {
		Transform result = new Transform(child.v.multiply(scale).rotate(angle).add(v), child.angle, child.mirrorx,
				child.scale);
		if (mirrorx) {
			return result.mirrorX().scale(scale).rotate(angle);
		} else {
			return result.scale(scale).rotate(angle);
		}
	}

	public Point transform(Point point) {
		Vector result = new Vector(point);
		if (mirrorx) {
			result = result.invertX();
		}
		return result.multiply(scale).rotate(angle).add(v).asPoint();
	}

	public Point transform(double x, double y) {
		Vector result = new Vector(x, y);
		if (mirrorx) {
			result = result.invertX();
		}
		return result.multiply(scale).rotate(angle).add(v).asPoint();
	}

	public double transform(double x) {
		return x * scale;
	}

	public Transform transformBack(Transform child) {
		Transform result = child.rotate(-angle).scale(1 / scale);
		if (mirrorx) {
			result.mirrorX();
		}
		return new Transform(result.v.substract(v).rotate(-angle).multiply(1 / scale), result.angle, result.mirrorx,
				result.scale);
	}

	public Point transformBack(Point p) {
		Vector result = new Vector(p);
		if (mirrorx) {
			result = result.invertX();
		}
		return result.substract(v).rotate(-angle).multiply(1 / scale).asPoint();
	}

	public Transform setTranslation(double dx, double dy) {
		return new Transform(new Vector(dx, dy), angle, mirrorx, scale);
	}

	public Transform setdx(double dx) {
		return new Transform(v.setdx(dx), angle, mirrorx, scale);
	}

	public Transform setdy(double dy) {
		return new Transform(v.setdy(dy), angle, mirrorx, scale);
	}

	public double getdx() {
		return v.getdx();
	}

	public double getdy() {
		return v.getdy();
	}

	public double getAngle() {
		return angle;
	}

	public double getScale() {
		return scale;
	}

	public Transform setAngle(double angle) {
		return new Transform(v, angle, mirrorx, scale);
	}

	public boolean getMirrorX() {
		return mirrorx;
	}

	@Override
	public String toString() {
		return "Transform{" + "p=" + v + ", angle=" + angle + ", scale=" + scale + '}';
	}
}
