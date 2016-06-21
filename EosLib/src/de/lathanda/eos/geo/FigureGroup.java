package de.lathanda.eos.geo;

/**
 * Kontainer für Figuren.
 * Dies ist eine gemeinsame Schnittstelle für Fenster und Gruppen.
 *
 * @author Peter (Lathanda) Schneider
 */
interface FigureGroup {
    /**
     * Fügt eine Figur einer Gruppe hinzu. Hierbei werden alle 
     * Transformationen auf das Koordinatensystem der Gruppe umgestellt.
     * Jede Figur kann nur in einer Gruppe sein, daher wird sie implizit
     * aus der vorherigen Gruppe entfernt, bevor sie hier hinzugefügt wird.
     * @param figure Figur
     */
    void addFigure(Figure figure);
    /**
     * Entfernt eine Figur aus einer Gruppe. Hierbei wird das Koordinatensystem
     * wieder hergestellt.
     * Die Figur wird hierdurch unsichtabr, da sie keinen Kontext mehr besitzt,
     * der sie anzeigen würde.
     * @param figure Figur 
     */
    void removeFigure(Figure figure);
    /**
     * Diese Methode informiert rekursiv alle Kontainer, 
     * dass sich Daten geändert haben, welche die Darstellung beeinflussen.
     */
    void fireDataChanged();
    /**
     * Diese Methode dient dazu das Durchlaufen einer Gruppenhierarchie
     * zu vereinfachen.
     * 
     * @return Die Figurengruppe oder null, wenn es ein Fenster ist.
     */
    Group getGroup();
}
