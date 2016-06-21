package de.lathanda.eos.base.math;

/**
 * \brief komplexe Zahl
 *
 * Die Klasse repräsentiert \f$\mathbb{C}\f$. Ihre Objekte sind immutable. Wie
 * bei der Klasse String können die Attributwerte nachträglich nicht verändert
 * werden. Das bedeutet alle Berechnung erzeugen neue Objekte als Ergebnis!
 *
 * @author Lathanda
 *
 */

public class Complex {

    /**
     * \brief 0
     */
    public static final Complex ZERO = new Complex(0, 0);
    /**
     * \brief 1
     */
    public static final Complex ONE = new Complex(1, 0);
    /**
     * \brief i
     */
    public static final Complex I = new Complex(0, 1);
    /**
     * \brief e
     */
    public static final Complex E = new Complex(Math.E, 0);
    /**
     * \brief \f$\pi\f$
     */
    public static final Complex PI = new Complex(Math.PI, 0);
    /**
     * \brief real Anteil
     */
    private final double a;
    /**
     * \brief imaginär Anteil
     */
    private final double b;

    /**
     * Neue komplexe Zahl a + i b. Diese Werte sind unveränderlich!
     * Rechenergebnisse sind immer neue Objekte.
     *
     * @param a Realteil
     * @param b Imaginärteil
     */
    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Realteil
     *
     * @return Realteil
     */
    public double Re() {
        return a;
    }

    /**
     * Imaginärteil
     *
     * @return Imaginärteil
     */
    public double Im() {
        return b;
    }

    /**
     * Addiert diese komplexe Zahl und die komplexe Zahl c.
     *
     * @param c
     * @return Summe
     */
    public Complex add(Complex c) {
        return new Complex(a + c.a, b + c.b);
    }

    /**
     * Substrahiert diese komplexe Zahl und die komplexe Zahl c.
     *
     * @param c
     * @return Differenz
     */
    public Complex substract(Complex c) {
        return new Complex(a - c.a, b - c.b);
    }

    /**
     * Berechnet die negative Zahl
     *
     * @return negative Zahl.
     */
    public Complex negative() {
        return new Complex(-a, -b);
    }

    /**
     * Berechnet die inverse Zahl
     *
     * @return inverse Zahl.
     */
    public Complex inverse() {
        return conjugation().divide(a * a + b * b);
    }

    /**
     * Multipliziert diese komplexe Zahl und die reele Zahl x.
     *
     * @param x zweiter Faktor
     * @return Produkt
     */
    public Complex multiply(double x) {
        return new Complex(a * x, b * x);
    }

    /**
     * Multipliziert diese komplexe Zahl und die komplexe Zahl c.
     *
     * @param c zweiter Faktor
     * @return Produkt
     */
    public Complex multiply(Complex c) {
        return new Complex(a * c.a - b * c.b, a * c.b + b * c.a);
    }

    /**
     * Dividiert diese komplexe Zahl und die reele Zahl x.
     *
     * @param x
     * @return Quotient
     */
    public Complex divide(double x) {
        return new Complex(a / x, b / x);
    }

    /**
     * Dividiert diese komplexe Zahl und die komplexe Zahl c.
     *
     * @param c
     * @return Quotient
     */
    public Complex divide(Complex c) {
        return multiply(c.inverse());
    }

    /**
     * Berechnet die x-te Potenz der komplexen Zahl.
     *
     * @param x reeler Exponent
     * @return Potenz
     */
    public Complex pow(double x) {
        double r = Math.pow(abs(), x);
        double phi = x * angle();
        return new Complex(Math.cos(phi) * r, Math.sin(phi) * r);
    }

    /**
     * Berechnet die c-te Potenz der komplexen Zahl.
     *
     * @param c
     * @return Potenz
     */
    public Complex pow(Complex c) {
        return pow(c.a).multiply(pow(c.b).powI());
    }

    /**
     * Berechnet die x-te Wurzel der komplexen Zahl. Das Ergebnis ist nur eine
     * der möglichen Lösungen. Es gibt aber unter umständen mehrere.
     *
     * @param x reeler Wurzelexponent
     * @return Wurzel
     */
    public Complex nroot(double x) {
        return pow(1 / x);
    }

    /**
     * Berechnet die c-te Wurzel der komplexen Zahl. Das Ergebnis ist nur eine
     * der möglichen Lösungen. Es gibt aber unter umständen mehrere.
     *
     * @param c Wurzelexponent
     * @return Wurzel
     */
    public Complex nroot(Complex c) {
        return pow(ONE.divide(c));
    }

    /**
     * Berechnet die komplexe Zahl hoch i.
     *
     * @return Ergebnis
     */
    private Complex powI() {
        double r = Math.pow(Math.E, -angle());
        double phi = Math.log(abs());
        return new Complex(Math.cos(phi) * r, Math.sin(phi) * r);
    }

    /**
     * Berechnet e hoch die komplexen Zahl
     *
     * @return Ergebnis
     */
    public Complex e() {
        return E.pow(this);
    }

    /**
     * Berechnet einen möglichen Logarithmus zur Basis e. Die Funktion ist für 0
     * nicht definiert. Wenn die Funktion \f$a + b i\f$ liefert, so ist die
     * Menke der Lösungen \f$a + b i + 2 k \pi i, k\in\mathbb{Z}\f$
     *
     * @return Lösung mit 0<= Im < \f$2 \pi\f$
     */
    public Complex ln() {
        return new Complex(Math.log(abs()), angle());
    }

    /**
     * Berechnet den Logarithmus zur übergebenen Basis Die Funktion ist für 0
     * nicht definiert.
     *
     * @param base Basis
     * @return Ergebnis
     */
    public Complex log(Complex base) {
        return ln().divide(base.ln());
    }

    /**
     * Berechnet den Betrag (Abstand zu (0,0)) der komplexen Zahl.
     *
     * @return Betrag
     */
    public double abs() {
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Berechnet den Winkel der Polarkoordinaten
     *
     * @return Winkel zwischen dieser komplexen Zahl als Vektor und dem
     * Vektor(1,0)
     */
    public double angle() {
        if (b == 0) {
            if (a < 0) {
                return Math.PI;
            } else if (a > 0) {
                return 0;
            } else {
                throw new ArithmeticException("Complex.angle(" + this + ")");
            }
        } else {
            return (b < 0) ? Math.atan(a / b) : Math.atan(a / b) + Math.PI;
        }
    }

    /**
     * Berechnet die komplex konjugierte Zahl
     *
     * @return komplex konjugierten Zahl
     */
    public Complex conjugation() {
        return new Complex(a, -b);
    }

    /**
     * Debugging Informationen
     *
     * @return Komplexezahl als Zeichenkette
     */
    @Override
    public String toString() {
        return a + "+" + b + "i";
    }

    /**
     * Vergleich die komplexe Zahl mit der anderen.
     *
     * @param c Vergleichswert
     * @return Wahr, wenn beide gleich sind
     */
    public boolean equals(Complex c) {
        return a == c.a && b == c.b;
    }

    /**
     * @return Eins
     */
    public Complex one() {
        return ONE;
    }

    /**
     * @return Null
     */
    public Complex zero() {
        return ZERO;
    }
}
