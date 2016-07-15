package de.lathanda.assembler.cpu;

public class ALU {
	private Cell source1;
	private Cell source2;
	private Cell destination;
	private Cell destinationOverflow;
	private Cell nil;
	private long result = 0L;
	private float fresult = 0F;
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
	private void defaultIntegerCCR() {
		ccr.setCarry(result < Integer.MIN_VALUE || result > Integer.MAX_VALUE);
		ccr.setZero(result == 0L);
		ccr.setNegative(result < 0L);		
	}
	private void storeIntegerResult() {
		destination.setLongLow(result);
		destinationOverflow.setLongHigh(result);		
	}
	private void defaultFloatCCR() {
		ccr.setZero(fresult == 0F);
		ccr.setNegative(fresult < 0F);		
	}
	private void storeFloatResult() {
		destination.setFloat(fresult);
	}
	
	public ConditionCodeRegister getCCR() {
		return ccr;
	}
	public void add() {
		result = source1.getLong() + source2.getLong();
		defaultIntegerCCR();
		storeIntegerResult();
	}
	public void fadd() {
		fresult = source1.getFloat() + source2.getFloat();
		defaultFloatCCR();
		storeFloatResult();
	}
	public void subtract() {
		result = source1.getLong() - source2.getLong();
		defaultIntegerCCR();
		storeIntegerResult();
	}
	public void fsubtract() {
		fresult = source1.getFloat() - source2.getFloat();
		defaultFloatCCR();
		storeFloatResult();
	}


	public void mul() {
		// TODO Auto-generated method stub
		
	}


	public void fmul() {
		// TODO Auto-generated method stub
		
	}

	public void div() {
		
	}
	
	public void fdiv() {
		// TODO Auto-generated method stub
		
	}


	public void inc() {
		// TODO Auto-generated method stub
		
	}


	public void dec() {
		// TODO Auto-generated method stub
		
	}


	public void neg() {
		// TODO Auto-generated method stub
		
	}


	public void fneg() {
		// TODO Auto-generated method stub
		
	}


	public void and() {
		// TODO Auto-generated method stub
		
	}


	public void or() {
		// TODO Auto-generated method stub
		
	}


	public void xor() {
		// TODO Auto-generated method stub
		
	}


	public void not() {
		// TODO Auto-generated method stub
		
	}


	public void asl() {
		// TODO Auto-generated method stub
		
	}


	public void asr() {
		// TODO Auto-generated method stub
		
	}


	public void lsl() {
		// TODO Auto-generated method stub
		
	}


	public void lsr() {
		// TODO Auto-generated method stub
		
	}


	public void rol() {
		// TODO Auto-generated method stub
		
	}


	public void ror() {
		// TODO Auto-generated method stub
		
	}


	public void cmp() {
		// TODO Auto-generated method stub
		
	}


	public void fcmp() {
		// TODO Auto-generated method stub
		
	}


	public void adc() {
		// TODO Auto-generated method stub
		
	}


	public void move() {
		// TODO Auto-generated method stub
		
	}
}
