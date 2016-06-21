package de.lathanda.eos.robot;

import java.util.ArrayList;
import de.lathanda.eos.robot.exceptions.CubeImmutableException;
import de.lathanda.eos.robot.exceptions.CubeMissingException;

/**
 * Eine Säule innerhalb der Welt.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class Column {
	private CubeArray cubes = new CubeArray();
	private boolean isMarked = false;

    /** 
     * Lässt einen Würfel ausgehend vom Level her "fallen".
     * Ist das Level frei fällt der Stein bis er auf den Boden oder ein nicht freies Feld stösst.
     * Ist das Level bereits belegt wird der Würfel auf die erste frei stelle darüber platziert.
     * @param level Setzhöhe
     * @param cube Würfel
     */
    public void dropCube(int level, Cube cube) {
        if(cubes.get(level).isFree()) {
        	int n = level;
        	while (cubes.get(n-1).isFree()) { n--; }
        	cubes.set(n, cube);
        } else {
        	int n = level+1;
        	while (!cubes.get(n).isFree()) {
        		n++;
        	}
        	cubes.set(n, cube);
        }
    }
    /**
     * Lässt einen Würfel von ganz oben fallen.
     * Er bleibt liegen sobald er auf ein belegtes Feld oder den Boden stösst.
     * @param cube Würfel
     */
    public void dropCube(Cube cube) {
    	//drop from above
    	dropCube(cubes.size(), cube);
    }
    /**
     * Setzt eine Markierung.
     * @param mark markieren oder nicht
     */
    public void setMark(boolean mark) {
    	isMarked = mark;      
    }
    /**
     * Wechselt den Markierungszustand
     */
    public void toggleMark() {
    	isMarked = ! isMarked;
    }
    /**
     * Prüft ob ein Schritt möglich ist.
     * Dazu wird im bereich level - fall, ...., level, ..., level + climb
     * geprüft ob es freien Platz gibt.
     * Bei sehr großen Werten von fall und climb kann der Schritt mehrdeutig werden.
     * Hier wird versuch den Höhenunterschied möglichst gering zu halten, im Zweifel geht es nach unten.
     * 
     * @param level Ausgangshöhe 
     * @param size Höhe der Figur
     * @param climb maximaler erlaubter Höhenunterschied
     * @param fall maximal erlaubte Fallhöhe
     * @return
     */
    public int isReachable(int level, int size, int climb, int fall) {
    	int maxDifference = Math.min(fall,  climb);
    	int difference = 0;
    	while (difference <= maxDifference) {
  			if (isWalkable(level - difference, size)) {
   				return level - difference;
    		}
    		if (isWalkable(level + difference, size)) {
    			return level + difference;
    		}
    		difference++;
    	}
    	if (fall < climb) {
    		while (difference <= climb) {
        		if (isWalkable(level + difference, size)) {
        			return level + difference;
        		}    	
        		difference++;
    		}
    	} else {
    		while (difference <= fall) {
        		if (isWalkable(level - difference, size)) {
        			return level - difference;
        		}    	
        		difference++;
    		}
    	}
        return -1;
    }
    /**
     * Prüft ob der Ort begehbar ist. Dazu muss ein fester untergrund existieren
     * und es müssen genügend Felder frei sein.
     * 
     * @param level
     * @param size
     * @return
     */
    private boolean isWalkable(int level, int size) {
        if (cubes.get(level - 1).isFree()) {
            return false; //check solid ground
        }
        for (int i = size; i-- > 0;) {
            if (!cubes.get(level + i).isFree()) {
                return false; //check free space
            }
        }
        return true;
    }
    /**
     * Prüft ob die Position genug Platz bietet.
     * Es wird nicht geprüft, ob dies ein legaler Schritt ist.
     * 
     * 
     * @param level
     * @param size
     * @return
     */
    public boolean isFree(int level, int size) {
        for (int i = size; i-- > 0;) {
            if (!cubes.get(level + i).isFree()) {
                return false; //check free space
            }
        }
        return true;
    }

    /**
     * Hebt den obersten Würfel auf.
     */
    public void pickup() throws CubeMissingException , CubeImmutableException {
        pickup(cubes.size());
    }
    /**
     * Hebt einen Würfel ausgehend von der Position auf.
     * Ist die Position gefüllt, wird der oberste Würfel dieses Stapels verwendet.
     * Ist die Position frei wird der erste Würfel genommen der unterhalb ist.
     * @param level
     */
    void pickup(int level) throws CubeMissingException , CubeImmutableException {
    	if (cubes.get(level).isFree()) {
    		for(int n = level; n --> 0;) {
    			if(!cubes.get(n).isEmpty()) {
    				removeCube(n);
    			}
    		}
    	} else {
    		int n = level;
    		while (!cubes.get(n+1).isFree()) {n++;}
    		removeCube(n);
    	}
    }
    /**
     * Prüft ob die Position markiert ist.
     * @return
     */
    public boolean isMarked() {
        return isMarked;
    }
    public void setMark() {
    	isMarked = true;
    }

    /**
     * Löscht ein Feld
     * @param level
     * @throws CubeImmutableException 
     */
	public void removeCube(int level) throws CubeImmutableException {
		cubes.get(level).setEmpty();
	}
	/**
	 * Prüft ob der Stapel leer ist.
	 * @return
	 */
	public boolean hasCube() {
		for (int i = 0; i < cubes.size(); i++) {
			if (!cubes.get(i).isFree()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Prüft ob der Stapel exakt n Würfel enthält.
	 * @param n
	 * @return
	 */
	public boolean hasCube(int n) {
		int numberOfCubes = 0;
		for (int i = 0; i < cubes.size(); i++) {
			if (!cubes.get(i).isFree()) {
				numberOfCubes++;
			}
		}
		return numberOfCubes == n;
	}

	public void setCube(int level, Cube cube) {
		cubes.set(level, cube);
	}
	public Cube[] getCubes() {
		return cubes.getAll();
	}
	/**
	 * Entfernt einen Würfel vollständig inklusive Markierung
	 * @param level
	 */
	public void remove(int level) {
		cubes.set(level, Cube.createEmpty());
	}
	private static class CubeArray {
	    private ArrayList<Cube> cubes = new ArrayList<>();
	    /**
	     * Die Hilfsmethode liefert einen Würfel für eine Höhe.
	     * Negative Wert liefert einen Bodenwürfel.
	     * Werte außerhalb des gültigen Bereichs liefern einen Leerwürfel.
	     * 
	     * @param index
	     * @return
	     */
	    private synchronized Cube get(int level) {
	        if (level < 0) {
	            return Cube.createGround();
	        }
	        if (level < cubes.size()) {
	            return cubes.get(level);
	        } else {
	            return Cube.createEmpty();
	        }
	    }    
	    /**
	     * Setzt einen neuen Würfel. Hierbei werden Lücken leer aufgefüllt.
	     * @param level
	     * @param cube
	     */
	    public synchronized void set(int level, Cube cube) {
	    	//fill up
	        for (int n = level - cubes.size() + 1; n --> 0;) {
	        	cubes.add(Cube.createEmpty());
	        }
	        //set cube
	        cubes.set(level,  cube);
	        //clean up
	        int n = cubes.size() - 1;
	        while ( n >= 0 && cubes.get(n).isEmpty()) {
	        	cubes.remove(n);
	        	n--;
	        }
	    }

		/**
		 * Liefert eine Liste der Würfel.
		 * 
		 * @return
		 */
		public synchronized Cube[] getAll() {
			Cube[] cubeArray = new Cube[cubes.size()];
			return cubes.toArray(cubeArray);
		}	
		public int size() {
			return cubes.size();
		}
	}
}
