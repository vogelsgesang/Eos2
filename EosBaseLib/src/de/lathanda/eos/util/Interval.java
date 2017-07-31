package de.lathanda.eos.util;

import java.util.Comparator;

public interface Interval {
	enum Place {
		LOWER,
		BETWEEN,
		HIGHER
	}
	double getLow();
	double getHigh();
	default Place locate(double value) {
		if (value > getHigh() ) {
			return Place.HIGHER;
		} else if (value < getLow()) {
			return Place.LOWER;
		} else {
			return Place.BETWEEN;
		}
	}
	default double center() {
		return (getLow() + getHigh()) / 2d; 
	}
	static class LowAscComparator<T extends Interval & Comparable<T> > implements Comparator<T> {
		@Override
		public int compare(T a, T b) {
			double aLow = a.getLow();
			double bLow = b.getLow();
			if (aLow == bLow) {
				return a.compareTo(b);
			} else {
				return Double.compare(aLow, bLow);
			}
		}		
	}
	static class LowDescComparator<T extends Interval & Comparable<T> > implements Comparator<T> {
		@Override
		public int compare(T a, T b) {
			double aLow = a.getLow();
			double bLow = b.getLow();
			if (aLow == bLow) {
				return b.compareTo(a);
			} else {
				return Double.compare(bLow, aLow);
			}
		}		
	}
	static class HighAscComparator<T extends Interval & Comparable<T> > implements Comparator<T> {
		@Override
		public int compare(T a, T b) {
			double aHigh = a.getHigh();
			double bHigh = b.getHigh();
			if (aHigh == bHigh) {
				return a.compareTo(b);
			} else {
				return Double.compare(aHigh, bHigh);
			}
		}	
	}	
	static class HighDescComparator<T extends Interval & Comparable<T> > implements Comparator<T> {
		@Override
		public int compare(T a, T b) {
			double aHigh = a.getHigh();
			double bHigh = b.getHigh();
			if (aHigh == bHigh) {
				return b.compareTo(a);
			} else {
				return Double.compare(bHigh, aHigh);
			}
		}	
	}	
}
