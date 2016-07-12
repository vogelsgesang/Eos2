package de.lathanda.assembler.cpu;

public class ConditionCodeRegister extends Register {
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
	public void setCarry(boolean carry) {
		mem.setBit(CARRY, carry);
	}
	public void clearCarry() {
		mem.clearBit(CARRY);
	}
	public boolean getZero() {
		return mem.getBit(ZERO) == 0b1;
	}
	public void setZero(boolean zero) {
		mem.setBit(ZERO, zero);
	}
	public void clearZero() {
		mem.clearBit(ZERO);
	}
	public void setZero() {
		mem.setBit(ZERO);
	}
	public boolean getNegative() {
		return mem.getBit(NEGATIVE) == 0b1;
	}
	public void setNegative() {
		mem.setBit(NEGATIVE);
	}
	public void setNegative(boolean negative) {
		mem.setBit(NEGATIVE, negative);
	}
	public void clearNegative() {
		mem.clearBit(NEGATIVE);
	}
}
