package de.lathanda.eos.util;

import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntervalTree<T extends Interval & Comparable<T> > {
	private Node<T> root;
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
		private void add(T t) {
			lowOrder.add(t);
			highOrder.add(t);			
		}
		public void insert(T t) {
			Node<T> act = this;
			while (true) {
				switch (t.locate(act.value)) {
				case LOWER:
					if (higher == null) {
						higher = new Node<T>(t.center());
						higher.add(t);
						return;
					} else {
						act = higher;
					}
					break;
				case HIGHER:
					if (lower == null) {
						lower = new Node<T>(t.center());
						lower.add(t);
						return;
					} else {
						act = lower;
					}				
					break;
				case BETWEEN:
					act.add(t);
					return;
				}
			}
		}
		public SortedSet<T> findIntervals(double value) {
			TreeSet<T> intervals = new TreeSet<T>();
			LinkedList<Node<T>> workingQueue = new LinkedList<>();
			workingQueue.add(this);
			while (!workingQueue.isEmpty()) {
				
			}
			return intervals;
			
		}
	}

}
