package de.lathanda.eos.base.math;

import java.awt.geom.AffineTransform;

/**
 * \brief Matrix
 *
 * Die Klasse erlaubt die zwei dimensionale Transformation eines Vektors
 * ({@link Vector}). Sie erlaubt zusätzlich zu Rotation und Skalierung, eine
 * Verschiebung, daher ist sie mathematisch betrachtet eine \f$\mathbb{R}^3
 * \times \mathbb{R}^3\f$ Matrix der Form \f$ \begin{pmatrix} x_{(1,1)} &
 * x_{(1,2)} & t_x \\ x_{(2,1)} & x_{(2,2)} & t_y \\ 0 & 0 & 1 \end{pmatrix}\f$,
 * es werden hierbei jedoch nur die echten Werte gespeichert. Vektoren werden
 * intern als \f$ \begin{pmatrix}x \\ y\\ 1\end{pmatrix}\f$ ausgewertet, wobei
 * der dritte Wert implizit ergänzt wird.
 *
 * @author Lathanda
 *
 */
public class Matrix {

    /**
     * \brief 1. Zeile 1. Spalte
     */
    private double x11;
    /**
     * \brief 1. Zeile 2. Spalte
     */
    private double x12;
    /**
     * \brief 2. Zeile 1. Spalte
     */
    private double x21;
    /**
     * \brief 2. Zeile 2. Spalte
     */
    private double x22;
    /**
     * \brief Verschiebung um x
     */
    private double tx;
    /**
     * \brief Verschiebung um y
     */
    private double ty;

    /**
     * Erzeugt eine neutrale Matrix \f$\begin{pmatrix} 1 & 0 & 0 \\ 0 & 1 & 0 \\
     * 0 & 0 & 1 \end{pmatrix}\f$
     */
    public Matrix() {
        x11 = 1.0;
        x12 = 0.0;
        x21 = 0.0;
        x22 = 1.0;
        tx = 0.0;
        ty = 0.0;
    }

    /**
     * Erzeugt eine Rotationsmatrix \f$\begin{pmatrix} cos(\alpha) &
     * -sin(\alpha) & 0 \\ sin(\alpha) & cos(\alpha) & 0 \\ 0 & 0 & 1
     * \end{pmatrix}\f$
     *
     * @param rotationAngle Rotationswinkel im Bogenmaß
     */
    public Matrix(double rotationAngle) {
        x11 = Math.cos(rotationAngle);
        x12 = -Math.sin(rotationAngle);
        x21 = Math.sin(rotationAngle);
        x22 = Math.cos(rotationAngle);
        tx = 0.0;
        ty = 0.0;
    }

    /**
     * Erzeugt eine Verschiebungsmatrix \f$\begin{pmatrix} 1 & 0 & dx \\ 0 & 1 &
     * dy \\ 0 & 0 & 1 \end{pmatrix}\f$
     *
     * @param translation Verschiebugnsvektor (dx,dy)
     */
    public Matrix(Vector translation) {
        x11 = 1.0;
        x12 = 0.0;
        x21 = 0.0;
        x22 = 1.0;
        tx = translation.getdx();
        ty = translation.getdy();
    }

    /**
     * Erzeugt eine Skalierungsmatrix \f$\begin{pmatrix} sx & 0 & 0 \\ 0 & sy &
     * 0 \\ 0 & 0 & 1 \end{pmatrix}\f$
     *
     * @param sx x Streckung
     * @param sy y Streckung Skalierungsvektor (dsx,dsy)
     */
    public Matrix(double sx, double sy) {
        x11 = sx;
        x12 = 0.0;
        x21 = 0.0;
        x22 = sy;
        tx = 0.0;
        ty = 0.0;
    }

    /**
     * Erzeugt eine Verschiebe-, Skalierungs-, Rotierungs- Matrix. Es wird erst
     * Skaliert, dann Verschoben, dann Rotiert
     *
     * @param xt x Verschiebung
     * @param yt y Verschiebung
     * @param a Drehwinkel
     * @param sx x Skalierung
     * @param sy y Skalierung
     */
    public Matrix(double xt, double yt, double a, double sx, double sy) {
        double c = Math.cos(a);
        double s = Math.sin(a);
        x11 = sx * c;
        x12 = -sy * s;
        x21 = sx * s;
        x22 = sy * c;
        tx = xt;
        ty = yt;
    }

    /**
     * Konvertiert die Matrix in die AWT Version der Matrix
     *
     * @return Java interne Matrix
     */
    public AffineTransform convert() {
        return new AffineTransform(x11, x21, x12, x22, tx, ty);
    }

    /**
     * Mulipliziert diese Matrix (M) mit dem Vektor v
     *
     * @param v Vektor v
     * @return \f$ M \times \vec{v}\f$
     */
    public Vector transform(Vector v) {
        double xa = v.getdx();
        double ya = v.getdy();
        double xb = xa * x11 + ya * x12 + tx;
        double yb = xa * x21 + ya * x22 + ty;
        return new Vector(xb, yb);
    }

    /**
     * Mulipliziert diese Matrix M mit dem Punkt p als Ortsvektor
     *
     * @param p Punkt p
     * @return \f$ M \times \vec{p}\f$
     */
    public Point transform(Point p) {
        double xa = p.getX();
        double ya = p.getY();
        double xb = xa * x11 + ya * x12 + tx;
        double yb = xa * x21 + ya * x22 + ty;
        return new Point(xb, yb);
    }

    /**
     * Transformiert eine Punktliste
     *
     * @param x Quell x {x1,x2,x3...}
     * @param y Quell y {y1,y2,y3..}
     * @param xt Transformiertes x {x1,x2,x3...}
     * @param yt Transformiertes x {y1,y2,y3...}
     */
    public void transform(double[] x, double[] y, double[] xt, double[] yt) {
        for (int i = 0; i < x.length; i++) {
            xt[i] = x[i] * x11 + y[i] * x12 + tx;
            yt[i] = x[i] * x21 + y[i] * x22 + ty;
        }
    }

    /**
     * Multipliziert diese Matrix M mit der Matrix N, \f$ M \times N\f$.
     *
     * @param N Matrix die Multipliziert wird
     * @return \f$ M \times N\f$
     */
    public Matrix transform(Matrix N) {
        Matrix u = new Matrix();
        u.x11 = x11 * N.x11 + x12 * N.x21;
        u.x12 = x11 * N.x12 + x12 * N.x22;
        u.x21 = x21 * N.x21 + x12 * N.x21;
        u.x22 = x21 * N.x12 + x22 * N.x22;
        u.tx = x11 * N.tx + x12 * N.ty + tx;
        u.ty = x21 * N.tx + x22 * N.ty + ty;
        return u;
    }

    /**
     * Konvertiert die Matrix in ein Feld.
     *
     * @return Felddaten der 3x3 Matrix
     */
    public double[] asArray() {
        return new double[]{x11, x12, tx, x21, x22, ty, 0d, 0d, 1d};
    }
}
