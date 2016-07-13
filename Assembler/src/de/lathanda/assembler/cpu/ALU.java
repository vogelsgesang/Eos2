package de.lathanda.assembler.cpu;

public class ALU {
	private Cell source1;
	private Cell source2;
	private Cell destination;
	private Cell destinationOverflow;
	private Cell nil;
	private long result = 0l;
	private ConditionCodeRegister ccr;
	public ALU() {
		this.ccr = new ConditionCodeRegister();
		nil = new Cell();
		this.source1 = nil;
		this.source2 = nil;
		this.destination = nil;
		this.destinationOverflow = nil;
	}

	
	public void setSource(Cell source1, Cell source2) {
		this.source1 = source1;
		this.source2 = source2;
		
	}
	public void setDestination(Cell destination, Cell destinationOverflow) {
		this.destination = destination;
		this.destinationOverflow = destinationOverflow;
	}
	public void setDestination(Cell destination) {
		this.destination = destination;
		destinationOverflow = nil;
	}
	public void setTarget(Cell target) {
		source1 = target;
		destination = target;
		source2 = nil;
		destinationOverflow = nil;
	}
	public void add() {
		result = source1.getLong() + source2.getLong();
		defaultIntegerCCR();
		storeResult();
	}
	public void subtract() {
		long temp = source1.getLong() - source2.getLong();
		defaultIntegerCCR();
		storeResult();
	}
	private void defaultIntegerCCR() {
		ccr.setCarry((result & (1<<32)) != 0);
		ccr.setZero(result == 0);
		ccr.setNegative(result < 0);		
	}
	private void storeResult() {
		destination.setLongLow(result);
		destinationOverflow.setLongHigh(result);		
	}
	public ConditionCodeRegister getCCR() {
		return ccr;
	}
}
