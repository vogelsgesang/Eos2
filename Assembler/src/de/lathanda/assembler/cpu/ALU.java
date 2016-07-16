package de.lathanda.assembler.cpu;

public class ALU {
	private Cell source1;
	private Cell source2;
	private Cell destination;
	private Cell destinationOverflow;
	private Cell nil;
	private int  result = 0;
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
		ccr.setZero(result == 0L);
		ccr.setNegative(result < 0);		
	}
	
	private void defaultFloatCCR() {
		ccr.setZero(fresult == 0F);
		ccr.setNegative(fresult < 0F);		
	}
	
	public ConditionCodeRegister getCCR() {
		return ccr;
	}
	
	public void add() {
		int a = source1.getInt();
		int b = source2.getInt();
		long al = source1.getUnsignedLong();
		long bl = source2.getUnsignedLong();
		result = a + b;
		defaultIntegerCCR();
		boolean signa = a < 0;
		boolean signb = b < 0;
		boolean signr = result < 0;
		ccr.setOverflow((signa == signb) && (signa != signr));
		ccr.setCarry((al + bl) > 0xFFFFFFFFL);
		destination.setInt(result);
	}
	
	public void fadd() {
		fresult = source1.getFloat() + source2.getFloat();
		defaultFloatCCR();
		destination.setFloat(fresult);
	}
	
	public void subtract() {
		int a = source1.getInt();
		int b = source2.getInt();
		long al = source1.getUnsignedLong();
		long bl = source2.getUnsignedLong();
		result = a - b;
		defaultIntegerCCR();
		boolean signa = a < 0;
		boolean signb = b < 0;
		boolean signr = result < 0;
		ccr.setOverflow((signa == !signb) && (signa != signr));
		ccr.setCarry((al - bl) < 0L);
		destination.setInt(result);
	}
	
	public void fsubtract() {
		fresult = source1.getFloat() - source2.getFloat();
		defaultFloatCCR();
		destination.setFloat(fresult);
	}

	public void mul() {
		long uresult = source1.getUnsignedLong() * source2.getUnsignedLong();
		ccr.setOverflow(uresult > Integer.MAX_VALUE || uresult < Integer.MIN_VALUE);
		result = (int)uresult;
		defaultIntegerCCR();
		destination.setLongLow(uresult);
		destinationOverflow.setLongHigh(uresult);
	}

	public void fmul() {
		fresult = source1.getFloat() * source2.getFloat();
		defaultFloatCCR();
		destination.setFloat(fresult);		
	}

	public void div() {
		result = source1.getInt() / source2.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}

	public void mod() {
		result = source1.getInt() % source2.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}
	
	public void fdiv() {
		fresult = source1.getFloat() / source2.getFloat();
		defaultFloatCCR();
		destination.setFloat(fresult);
	}

	public void inc() {
	    result = destination.getInt() + 1;
	    ccr.setCarry(result == 0);
	    ccr.setOverflow(result == 0x10000000);
	    defaultIntegerCCR();
	    destination.setInt(result);		
	}

	public void dec() {
	    result = destination.getInt() - 1;
	    ccr.setCarry(result == -1);
	    ccr.setOverflow(result == 0x0FFFFFFF);
	    defaultIntegerCCR();
	    destination.setInt(result);		
	}

	public void neg() {
		result = -destination.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}

	public void fneg() {
		fresult = -destination.getFloat();
		defaultFloatCCR();
		destination.setFloat(result);		
	}

	public void and() {
		result = source1.getInt() & source2.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}

	public void or() {
		result = source1.getInt() | source2.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}

	public void xor() {
		result = source1.getInt() ^ source2.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}

	public void not() {
		result = ~source2.getInt();
		defaultIntegerCCR();
		destination.setInt(result);
	}

	public void asl() {
		result = source2.getInt() << source1.getInt();
		defaultIntegerCCR();
		destination.setInt(result);	
	}

	public void asr() {
		result = source2.getInt() >> source1.getInt();
		defaultIntegerCCR();
		destination.setInt(result);	
	}

	public void lsl() {
		result = source2.getInt() << source1.getInt();
		defaultIntegerCCR();
		destination.setInt(result);	
	}

	public void lsr() {
		result = source2.getInt() >>> source1.getInt();
		defaultIntegerCCR();
		destination.setInt(result);	
	}

	public void rol() {
		result = Integer.rotateLeft(source2.getInt(), source1.getInt());
		defaultIntegerCCR();
		destination.setInt(result);	
	}

	public void ror() {
		result = Integer.rotateRight(source2.getInt(), source1.getInt());
		defaultIntegerCCR();
		destination.setInt(result);	
	}

	public void cmp() {
		result = source1.getInt() - source2.getInt();
		defaultIntegerCCR();
	}

	public void fcmp() {
		fresult = source1.getFloat() - source2.getFloat();
		defaultFloatCCR();
	}

	public void adc() {
		long al = source1.getUnsignedLong();
		long bl = source2.getUnsignedLong();
		result = (int)(al + bl + ((ccr.getCarry())?1:0));
		defaultIntegerCCR();
		ccr.setCarry((al + bl + ((ccr.getCarry())?1:0)) > 0xFFFFFFFFL);
		destination.setInt(result);
	}

	public void move() {
		destination.setInt(source1.getInt());
	}
}
