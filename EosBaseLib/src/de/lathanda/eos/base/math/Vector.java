package de.lathanda.eos.base.math;

import de.lathanda.eos.base.math.Point;
import de.lathanda.eos.base.Picture;

import java.text.MessageFormat;

/**
 * \brief Vektor
 *
 * Die Klasse repräsentiert einen Vektor mit sehr vielen
 * Transformationsmethoden. Die Methoden decken einen großen Teil der
 * analytischen Geometrie ab. Grundsätzlich ist es unmöglich die Koordinaten
 * eines Vektors zu verändern, wenn er einmal erzeugt wurde. Alle Berechnungen
 * liefern neue Vektorobjekte. Soll eine Vektor verändert werden muss man zB
 * schreiben 
 * \code 
 *   Vector a = new Vector (1,0); 
 *   Vector b = new Vector (0,1); 
 *   a = a.add(b); 
 * \endcode
 *
 * <h2>Anwendungsbeispiele</h2>
 * \li <B>Lineare Bewegung</B> <br>
 * Ein Punkt p soll in Richtung Vektor v bewegt werden. 
 * \code 
 *   p.move(v);
 * \endcode
 *
 * \li <B>Vektorzerlegung</B> <br>
 * Ein Vektor v soll anhand eines Vektors b zerlegt werden, in eine Komponente
 * die Parallel (p) zu b ist und eine die senkrecht (s) zu b ist. 
 * \code 
 *   Vector p = v.getProjection(b); 
 *   Vector s = v.getPerpendicular(b); 
 * \endcode 
 * Hierbei gilt \f$vec{v} = \vec{p} + \vec{s}\f$
 *
 * \li <B>Flächenberechnung</B><br>
 * Für ein Dreieck mit den Ecken A,B,C soll die Fläche berechnet werden. 
 * \code
 *   Vector c = new Vector(A, B); 
 *   Vector b = new Vector(A, C); 
 *   double fläche = b.getParallelogramArea(c) / 2; 
 * \endcode
 *
 * \li <B>Drehsinn</B><br>
 * Für eine Vektorkette (vk) soll ermittelt werden ob sie links oder rechts
 * gekrümmt ist. 
 * \code 
 *   if (vk[i].getSignedParallelogramArea(vk[i+1]) < 0) {
 *     //linksgekrümmt (unter Vorbehalt) 
 *   } else { 
 *     //rechtsgekrümmt (unter Vorbehalt)
 *   } 
 * \endcode 
 * Was links und rechts ist hängt von der Orientierung der
 * Koordinatenachsen ab, daher einfach ausprobieren was für die jeweilige
 * Anwendung links und rechts ist.
 *
 * \li <B>Elastischer Stoss zwischen Kugeln</B><br>
 * Zwei Kugel mit den Mittelpunkten pa und pb mit den Geschwindigkeitsvektoren
 * va und vb Stossen zusammen. Die neuen Geschwindigkeitsvektoren sind va2 und
 * vb2 
 * \code 
 *   Vector normale = new Vector(pa, pb);
 *   //Verbindung zwischen den beiden Mittelpunkten = Normale auf Reflektionsachse 
 *   // Impulszerlegung 
 *   // V Kollisionsrichtung H Senkrecht zur Kollisionsrichtung 
 *   // H ist der Anteil der nicht mit dem Stoss zu tun hat 
 *   // V muss wie im 1 Dimensionalen Fall behandelt werden 
 *   Vector aH = va.getPerpendicular(normale); 
 *   Vector bH = vb.getPerpendicular(normale); 
 *   Vector aV = va.getProjection(normale); 
 *   Vector bV = vb.getProjection(normale); 
 *   // Die vertikalen Impulse werden getauscht, die Horizontalen bleiben erhalten. 
 *   Vector va2 = bV.add(aH); 
 *   Vector vb2 = aV.add(bH); 
 * \endcode
 *
 * \li <B>Reflexion an einer Bande</B><br>
 * v ist die Geschwindigkeit, A und B Anfang und Ende der Bande. vH ist der
 * Geschwindigkeitsanteil entlang der Bande. vV ist der Geschwindigkeitsanteil
 * senkrecht zu Bande. v2 ist die neue Geschwindigkeit 
 * \code 
 *   b = new Vector(A, B); 
 *   Vector vH = v.getProjection(b); 
 *   Vector vV = v.getPerpendicular(b); 
 *   v2 = vH.substract(vV); 
 * \endcode
 *
 * \li <B>Richtungsbewegung</B><br>
 * Ein Punkt p soll in Richtung winkel um länge bewegt werden. 
 * \code 
 *   p.move(new Vector(winkel).setLength(länge)); 
 * \endcode
 *
 * @author Lathanda
 *
 */
