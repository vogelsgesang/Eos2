package de.lathanda.assembler.cpu;

public class ControlUnit {
	private Memory mem;
	private ALU alu;
	private Register ir;
	private Register mar;
	private Register[] r;
	public ControlUnit(Memory mem, ALU alu) {
		super();
		this.mem = mem;
		this.alu = alu;
		r = new Register[16];
		for(int i = 0; i < r.length; i++) {
			r[i] = new Register();
		}
		ir = new Register();
		mar = new Register();
	}
	public void init(int address) {
		mar.setValue(address);
	}
	public void next() {
		ir.setValue(nextCell().getInt());
		decode();
		execute();
	}
	private void execute() {
		// TODO Auto-generated method stub
		
	}
	private void decode() {
		Cell code = ir.getCell();
		
		int opcode = code.getBits(0, 8);
		Cell eaSrc  = getEffectiveAddress(code.getBits( 8, 4), code.getBits(12, 4), code.getBits(16, 4));
		Cell eaDest = getEffectiveAddress(code.getBits(20, 4), code.getBits(24, 4), code.getBits(28, 4));
		switch(opcode) {
		
		}
		
	}
	private Cell getEffectiveAddress(int addrMode, int register, int index) {
		switch (addrMode) {
		case 0b0000: // direct register
			return r[register].getCell();
		case 0b0001: // indirect register
			return mem.getMemoryCell(r[register].getInt());
		case 0b0010: //indirect register post increment
			Cell cell = mem.getMemoryCell(r[register].getInt());
			r[register].increment();
			return cell;
		case 0b0011: //indirect register pre decrement
			r[register].decrement();
			return mem.getMemoryCell(r[register].getInt());
		case 0b0100: //indirect register + displacement
			return mem.getMemoryCell(r[register].getInt() + nextCell().getInt());
		case 0b0101: //indirect + index
			return mem.getMemoryCell(r[register].getInt() + r[index].getInt());
		case 0b0111: //absolute
			return mem.getMemoryCell(nextCell().getInt());
		case 0b1000: //direct
			return nextCell();
		case 0b1001: //absolut + index
			return mem.getMemoryCell(nextCell().getInt() + r[index].getInt());
		default:
			throw new IllegalOperationException();
		}
	}
	private Cell nextCell() {
		Cell cell = mem.getMemoryCell(mar.getInt());
		mar.increment();
		return cell;
	}
}
