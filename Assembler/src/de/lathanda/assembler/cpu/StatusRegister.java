package de.lathanda.assembler.cpu;

public class StatusRegister extends Register {
	private Cell mem;
	private static final int ZERO = 0;
	private static final int NEGATIVE = 1;
	private static final int CARRY = 2;
	public boolean getCarry() {
		return mem.getBit(CARRY) == 0b1;
	}
	public void setCarry() {
		mem.setBit(CARRY);
	}
	public boolean getZero() {
		return mem.getBit(ZERO) == 0b1;
	}
	public void setZero() {
		mem.setBit(ZERO);
	}
	public boolean getNegative() {
		return mem.getBit(NEGATIVE) == 0b1;
	}
	public void setNEgative() {
		mem.setBit(NEGATIVE);;
	}
}
