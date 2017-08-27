package de.lathanda.eos.util;

import java.util.Comparator;
/**
 * \brief Interval
 * 
 * Diese Schnittstelle muss realisiert werden um einen IntervalTree zu nutzen.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface Interval {
	enum Place {
		///Davor
		LOWER,
		///Dazwischen
		BETWEEN,
		///Danach
		HIGHER
	}
	/**
	 * Untere Grenze
	 * @return
	 */
	double getLow();
	/**
	 * Obere Grenze
	 * @return
	 */
	double getHigh();
	/**
	 * Wert innerhalb des Intervalls lokalisieren. 
	 * @param value
	 * @return
	 */
	default Place locate(double value) {
		if (value > getHigh() ) {
			return Place.HIGHER;
		} else if (value < getLow()) {
			return Place.LOWER;
		} else {
			return Place.BETWEEN;
		}
	}
	/**
	 * Mitte des Intervals.
	 * Es darf jeder Wert geliefert werden der innerhalb des Intervals liegt.
	 * Standard ist der Durchschnitt zwischen low und high.
	 * @return
	 */
	default double getCenter() {
		return (getLow() + getHigh()) / 2d; 
	}
	/**
	 * Sortierung nach der unteren Grenze aufsteigend.  
	 */
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
	/**
	 * Sortierung nach der unteren Grenze ansteigend.  
	 */
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
	/**
	 * Sortierung nach der oberen Grenze aufsteigend.  
	 */
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
	/**
	 * Sortierung nach der oberen Grenze absteigend.  
	 */
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