public class Vector {

    public static Vector ZERO = new Vector(0, 0);
    public static Vector BASE1 = new Vector(1, 0);
    public static Vector BASE2 = new Vector(0, 1);
    /**
     * \brief x Wert
     */
    private final double dX;
    /**
     * \brief y Wert
     */
    private final double dY;

    /**
     * Neuer Vektor
     *
     * @param dX x Wert
     * @param dY y Wert
     */
    public Vector(double dX, double dY) {
        this.dX = dX;
        this.dY = dY;
    }

    /**
     * Konvertiert einen Punkt in einen Ortsvektor
     *
     * @param p Punkt
     */
    public Vector(Point p) {
        this.dX = p.getX();
        this.dY = p.getY();
    }

    /**
     * Neuer Richtungsvektor der Länge 1.
     *
     * @param angle Richtung im Bogenmaß 0 => (1,0)
     */
    public Vector(double angle) {
        dX = Math.cos(angle);
        dY = Math.sin(angle);
    }

    /**
     * Neuer Differenzvektor von A nach B
     *
     * @param A Anfangspunkt
     * @param B Endpunkt
     */
    public Vector(Point A, Point B) {
        dX = B.getX() - A.getX();
        dY = B.getY() - A.getY();
    }

    /**
     * Kopiert einen Vektor
     *
     * @param v Original Vektor
     */
    public Vector(Vector v) {
        dX = v.dX;
        dY = v.dY;
    }

    /**
     * S-Multiplikation. Multipliziert einen Vektor mit einer Zahl. Der Vektor
     * wird dabei verändert.
     *
     * @param k Skalar
     * @return Neuer Vektor um k verlängert.
     */
    public Vector multiply(double k) {
        return new Vector(dX * k, dY * k);
    }

    /**
     * Gegenvektor.
     *
     * @return Neuer Vektor negativer Länge.
     */
    public Vector negative() {
        return new Vector(-dX, -dY);
    }

    /**
     * Berechnet die Länge des Vektors.
     *
     * @return Die Länge des Vektors
     */
    public double getLength() {
        return Math.sqrt(dX * dX + dY * dY);
    }

    /**
     * Erzeugt einen neuen Vektor der gewünschten Länge.
     *
     * @param l Neue Länge
     * @return Ein neuer Vektor der Länge l.
     */
    public Vector setLength(double l) {
        if (l == 0) {
            return ZERO;
        } else {
            return normalize().multiply(l);
        }
    }

    /**
     * Der x Wert des Vektors
     *
     * @return x Wert
     */
    public double getdx() {
        return dX;
    }

    /**
     * Der y Wert des Vektors
     *
     * @return y Wert
     */
    public double getdy() {
        return dY;
    }
    public Vector setdx(double newdx) {
        return new Vector(newdx, dY);
    }
    public Vector setdy(double newdy) {
        return new Vector(dX, newdy);
    }    
    public Point asPoint() {
        return new Point(dX,dY);
    }
    /**
     * Vektoraddition.
     *
     * @param v Zweiter Vektor
     * @return Ergebnis der Addition.
     */
    public Vector add(Vector v) {
        return new Vector(dX + v.dX, dY + v.dY);
    }

    /**
     * Vektorsubtraction.
     *
     * @param v Zweiter Vektor
     * @return Ergebnis der Differenz.
     */
    public Vector substract(Vector v) {
        return new Vector(dX - v.dX, dY - v.dY);
    }

    public Vector substract(double dx, double dy) {
        return new Vector(this.dX - dx, this.dY - dy);
    }
    /**
     * Vektoraddition mit Koordinatenpaar (x,y).
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return Ergebnis der Addition..
     */
    public Vector add(double x, double y) {
        return new Vector(dX + x, dY + y);
    }

    /**
     * Der Winkel wird ausgehend von der x-Achse in Richtung der y-Achse
     * bestimmt.
     *
     * @return Winkel für Polarkoordinaten
     */
    public double getAngle() {
        double angle = Math.atan(dY / dX);
        if (dX < 0) {
            angle += Math.PI;
        }
        return angle;
    }

    /**
     * Erzeugt einen um den winkel (im Bogenmaß) rotieren Vektor. Im
     * Standardkoordinatensystem gegen den Uhrzeigersinn. Diese Methode sollte
     * nicht für rechtwinklige Drehungen verwendet werden, da sie rundet. Der
     * Bildschirm ist anders orientiert!
     *
     * @param dAngle Roationswinkel
     * @return Gedrehter Vector.
     */
    public Vector rotate(double dAngle) {
        double length = getLength();
        if (length == 0) {
        	return this;
        }
        double angle = getAngle();
        angle += dAngle;
        return new Vector(length * Math.cos(angle), length * Math.sin(angle));
    }
    public Vector invertX() {
        return new Vector(-dX,dY);
    }
    public Vector invertY() {
        return new Vector(dX,-dY);
    }
   
