package de.lathanda.assembler.cpu;

public class ALU {
	private Cell source;
	private Cell destination;
	private ConditionCodeRegister ccr;
	public ALU(Cell source, Cell destination) {
		this.ccr = new ConditionCodeRegister();
		this.source = source;
		this.destination = destination;
	}

	
	public void setSource(Cell source) {
		this.source = source;
		
	}
	public void setDestination(Cell destination) {
		this.destination = destination;
	}
	
	public void add() {
		long temp = source.getLong() + destination.getLong();
		ccr.setCarry((temp & (1<<32)) != 0);
		ccr.setZero(temp == 0);
		ccr.setNegative(temp < 0);
		destination.setLong(temp);
	}
	public void subtract() {
		long temp = source.getLong() - destination.getLong();
		ccr.clearCarry();
		ccr.setZero(temp == 0);
		ccr.setNegative(temp < 0);
		destination.setLong(temp);
	}

}
