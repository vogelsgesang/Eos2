package de.lathanda.eos.base.math;

/**
 * \brief Intervall
 * 
 * Diese Klasse verwaltet Intervalle.
 * Sie wird unter anderem als Hilfsklasse für die Polygonüberlappungsberechnung
 * benötigt.
 * 
 * @author Lathanda
 * 
 */
public class Range {
	/**
	 * \brief Untere Intervallgrenze
	 */
	public double min;
	/**
	 * \brief Obere Intervallgrenze
	 */
	public double max;

	/**
	 * Neues Intervall
	 * 
	 * @param min
	 *            Untere Intervalgrenze
	 * @param max
	 *            Obere Intervalgrenze
	 */
	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Intervall bis x ausdehnen
	 * 
	 * @param x
	 *            neue Intervallgrenze, je nach Wert entweder die untere oder
	 *            die obere Grenze
	 */
	public void extend(double x) {
		if (x < min)
			min = x;
		if (x > max)
			max = x;
	}

	/**
	 * Prüft ob zwei Intervalle überlappen
	 * 
	 * @param b
	 *            Zweites Intervall
	 * @return ob die zwei Intervalle überlappen
	 */
	public boolean overlap(Range b) {
		return (min < b.max && b.min < max);
	}

	/**
	 * Verschiebt das Intervall
	 * 
	 * @param x
	 *            Verschiebung
	 */
	public void translate(double x) {
		min += x;
		max += x;
	}
	/**
	 * Prüft ob ein Wert innerhalb des Intervalls liegt
	 * 
	 * @param x Wert 
	 * @return Wahr wenn  der Wert innerhalb des Intervalls liegt
	 */
	public boolean contains(double x) {
		return min < x && x < max;
	}
	/**
	 * Liefert die Größe des Intervalls
	 * @return Abstand zwischen min und max
	 */
	public double size() {
		return max - min;
	}
}

