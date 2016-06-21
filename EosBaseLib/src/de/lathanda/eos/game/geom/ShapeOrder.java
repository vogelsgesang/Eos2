package de.lathanda.eos.game.geom;

/**
 * \brief Formordnung
 *
 * Dieser Kontainer dient dazu Formen unterschiedlich nach ihren Rändern zu
 * sortieren.
 *
 * @author Lathanda
 *
 */
class ShapeOrder implements Comparable<ShapeOrder> {

    /**
     * \brief Sortiermodus
     */
    public static enum OrderType {

        /// nach linker Grenze

        X0,
        /// nach rechter Grenze
        X1,
        /// nach unterer Grenze
        Y0,
        /// nach obere Grenze
        Y1
    };

    /**
     * \brief Klammerart
     */
    public static enum Bracket {

        OPEN(1), CLOSE(2);
        public final int order;

        private Bracket(int order) {
            this.order = order;
        }
    };

    /**
     * \brief Wert
     */
    public final double value;
    /**
     * \brief Klammerart
     */
    public final Bracket bracket;
    /**
     * \brief Umriss
     */
    public final Shape shape;

    /**
     * Neuer Sortierkontainer
     *
     * @param type Klammerart
     * @param outline Umriss
     */
    public ShapeOrder(OrderType type, Shape shape) {
        this.shape = shape;
        switch (type) {
            case X0:
                value = shape.getLeft();
                bracket = Bracket.OPEN;
                break;
            case X1:
                value = shape.getRight();
                bracket = Bracket.CLOSE;
                break;
            case Y0:
                value = shape.getBottom();
                bracket = Bracket.OPEN;
                break;
            case Y1:
                value = shape.getTop();
                bracket = Bracket.CLOSE;
                break;
            // BEGIN DELETE ME
            default:
			// We need the default case as the java compiler is unable to
                // process enumerations correctly
                // This is really dangerous and bad code, as it will prevent this
                // code from throwing a
                // compile time error if someone extends the enumeration
                // sometimes Java just sucks (20080830)
                bracket = Bracket.OPEN;
                value = 0;
                System.err.println("unknown order type occured, incomplete code change");
                System.exit(-1);
            // END DELETE ME
        }
    }

    /**
     * Sortierregel
     */
    @Override
    public int compareTo(ShapeOrder o) {
        double diff = value - o.value;
        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return +1;
        } else {
            /*
             * Remark: we may only return 0 if the two objects are really
             * identical, identical in our case means same type AND same id
             */
            int result = shape.id - o.shape.id;
            if (result != 0) {
                return result;
            } else {
                return bracket.order - o.bracket.order;
            }
        }
    }

    /**
     * Liefert eine lesbare Version des Objektes
     *
     * @return Informationen
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        switch (bracket) {
            case OPEN:
                result.append("open");
                break;
            case CLOSE:
                result.append("close");
        }
        result.append(value);
        return result.toString();
    }
}
