package de.lathanda.eos.interpreter.javacc;

import de.lathanda.eos.interpreter.Marker;
import java.util.LinkedList;

/**
 * Diese Klasse verpackt ein Template, da javacc 
 * die Templatenotation nicht versteht.
 * <> erzeugen einen Fehler. 
 * 
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class MarkerStack extends LinkedList<Marker> {
    private static final long serialVersionUID = -4276688126529265580L;
}
