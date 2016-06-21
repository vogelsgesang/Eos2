package de.lathanda.eos.game;

import java.util.Objects;

import de.lathanda.eos.base.Picture;
import de.lathanda.eos.game.geom.Shape;
import java.awt.Color;

/**
 * \brief Spielobjekt
 *
 * Die Klasse dient als Oberklasse für alle Elemente eines Spiels.
 *
 * Aufrufsequenz init -> (update, processCollision)*  | (render)* -> cleanup
 *
 * Die {@link Sprite#update(Game)} Methode kann redefiniert werden, 
 * um Sprites zeitgesteuert
 * zu verändern. Entweder weil eine Benutzereingabe erfolgt ist,
 * weil genügend Runden vergangen sind oder einfach immer. Es wird garantiert,
 * dass die Methode im Mittel im Takt aufgerufen wird. Also auf jedem Rechner
 * gleich oft pro Sekunde. Standard ist 30 Aufrufe pro Sekunde. Über die
 * Methoden {@link Sprite#sleep(int)} kann man den Aufruf der
 * {@link Sprite#update()} Methode aussetzen.
 *
 * Die {@link Sprite#render(Picture)} Methode kann redefiniert werden, um den Sprite
 * darzustellen. Es ist nicht zu empfehlen hier
 * Attribute zu ändern! Die Methode wird abhängig von der Rechenleistung des
 * Rechners und der Grafikkarte, öfter oder weniger oft aufgerufen. Hier
 * Attribute zu ändern, kann also unsinniges Verhalten erzeugen (Slowmotion,
 * Zeitraffer etc.).
 *
 * Benötigt man eine Kollisionsberechnung, muss man das Attribut shape 
 * sinnvoll befüllen. Es handelt sich hier um die Bounding Box des Sprites.
 *
 * Die Gameengine überprüft zwischen zwei Runden, ob sich zwei Bounding Boxen 
 * überlappen. Ist dies der Fall wird {@link #processCollision(Sprite, Game)} aufgerufen.
 *
 * Hier prüft man mit welchem Sprite man kollidiert ist und reagiert
 * entsprechend.
 *
 * 
 *
 * @author Lathanda
 *
 */
public abstract class Sprite implements Comparable<Sprite> {

    /**
     * \brief ID Zähler
     */
    private static int nextID = 1;
    /**
     * \brief ID des Objekts.
     */
    private final Integer id = nextID++;

    /**
     * \brief Bounding Box des Sprites
     */
    protected Shape shape;
    /**
     * \brief Zeichenebene
     *
     * Die Sprites werden in der Reihenfolge der Zeichenebene gezeichnet. Je
     * kleiner desto später. Hierdurch überzeichnen Weltobjekte mit kleinerer
     * Nummer andere Objekte mit großer Nummer. Der Standardwert ist 0. Negativ
     * bedeutet Vordergrund. Positiv bedeutet Hintergrund.
     *
     */
    /*package*/ final int plane;
    /**
     * \brief Anzahl der vergangenen Runden seit Objekterzeugung
     */
    protected long round = 0;
    /**
     * \brief Globale Runde in der das Objekt erzeugt wurde.
     */
    private long start = -1;
    /**
     * \brief Objekt will bis zu dieser Runde warten
     */
    private long sleepUntil = -1;

    /**
     * Konstruktor
     */
    public Sprite() {
    	plane = 0;
    }
    public Sprite(int plane) {
    	this.plane = plane;
    }

    /**
     * Frägt die ID ab
     *
     * @return Die ID des Objekts
     */
    public final Integer getID() {
        return id;
    }

