package de.lathanda.assembler.cpu;

public class Register {
	private Cell mem;
	public Cell getCell() {
		return mem;
	}
	public void setValue(int value) {
		mem.setInt(value);		
	}
	public void set(Cell c) {
		mem.set(c);
	}
	public void set(Register r) {	
		mem.set(r);
	}
	public void setValue(float value) {
		mem.setFloat(value);
	}
	public int getInt() {
		return mem.getInt();
	}
	public float getFloat() {
		return mem.getFloat();
	}
	public void increment() {
		mem.increment();
	}
	public void decrement() {
		mem.decrement();
	}
}
