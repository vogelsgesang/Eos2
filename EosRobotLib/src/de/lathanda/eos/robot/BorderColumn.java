package de.lathanda.eos.robot;

public class BorderColumn extends Column {

	@Override
	public int isReachable(int level, int size, int climb, int fall) {
		return -1;
	}

	@Override
	public boolean isFree(int level, int size) {
		return false;
	}

}