    /**
     * Hashwert für die Indizierung
     */
    @Override
    public final int hashCode() {
        /*
         * this value should be more predictable
         */
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sprite other = (Sprite) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Vergleichoperator für Sortierung
     * @param w
     */
    @Override
    public final int compareTo(Sprite w) {
        if (w == null) {
            return Integer.MIN_VALUE;
        }
        int v = w.plane - plane;
        return (v == 0) ? w.id - id : v;
    }

    /**
     * Diese Methode wird vor dem Eintritt in das Spiel aufgerufen. Unterklassen
     * sollten Sie überschreiben, um Code vor dem ersten update Aufruf
     * auszuführen.
     */
    public void init(Game game) {
    }

    /**
     * Diese Methode wird nach dem Verlassen des Spiels aufgerufen. Unterklassen
     * sollten Sie überschreiben, um Aufzuräumen.
     * <ul>
     * <li> abhängige Sprites entfernen
     * <li> Resourcenfreigeben (Bilder, Fenster, Dateien etc.)
     * </ul>
     * @param game Die Spielwelt
     */
    public void cleanup(Game game) {
    }

    /**
     * Der Sprite soll sich zeichnen, abhängig von seinen Attribute. Alle
     * Längenangaben sind standardmäßig in mm. Das Koordinatensystem ist
     * zentriert mit x nach rechts und y nach oben. Genaueres kann bei
     * {@link de.lathanda.eos.base.Picture} nachgelesen werden.
     *
     * Beim Zeichnen werden in der Regel viele Attribute gelesen, aber nicht
     * verändert. Die Veränderung der Attribute erfolgt in
     * {@link Sprite#update(Game)}.
     *
     * @param p Grafikobjekt auf das gezeichnet werden soll.
     */
    public void render(Picture p) {
    	if (shape != null) {
    		p.setFillColor(Color.GREEN);
    		p.setLineColor(Color.GRAY);
    		p.drawShape(shape);
    	}
    };

    /**
     * Diese Methode dient internen Zwecken.
     * @param round Die aktuelle Runde
     * @param game Die Spielwelt
     */
    public final void update(long round, Game game) {
        if (start == -1) {
            start = round - 1;
        }
        this.round = round - start;
        if (this.round > sleepUntil) {
            update(game);
        }
    }

    /**
     * Unterklassen müssen diese Methode überschreiben um zeitabhängig ihre
     * Attributwert zu verändern. Etwa zur Simulation physikalischer Vorgänge
     * mit der Methode kleiner Schritte.
     *
     * Es gibt hier eine Trennung update() ändert die Werte, render() stellt sie
     * dar.
     *
     * Es wird versucht, dass die Methode immer im gleichen Zeitabstand
     * aufgerufen wird. Wird dies durch nicht zu beeinflussende Randbedingungen,
     * wie Betriebssystem und Rechnerlast verhindert, werden fehlende Intervalle
     * beschleunigt nachgeholt.
     *
     */
    public void update(Game game) {
    	
    };

    /**
     * Diese Methode lässt diesen Sprite einige Runden schlafen. Befinden wir
     * uns in Runde 23 und rufen sleep(5) auf, wird update() das nächste mal in
     * Runde 29 aufgerufen. Die Runden 24,25,26,27,28 werden verschlafen. Es
     * wirkt nur der letzte Aufruf, die Werte werden nicht addiert.
     *
     * @param rounds Anzahl der Runden die geschlafen werden soll.
     */
    public final void sleep(int rounds) {
        sleepUntil = round + rounds;
    }

    /**
     * Lässt das Objekt für unbestimmte Zeit schlafen. Siehe wakeup()
     */
    public final void sleep() {
        sleepUntil = Long.MAX_VALUE;
    }

    /**
     * Hebt den Effekt von {@link Sprite#sleep()} und {@link Sprite#sleep(int)}
     * auf
     */
    public final void wakeup() {
        sleepUntil = -1;
    }
    /**
     * Eine Kollision zwischen diesem Objekt und dem Objekt b hat stattgefunden.
     * Es soll die Kollision behandelt werden. Wird wahr zurückgeliefert wird
     * die Kollision als erledigt betrachtet und b.processCollision(this) nicht
     * mehr aufgerufen. Wird falsch zurück geliefert wird die
     * Kollisionsbehandlung an das Objekt b weiter gereicht. Man sollte wahr
     * zurück liefern, wenn b nicht mehr auf gerufen werden soll.
     *
     * Eine Implementierung könnte zum Beispiel wie folgt aussehen... 
     * \code 
     * if (b instanceof Bonus) { 
     *   punkt = punkt + 100; 
     * } 
     * if (b instanceof Niete) {
     *   punkte = punkte - 300; 
     * } 
     * return false; 
     * \endcode 
     * Bei dieser Variante müsse
     * die Klasse Bonus ebenfalls die Kollision behandeln. Man hätte aber auch
     * folgendes schreiben können ... 
     * \code 
     * if (b instanceof Bonus) { 
     *   punkt = punkt + 100; 
     *   game.removeSprite(b); 
     *   return true; 
     * } 
     * if (b instanceof Niete) { 
     *   punkte = punkte - 300; 
     *   game.removeSprite(b); 
     *   return true; 
     * }
     * return false; 
     * \endcode 
     * Diesmal macht es Sinn wahr zurückzuliefern, da die
     * Kollision schon fertig behandelt ist. Im Zweifel ist falsch aber die
     * bessere Wahl.
     *
     * @param b mit diesem Objekt kollidiert
     * @param game Die Spielwelt
     * @return wahr, wenn die Kollision komplett behandelt wurde.
     */
    public boolean processCollision(Sprite b, Game game) {
        return false;
    }    

    public int getPlane() {
    	return plane;
    }
    public Shape getShape() {
    	return shape;
    }
  
}
