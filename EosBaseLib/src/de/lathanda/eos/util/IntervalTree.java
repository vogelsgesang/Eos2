package de.lathanda.eos.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
/**
 * \brief IntervalTree
 * 
 * Dieser Baum verwaltet Intervalle als Baum und erlaubt logarithmisches suchen auf Intervallen.
 * Diese Datenstruktur eignet sich daher für verschiedene geometrische Berechnungen.
 * -Suchen von überlappenden Rechtecken (Kollisionserkennung)
 * -Zuordnung einer Klickkoordinate zu einem Rechteck (BoundingBox)
 * Die Suchen erfordern im Mittel logarithmische Zeit.
 * Intervalle sollten niemals sortiert eingefügt werden, da der Baum
 * sonst einseitig wird und die Performance leidet. 
 * Eine zufällige Reihenfolge wäre optimal.
 * 
 * @author Peter (Lathanda) Schneider
 * @param <T> Intervallklasse
 */
public class IntervalTree<T extends Interval & Comparable<T> > {
	///Wurzel, null wenn leer
	private Node<T> root;
	/**
	 * Neues Interval hinzufügen
	 * @param t
	 */
	public void insert(T t) {
		if (root == null) {
			root = new Node<>(t);
		} else {
			insert(root, t);
		}
	}
	/**
	 * Alle Intervalle die den Wert enthalten suchen.
	 * @param value
	 * @return Unsortierte Liste der Intervalle
	 */
	public List<T> seek(double value) {
		LinkedList<T> intervals = new LinkedList<>();
		if (root == null) {
			return intervals;
		} else {
			seek(root, value, intervals);
			return intervals;
		}
	}
	/**
	 * Alle Intervalle die den Wert enthalten suchen.
	 * @param value
	 * @param intervals Hier werden die Intervalle eingefügt. Sortierung möglich!
	 */
	public void seek(double value, Collection<T> intervals) {
		if (root != null) {
			seek(root, value, intervals);
		}
	}	
	/**
	 * Alle Intervall die mit dem Interval überlappen suchen.
	 * @param t
	 * @return
	 */
	public List<T> seek(T t) {
		LinkedList<T> intervals = new LinkedList<>();
		if (root == null) {
			return intervals;
		} else {
			seek(root, t, intervals);
			return intervals;
		}		
	}
	/**
	 * Alle Intervall die mit dem Interval überlappen suchen.
	 * @param t
	 * @param intervals Hier werden die Intervalle eingefügt. Sortierung möglich!
	 */
	public void seek(T t, Collection<T> intervals) {
		if (root != null) {
			seek(root, t, intervals);
		}
	}
	/**
	 * Interne Suche nach Wert.
	 * @param start
	 * @param value
	 * @param intervals
	 */
	private static <T extends Interval & Comparable<T> >void seek(Node<T> start, double value, Collection<T> intervals) {
		LinkedList<Node<T> > queue = new LinkedList<>();
		Node<T> work = null;
		queue.add(start);
		while (!queue.isEmpty()) {
			work = queue.pop();
			if (value < work.value) {
				work.addLow(value,  intervals);
				if (work.lower != null) {
					queue.addFirst(work.lower);
				}
			} else if (value > work.value) {
				work.addHigh(value,  intervals);
				if (work.higher != null) {
					queue.addFirst(work.higher);
				}
			} else {
				work.addAll(intervals);
			}
		}
	}
	/**
	 * Interne Suche nach Interval.
	 * @param start
	 * @param t
	 * @param intervals
	 */
	private static <T extends Interval & Comparable<T> >void seek(Node<T> start, T t, Collection<T> intervals) {
		LinkedList<Node<T> > queue = new LinkedList<>();
		Node<T> work = null;
		queue.add(start);
		while (!queue.isEmpty()) {
			work = queue.pop();
			if (t.getHigh() < work.value) {
				work.addLow(t.getHigh(), intervals);
				if (work.lower != null) {
					queue.addFirst(work.lower);
				}
			} else if (t.getLow() > work.value) {
				work.addHigh(t.getLow(),  intervals);
				if (work.higher != null) {
					queue.addFirst(work.higher);
				}
			} else {
				work.addAll(intervals);
				if (work.higher != null) {
					queue.addFirst(work.higher);
				}
				if (work.lower != null) {
					queue.addFirst(work.lower);
				}
				
			}
		}		
	}
	/**
	 * Internes Einfügen
	 * @param start
	 * @param t
	 */
	private static <T extends Interval & Comparable<T> >void insert(Node<T> start, T t) {
		Node<T> act = start;
		while (true) {
			switch (t.locate(act.value)) {
			case LOWER:
				if (act.higher == null) {
					act.higher = new Node<T>(t);
					return;
				} else {
					act = act.higher;
				}
				break;
			case HIGHER:
				if (act.lower == null) {
					act.lower = new Node<T>(t.getCenter());
					act.lower.add(t);
					return;
				} else {
					act = act.lower;
				}				
				break;
			case BETWEEN:
				act.add(t);
				return;
			}
		}		
	}	
	/**
	 * \brief Knoten
	 * Datenstruktur zum Verwalten der Suchstrukturen. 
	 *
	 * @param <T>
	 */
	private static class Node<T extends Interval & Comparable<T> > {
		///Alle Intervall dieses Knotens enthalten diesen Wert zB 5
		private final double value;
		/// Nach unterer Grenze sortiert zB (1,6),(2,8),(3,7),(4,9)
		private final SortedSet<T>  lowOrder;
		/// Nach oberer Grenze sortiert  zB (4,9),(2,8),(3,7),(1,6)
		private final SortedSet<T> highOrder;
		/// Intervalle die den Wert nicht enthalten und davor liegen
		private Node<T> lower;
		/// Intervalle die den Wert nicht enthalten und danach liegen
		private Node<T> higher;
		/**
		 * Leerer Knoten zu diesem Wert.
		 * @param value
		 */
		private Node(double value) {
			this.value = value;
			lowOrder = new TreeSet<T>(new Interval.LowAscComparator<>());
			highOrder = new TreeSet<T>(new Interval.HighDescComparator<>());
		}
		/**
		 * Knoten mit diesem Intervall und der Mitte des Intervalls als Wert.
		 * @param t
		 */
		private Node(T t) {
			this.value = t.getCenter();
			lowOrder = new TreeSet<T>(new Interval.LowAscComparator<>());
			highOrder = new TreeSet<T>(new Interval.HighDescComparator<>());
			add(t);
		}
		/**
		 * Neues Intervall einfügen in die Listen. Der Wert muss enthalten sein!
		 * @param t
		 */
		private void add(T t) {
			lowOrder.add(t);
			highOrder.add(t);			
		}
		/**
		 * Intervalle aus der Liste auslesen die nach der oberen Grenze sortiert ist.
		 * Alle Intervall bis zum Wert werden in die Menge eingefügt.
		 * @param value
		 * @param list
		 */
		private void addHigh(double value, Collection<T> list) {
			for(T t: highOrder) {
				if (t.getHigh() >= value) {
					list.add(t);
				} else {
					break;
				}
			}			
		}
		/**
		 * Intervalle aus der Liste auslesen die nach der unteren Grenze sortiert ist.
		 * Alle Intervall bis zum Wert werden in die Menge eingefügt.
		 * @param value
		 * @param list
		 */
		private void addLow(double value, Collection<T> list) {
			for(T t: lowOrder) {
				if (t.getLow() <= value) {
					list.add(t);
				} else {
					break;
				}
			}			
		}
		/**
		 * Alle Intervalle in die Liste einfügen die dieser Knoten verwaltet.
		 * @param list
		 */
		public void addAll(Collection<T> list) {
			list.addAll(lowOrder);
		}
	}
}
