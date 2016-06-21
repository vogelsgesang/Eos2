package de.lathanda.eos.game.geom;

/**
 * \brief Formarten
 *
 * Aufzählung aller bekannten Formarten.
 *
 * @author Lathanda
 *
 */
enum Types {

    /**
     * Undefinierte Form, hier wird nur das Rahmenrechteck berücksichtigt
     */
    UNDEFINED,
    /**
     * Rechteckige Form
     */
    RECTANGLE,
    /**
     * Polygonform
     */
    POLYGON,
    /**
     * Kreisform
     */
    CIRCLE,
    /**
     * Punkt
     */
    DOT,
    /**
     * Formen Gruppe
     */
    GROUP
}