    /**
     * Skalarprodukt
     *
     * @param b Zweiter Vektor
     * @return Skalarprodukt
     */
    public double dotProduct(Vector b) {
        return dX * b.dX + dY * b.dY;
    }

    /**
     * Skalarprodukt mit einem Koordinatenpaar (x,y)
     *
     * @param x x Wert
     * @param y y Wert
     * @return Skalarprodukt
     */
    public double dotProduct(double x, double y) {
        return dX * x + dY * y;
    }

    /**
     * Ein neuer Vektor mit gleicher Richtung aber der Länge 1.
     *
     * @return Normalisierter Vektor.
     */
    public Vector normalize() {
        double n = getLength();
        if (n == 0) {
            return Vector.ZERO;
        } else {
            return new Vector(dX / n, dY / n);
        }
    }

    /**
     * Dreht den Vektor in einem Standard Koordinatensystem gegen den
     * Uhrzeigersinn. Die Orientierung hängt jedoch von der Orientierung der
     * Achsen ab, daher sollte man die Methode einfach ausprobieren, das
     * Verhalten bleibt wie es ist. Der Vektor wird dabei verändert.
     *
     * @return Um 90° gedrehter Vektor.
     */
    public Vector rotateRightAngleCounterClockwise() {
        return new Vector(-dY, dX);
    }

    /**
     * Berechnet den Abstand der Spitze dieses Vektors zum Basisvektor (base).
     * Die beiden Vektoren spannen dabei ein Dreieck auf. Der berechnete Wert
     * ist die Höhe von der Spitze dieses Vektors auf die Seite die durch den
     * Basisvektor gebildet wird.
     *
     * @param base Länge der senkrechten auf dem Basisvektor
     * @return Länge des Lotes
     */
    public double getPerpendicularLength(Vector base) {
        double length = base.getLength();
        if (length == 0) {
            return getLength();
        } else {
            return crossproduct(base) / length;
        }
    }

    /**
     * Projeziert diesen Vektor auf den Basisvektor (base) und ermittelt die
     * Länge. Es wird somit die Länge des Schattens berechnet.
     *
     * @param base Vektor auf den projiziert wird.
     * @return Länge der Projektion auf den Basisvektor
     */
    public double getProjectionLength(Vector base) {
        double length = base.getLength();
        if (length == 0) {
            return 0;
        } else {
            return dotProduct(base) / length;
        }
    }

    /**
     * Berechnet die Fläche des Parallelogramms, welches die beiden Vektoren
     * aufspannen.
     *
     * @param a Zweiter Vektor
     * @return Fläche des aufgespannten Parallelogramms
     */
    public double getParallelogramArea(Vector a) {
        return Math.abs(crossproduct(a));
    }

    /**
     * Berechnet die Vorzeichen behaftet Fläche des Parallelogramms, welches die
     * beiden Vektoren aufspannen. Der Wert ist negativ wenn die Fläche links
     * von diesem Vektor liegt. Dies erlaubt Orientierungstest. Die Orientierung
     * hängt jedoch von der Orientierung der Achsen ab, daher sollte man die
     * Methode einfach ausprobieren, das Verhalten bleibt wie es ist.
     *
     * @param a Zweiter Vektor
     * @return Vorzeichen behaftete Fläche des aufgespannten Parallelogramms
     */
    public double crossproduct(Vector a) {
        return dY * a.dX - dX * a.dY;
    }

    /**
     * Berechnet den Lot Vektor von der Spitze dieses Vektors auf den
     * Basisvektor. Ob der Vektor zur Spitze zeigt oder zur Basis hängt von der
     * Orientierung ab.
     *
     * @param base Basisvektor
     * @return Lotvektor
     */
    public Vector getPerpendicular(Vector base) {
        return base.rotateRightAngleCounterClockwise().setLength(getPerpendicularLength(base));
    }

    /**
     * Prüft ob der Vektor der 0 Vektor ist. Dies kann unerwartete Effekte
     * auslösen, da eine Gleichheit mir 0 eher selten der Fall ist. Wenn
     * Probleme auftreten kann als alternative die Länge geprüft werden. Etwa
     * getLength() < 0.1
     * @return 
	 * @
     *
     * return Wahr, wenn (0,0)
     */
    public boolean isZero() {
        return dX == 0 && dY == 0;
    }

