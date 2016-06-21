package de.lathanda.eos.game.tools;

import java.util.Random;

/**
 *
 * @author Lathanda
 *
 * \brief Klasse für Zufallslabyrinthe
 *
 * Die Klasse kann zufällig wirkende Labyrinthe erzeugen die über die Parameter
 * konfiguriert werden. Man kann entweder das gesamte Labyrinth füllen oder
 * Teilbereiche. Werden für verschiedene Bereiche unterschiedliche Werte
 * benutzt, kann das Labyrinth interessanter werden, es besteht jedoch
 * potentiell die Gefahr, dass es nicht mehr zusammenhängend ist. Von sich aus
 * kennt das Labyrinth nur {@link #WALL} und {@link #FREE}. Per
 * {@link #setCell(int, int, int)} können andere Werte ergänzt werden oder man
 * lässt sich das gesamt Labyrinth per {@link #getLabyrinth()} geben.
 *
 * @author Lathanda
 * @version 2.0.4
 * @since 2.0.0
 */


public class Labyrinth {

    /**
     * \brief Leeres Feld
     */
    public static final int FREE = 0;
    /**
     * \brief Wand
     */
    public static final int WALL = 1;
    /**
     * \brief Breite
     */
    private int width;
    /**
     * \brief Höhe
     */
    private int height;
    /**
     * \brief Zellen
     */
    private int[][] cell;

    /**
     * Erzeugt ein neues leeres Labyrinth
     *
     * @param width Breite
     * @param height Höhe
     */
    public Labyrinth(int width, int height) {
        this.width = width;
        this.height = height;
        cell = new int[width][height];
        clear();
    }

    /**
     * Versetzt das Labyrinth in den Grundzustand. Außen Wände innen frei.
     */
    public void clear() {
        //center
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cell[x][y] = FREE;
            }
        }
        //border
        for (int i = 0; i < width; i++) {
            cell[i][0] = WALL;
            cell[i][height - 1] = WALL;
        }
        for (int i = 0; i < height; i++) {
            cell[0][i] = WALL;
            cell[width - 1][i] = WALL;
        }
    }

    /**
     * Befüllt das gesamte Labyrinth mit Wänden. Es wird implizit
     * {@link Labyrinth#clear()} aufgerufen.
     *
     * @param labNr Seed für den Zufallsgenerator. Für jeden Werte von labNr
     * wird immer das gleiche Labyrinth erzeugt!
     * @param wallNr Anzahl Wände
     * @param raster Rasterweite, die Gänge sind (Raster - 1) breit
     */
    public void createLabyrinth(int labNr, int wallNr, int raster) {
        clear();
        createLabyrinth(labNr, wallNr, raster, 1, 1, width - 2, height - 2);
    }

    /**
     * Füllt einen Bereich mit Labyrinthwänden.
     *
     * @param labNr Seed für den Zufallsgenerator. Für jeden Werte von labNr
     * wird immer das gleiche Labyrinth erzeugt!
     * @param wallNr Anzahl Wände
     * @param raster Rasterweite, die Gänge sind (Raster - 1) breit
     * @param xa linke Grenze
     * @param ya untere Grenze
     * @param wa Breite
     * @param ha Höhe
     */
    public void createLabyrinth(int labNr, int wallNr, int raster, int xa, int ya, int wa, int ha) {
        createLabyrinth(new Random(labNr), wallNr, raster, xa, ya, wa, ha);
    }

    /**
     * Füllt einen Bereich mit Labyrinthwänden.
     *
     * @param dice Zufallsgenerator
     * @param wallNr Anzahl Wände
     * @param raster Rasterweite, die Gänge sind (Raster - 1) breit
     * @param xa linke Grenze
     * @param ya untere Grenze
     * @param wa Breite
     * @param ha Höhe
     */
    public void createLabyrinth(Random dice, int wallNr, int raster, int xa, int ya, int wa, int ha) {
        createLabyrinth(dice, wallNr, raster, xa, ya, wa, ha, wa / raster / 2);
    }

    /**
     * Füllt einen Bereich mit Labyrinthwänden.
     *
     * @param dice Zufallsgenerator
     * @param wallNr Anzahl Wände
     * @param raster Rasterweite, die Gänge sind (Raster - 1) breit
     * @param xa linke Grenze
     * @param ya untere Grenze
     * @param wa Breite
     * @param ha Höhe
     * @param wallLength maximale Wandlänge
     */
    public void createLabyrinth(Random dice, int wallNr, int raster, int xa, int ya, int wa, int ha, int wallLength) {
        final int MIN_LENGTH = 1;
        int DIFF_LENGTH = wallLength;

        //fill up labyrinth
        for (int m = 0; m < wallNr; m++) {
            int x = raster * dice.nextInt((wa - 1) / raster) + xa + 1;
            int y = raster * dice.nextInt((ha - 1) / raster) + ya + 1;
            int direction = dice.nextInt(4);
            int length = raster * (dice.nextInt(DIFF_LENGTH) + MIN_LENGTH) + 1;
            for (int i = 0; i < length; i++) {
                int xw;
                int yw;
                switch (direction) {
                    case 0:
                        xw = x;
                        yw = y + i;
                        break;
                    case 1:
                        xw = x + i;
                        yw = y;
                        break;
                    case 2:
                        xw = x;
                        yw = y - i;
                        break;
                    default:
                        xw = x - i;
                        yw = y;
                        break;
                }
                if (xa <= xw && xw < xa + wa
                        && ya <= yw && yw < ya + ha
                        && cell[xw][yw] != WALL) {
                    cell[xw][yw] = WALL;
                } else {
                    break;
                }

            }
        }
    }

    /**
     * Liefert die Breite des Labyrinths
     *
     * @return Breite
     */
    public int getWidth() {
        return width;
    }

    /**
     * Liefert die Höhe des Labyrinths
     *
     * @return Höhe
     */
    public int getHeight() {
        return height;
    }

    /**
     * Liefert den Inhalt der Zelle
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @return Der Wert der Zelle WALL oder FREE, wenn setCell() nie benutzt
     * wurde
     */
    public int getCell(int x, int y) {
        return cell[x][y];
    }

    /**
     * Setzt die Zelle auf einen neuen Wert, x,y wird nicht geprüft
     *
     * @param x x Koordinate
     * @param y y Koordinate
     * @param value Neuer Wert
     */
    public void setCell(int x, int y, int value) {
        cell[x][y] = value;
    }

    /**
     * Liefert das gesamte Labyrinth als Feld zurück.
     *
     * @return Labyrinth Daten
     */
    public int[][] getLabyrinth() {
        return cell;
    }
}
