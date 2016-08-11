package de.lathanda.eos.interpreter.javacc;

import java.util.LinkedList;

import de.lathanda.eos.common.Marker;

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
