package de.lathanda.eos.robot.geom3d;

public class NFaceException extends Exception {

	private static final long serialVersionUID = 7719784100428330155L;
	private int faces;
	NFaceException(int n){
		this.faces = n;
	}
	public int getFaces() {
		return faces;
	}
}
