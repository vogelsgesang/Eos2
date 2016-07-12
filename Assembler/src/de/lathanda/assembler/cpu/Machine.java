package de.lathanda.assembler.cpu;

public class Machine {
	private Memory memory;
	private ControlUnit cu;
	private ALU alu;
	public Machine() {
		memory = new Memory();
		alu = new ALU(memory.getMemoryCell(0), memory.getMemoryCell(0));
		cu = new ControlUnit(memory, alu);
		
	}
	public void singleOperation() {
		cu.next();
	}
	
}
