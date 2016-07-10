package de.lathanda.assembler.cpu;

public class Machine {
	private Memory memory;
	private ControlUnit cu;
	private ALU alu;
	public Machine() {
		memory = new Memory();
		alu = new ALU();
		cu = new ControlUnit(memory, alu);
		
	}
	public void singleOperation() {
		cu.next();
	}
	
}