    /**
     * Debugginginformationen
     *
     * @return Zeichenkette für Fehlersuche
     */
    @Override
    public String toString() {
        return MessageFormat.format("({0,number,#.00}|{1,number,#.00})",
                new Object[]{dX, dY});
    }

    /**
     * Projeziert diesen Vektor auf den Basisvektor (base) und den Projezierten
     * Vektor. Es wird somit der Schattenvektor berechnet.
     *
     * @param base Vektor auf den projiziert wird
     *
     * @return Projektionvektor
     */
    public Vector getProjection(Vector base) {
        Vector b = base.normalize();
        return b.multiply(dotProduct(b));
    }

    /**
     * Addiert den Wert zur Länge des Vektors. Wird die Länge dadurch negativ
     * wird der Vektor (0,0).
     *
     * @param l Länge
     * @return Vektor mit neuer Länge
     */
    public Vector addLength(double l) {
        double actL = getLength();
        if (actL + l <= 0.0 || actL == 0) {
            return ZERO;
        } else {
            return multiply((actL + l) / actL);
        }
    }

    /**
     * Erzeugt einen Vektor dessen Länge innerhalb der Schranken liegt. Dies
     * kann sinnvoll sein, wenn eine Bewegung eine gewisse Geschwindigkeit nicht
     * überschreiten bzw. unterschreiten soll.
     *
     * @param min Minimalwert der Länge (-1 für unbegrenzt)
     * @param max Maximalwert der Länge (Double.POSITIVE_INFINITY für ungerenzt)
     * @return 
     */
    public Vector restrict(double min, double max) {
        double len = getLength();
        if (len < min) {
            return setLength(min);
        }
        if (len > max) {
            return setLength(max);
        }
        return this;
    }

    /**
     * Zeichnet den Vector als Pfeil. Bei Vektoren die Bewegungen darstellen
     * bietet sich an als Maßstab die Frequenz zu wählen, in der Regel 30. Die
     * Zeichnung entspricht dann der Bewegung pro Sekunde. Bei echten
     * Verschiebungsvektoren sollte der Maßstab 1 gewählt werden, etwa um
     * Rechnungen zu visualisieren. Wird ein dickerer oder Farbiger Pfeil
     * benötigt, so muss das das Graphikobjekt vorher entsprechende verändert
     * werden.
     *
     * @param g Grafikobjekt
     * @param p Ausgangspunkt des Vektors
     * @param scale Maßstab (~30 Bewegung, 1 Verschiebung)
     * @param arrowLength Länge der Pfeilspitze, (~12) die Spitze is immer
     * doppelt so lang wie breit. Bei sehr kurzen Vektoren wird die Pfeilspitze
     * durch die Länge des Vektors begrenzt.
     */
    public void render(Picture g, Point p, double scale, double arrowLength) {
        /*               this
         *               --->           HHH    |
         * 2* lineDiff | LLLLLLLLLLLLLLLHHHHHH | 2*headDiff
         *                              HHH    | headDiff = 3*lineDiff
         *               -------------->
         *                 headBase
         *               -------------------->
         *                 v = scale * this
         */
        Vector v = multiply(scale);
        double length = Math.min(v.getLength(), arrowLength);
        Point head = new Point(p);
        head.move(v);
        Vector headLength = v.setLength(-length);
        Vector headBreadth = v.setLength(length / 4d).rotateRightAngleCounterClockwise();
        Vector lineBreadth = headBreadth.multiply(1d / 3d);
        double[] x = new double[7];
        double[] y = new double[7];
        // all vectors are known, now we follow the vector chain
        Vector point = new Vector(head.getX(), head.getY());
        x[0] = point.getdx();
        y[0] = point.getdy();
        point = point.add(headLength).add(headBreadth);
        x[1] = point.getdx();
        y[1] = point.getdy();
        point = point.substract(headBreadth).substract(headBreadth);
        x[6] = point.getdx();
        y[6] = point.getdy();
        point = point.add(headBreadth).add(lineBreadth);
        x[2] = point.getdx();
        y[2] = point.getdy();
        point = point.substract(lineBreadth).substract(lineBreadth);
        x[5] = point.getdx();
        y[5] = point.getdy();
        point = new Vector(p.getX(), p.getY());
        point = point.add(lineBreadth);
        x[3] = point.getdx();
        y[3] = point.getdy();
        point = point.substract(lineBreadth).substract(lineBreadth);
        x[4] = point.getdx();
        y[4] = point.getdy();
        g.drawPolygon(x, y);
    }
}
