package de.lathanda.assembler.cpu;

public class ControlUnit {
	private Memory mem;
	private ALU alu;
	private Register ir;
	private Register mar;
	public ControlUnit(Memory mem) {
		super();
		this.mem = mem;
		alu = new ALU();
		ir = new Register();
		mar = new Register();
	}
	public void init(int address) {
		mar.setValue(address);
	}
	public void next() {
		ir.setValue(mem.read(mar.getInteger()));
		decode();
		readOperands();
		execute();
	}
	private void execute() {
		// TODO Auto-generated method stub
		
	}
	private void readOperands() {
		// TODO Auto-generated method stub
		
	}
	private void decode() {
		// TODO Auto-generated method stub
		
	}
}
