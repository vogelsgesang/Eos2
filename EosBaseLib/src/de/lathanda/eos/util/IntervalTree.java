package de.lathanda.eos.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntervalTree<T extends Interval & Comparable<T> > {
	private Node<T> root;
	
	public void insert(T t) {
		if (root == null) {
			root = new Node<>(t);
		} else {
			insert(root, t);
		}
	}
	public List<T> seek(double value) {
		LinkedList<T> intervals = new LinkedList<>();
		if (root == null) {
			return intervals;
		} else {
			seek(root, value, intervals);
			return intervals;
		}
	}
	public void seek(double value, Collection<T> intervals) {
		if (root != null) {
			seek(root, value, intervals);
		}
	}	
	public List<T> seek(T t) {
		LinkedList<T> intervals = new LinkedList<>();
		if (root == null) {
			return intervals;
		} else {
			seek(root, t, intervals);
			return intervals;
		}		
	}
	public void seek(T t, Collection<T> intervals) {
		if (root != null) {
			seek(root, t, intervals);
		}
	}
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
			} else {
				work.addHigh(value,  intervals);
				if (work.higher != null) {
					queue.addFirst(work.higher);
				}
			}
		}
	}
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
	
	private static class Node<T extends Interval & Comparable<T> > {
		private final double value;
		private final SortedSet<T>  lowOrder;
		private final SortedSet<T> highOrder;
		private Node<T> lower;
		private Node<T> higher;
		private Node(double value) {
			this.value = value;
			lowOrder = new TreeSet<T>(new Interval.LowAscComparator<>());
			highOrder = new TreeSet<T>(new Interval.HighDescComparator<>());
		}
		private Node(T t) {
			this.value = t.getCenter();
			lowOrder = new TreeSet<T>(new Interval.LowAscComparator<>());
			highOrder = new TreeSet<T>(new Interval.HighDescComparator<>());
			add(t);
		}
		private void add(T t) {
			lowOrder.add(t);
			highOrder.add(t);			
		}
		private void addHigh(double value, Collection<T> list) {
			for(T t: highOrder) {
				if (t.getHigh() > value) {
					list.add(t);
				} else {
					break;
				}
			}			
		}
		private void addLow(double value, Collection<T> list) {
			for(T t: lowOrder) {
				if (t.getLow() < value) {
					list.add(t);
				} else {
					break;
				}
			}			
		}
		public void addAll(Collection<T> list) {
			list.addAll(lowOrder);
		}
	}
}
