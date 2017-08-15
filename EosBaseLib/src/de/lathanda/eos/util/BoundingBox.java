package de.lathanda.eos.util;

public class BoundingBox<T extends Comparable<T> > {
	private final double x1, y1;
	private final double x2, y2;
	private final T source;
	
	public BoundingBox(double x1, double y1, double x2, double y2, T source) {
		if (x1 < x2) {
			this.x1 = x1;
			this.x2 = x2;
		} else {
			this.x1 = x2;
			this.x2 = x1;
		}
		if (y1 < y2) {
			this.y1 = y1;
			this.y2 = y2;
		} else {
			this.y1 = y2;
			this.y2 = y1;
		}
		this.source = source;
	}
	public T getSource() {
		return source;
	}
	public class xInterval implements Interval, Comparable<xInterval> {
		@Override
		public int compareTo(xInterval b) {
			BoundingBox<T> r = b.getInner();
			return source.compareTo(r.source);
		}
		@Override
		public double getLow() {
			return x1;
		}
		@Override
		public double getHigh() {
			return x2;
		}
		private BoundingBox<T> getInner() {
			return BoundingBox.this;
		}
		public T getSource() {
			return source;
		}
	}
	public class yInterval implements Interval, Comparable<yInterval> {
		@Override
		public int compareTo(yInterval b) {
			BoundingBox<T> r = b.getInner();
			return source.compareTo(r.source);
		}
		@Override
		public double getLow() {
			return y1;
		}
		@Override
		public double getHigh() {
			return y2;
		}
		private BoundingBox<T> getInner() {
			return BoundingBox.this;
		}
		public T getSource() {
			return source;
		}
	}
}
