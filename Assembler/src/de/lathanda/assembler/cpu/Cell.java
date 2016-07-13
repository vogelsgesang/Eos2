package de.lathanda.assembler.cpu;

public class Cell {
	private int content;
	public Cell() {
		this.content = 0;
	}
	public int getInt() {
		return content;
	}
	public void setInt(int value) {
		this.content = value;
	}
	public long getLong() {
		return (long)content;
	}
	public void setLongLow(long value) {
		this.content = (int)value;
	}
	public void setLongHigh(long value) {
		this.content = (int)(value >> 32);
	}
	public float getFloat() {
		return Float.intBitsToFloat(content);
	}
	public void setFloat(float value) {
		this.content = Float.floatToIntBits(value);
	}
	/**
	 * Liefert die Bits gezählt von links 0 bis ganz rechts 31
	 * @param from
	 * @param to
	 * @return
	 */
	public int getBitRange(int from, int to) {
		return (this.content << from) >> (32 - to + from);
	}
	public int getBits(int from, int length) {
		return (this.content << from) >> (32 - length);
	}
	public int getBit(int index) {
		return (this.content & (1 << index)) >> index;
	}
	public void setBit(int index) {
		this.content = this.content | (1 << index);
	}
	public void clearBit(int index) {
		this.content = this.content & ~(1 << index);
	}
	public void setBit(int index, boolean value) {
		if (value) {
			setBit(index);
		} else {
			clearBit(index);
		}
	}
	public void toggleBit(int index) {
		this.content = this.content ^ (1 << index);
	}
	public void increment() {
		content++;
	}
	public void decrement() {
		content--;
	}
}
