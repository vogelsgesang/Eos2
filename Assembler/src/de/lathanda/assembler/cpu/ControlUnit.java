package de.lathanda.assembler.cpu;

public class ControlUnit {
	private Memory mem;         // memory 
	private ALU alu;            // arithmetic logical unit
	private Register ir;        // instruction register
	private Register mar;       // memory address register
	private Register[] r;       // universal registers
	private Register a;         // accumulator
	private Register b;         // overflow register
	private Register sr;        // stack register
	private Cell eaSource;      // effective address source
	private Cell eaDestination; // effective address destination
	private OpCode opcode;      // operation code
	private boolean isFinished;
	public ControlUnit(Memory mem, ALU alu) {
		super();
		this.mem = mem;
		this.alu = alu;
		r = new Register[16];
		for(int i = 0; i < r.length; i++) {
			r[i] = new Register();
		}
		a = new Register();
		b = new Register();
		ir = new Register();
		sr = new Register();
		mar = new Register();
		eaSource      = new Cell();
		eaDestination = new Cell();
	}
	public void init(int address) {
		mar.setValue(address);
		isFinished = false;
	}
	public void next() {
		ir.setValue(nextCell().getInt());
		decode();
		execute();
	}
	private void execute() {
		if (opcode.alu) {
			alu.setSource(eaDestination, eaSource);
			alu.setDestination(eaDestination, b.getCell());
		}
		switch (opcode) {
		case NOP:
			break;
		case ADD:
			alu.add();
			break;
		case FADD: 
			alu.fadd();
			break;
		case SUB:
			alu.subtract();
			break;
		case FSUB:
			alu.fsubtract();
			break;
		case MUL:
			alu.mul();
			break;
		case FMUL:
			alu.fmul();
			break;
		case DIV:
			alu.div();
			break;
		case MOD:
			alu.mod();
			break;
		case FDIV:
			alu.fdiv();
			break;
		case INC:
			alu.inc();
			break;
		case DEC:
			alu.dec();
			break;
		case NEG:
			alu.neg();
			break;
		case FNEG:
			alu.fneg();
			break;
		case AND:
			alu.and();
			break;
		case OR:
			alu.or();
			break;
		case XOR:
			alu.xor();
			break;
		case NOT:
			alu.not();
			break;
		case ASL:
			alu.asl();
			break;
		case ASR:
			alu.asr();
			break;
		case LSL:
			alu.lsl();
			break;
		case LSR:
			alu.lsr();
			break;
		case ROL:
			alu.rol();
			break;
		case ROR:
			alu.ror();
			break;
		case BCC:
			bcc();
			break;
		case BCS:
			bcs();
			break;
		case BVC:
			bvc();
			break;
		case BVS:
			bvs();
			break;
		case BLT:
			blt();
			break;
		case BLE:
			ble();
			break;
		case BEQ:
			beq();
			break;
		case BGE:
			bge();
			break;
		case BNE:
			bne();
			break;
		case BGT:
			bgt();
			break;
		case BPL:
			bpl();
			break;
		case BMI:
			bmi();
			break;
		case CMP:
			alu.cmp();
			break;
		case FCMP:
			alu.fcmp();
			break;
		case EXG:
			exg();
			break;
		case JMP:
			jmp();
			break;
		case JSR:
			jsr();
			break;
		case RTS:
			rts();
			break;
		case ADC:
			alu.adc();
			break;
		case MOVE:
			alu.move();
			break;
		case HLT:
			isFinished = true;
			break;
		}
		
	}
	private void rts() {
		mar.set(mem.getMemoryCell(sr));
		sr.increment();		
	}
	private void jsr() {
		sr.decrement();
		mem.getMemoryCell(sr).set(mar);
		mar.set(eaSource);
	}
	private void jmp() {
		mar.set(eaSource);
	}
	private void exg() {
		int temp = eaSource.getInt();
		eaSource.set(eaDestination);
		eaDestination.setInt(temp);		
	}
	private void bmi() {
		if (alu.getCCR().getNegative()) {
			mar.set(eaSource);
		}
	}
	private void bpl() {
		if (!(alu.getCCR().getNegative() || alu.getCCR().getZero())) {
			mar.set(eaSource);
		}
	}
	private void bgt() {
		if (alu.getCCR().getNegative()) {
			mar.set(eaSource);
		}
		
	}
	private void bne() {
		if (!alu.getCCR().getZero()) {
			mar.set(eaSource);
		}
	}
	private void bge() {
		if (alu.getCCR().getZero() || alu.getCCR().getNegative()) {
			mar.set(eaSource);
		}
	}
	private void beq() {
		if (alu.getCCR().getZero()) {
			mar.set(eaSource);
		}
	}
	private void ble() {
		if (!alu.getCCR().getNegative()) {
			mar.set(eaSource);
		}
	}
	private void blt() {
		if (!(alu.getCCR().getNegative() || alu.getCCR().getZero())) {
			mar.set(eaSource);
		}
	}
	private void bvs() {
		if (alu.getCCR().getOverflow()) {
			mar.set(eaSource);
		}
	}
	private void bvc() {
		if (!alu.getCCR().getOverflow()) {
			mar.set(eaSource);
		}
	}
	private void bcs() {
		if (alu.getCCR().getCarry()) {
			mar.set(eaSource);
		}
	}
	private void bcc() {
		if (!alu.getCCR().getCarry()) {
			mar.set(eaSource);
		}
	}
	private void decode() {
		Cell code = ir.getCell();	
		opcode        = OpCode.getOpCode(code.getBits(0, 8));
		eaSource      = getEffectiveAddress(code.getBits( 8, 4), code.getBits(12, 4), code.getBits(16, 4));
		eaDestination = getEffectiveAddress(code.getBits(20, 4), code.getBits(24, 4), code.getBits(28, 4));
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
		case 0b1000: //direct, intermediate
			return nextCell();
		case 0b1001: //absolut + index
			return mem.getMemoryCell(nextCell().getInt() + r[index].getInt());
		case 0b1010: //accumulator
			return a.getCell();
		case 0b1011: //overflow
			return b.getCell();
		default:
			throw new IllegalOperationException();
		}
	}
	private Cell nextCell() {
		Cell cell = mem.getMemoryCell(mar.getInt());
		mar.increment();
		return cell;
	}
	public boolean isFinished() {
		return isFinished;
	}
}
