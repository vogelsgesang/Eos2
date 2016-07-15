package de.lathanda.assembler.cpu;

public class ControlUnit {
	private Memory mem;
	private ALU alu;
	private Register ir;
	private Register mar;
	private Register[] r;
	private Register a;
	private Register b;
	private Cell eaSource;
	private Cell eaDestination;
	private OpCode opcode;
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
		mar = new Register();
		eaSource      = mem.getMemoryCell(0);
		eaDestination = mem.getMemoryCell(0);
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
		}
		
	}
	private void rts() {
		// TODO Auto-generated method stub
		
	}
	private void jsr() {
		// TODO Auto-generated method stub
		
	}
	private void jmp() {
		// TODO Auto-generated method stub
		
	}
	private void exg() {
		// TODO Auto-generated method stub
		
	}
	private void bmi() {
		// TODO Auto-generated method stub
		
	}
	private void bpl() {
		// TODO Auto-generated method stub
		
	}
	private void bgt() {
		// TODO Auto-generated method stub
		
	}
	private void bne() {
		// TODO Auto-generated method stub
		
	}
	private void bge() {
		// TODO Auto-generated method stub
		
	}
	private void beq() {
		// TODO Auto-generated method stub
		
	}
	private void ble() {
		// TODO Auto-generated method stub
		
	}
	private void blt() {
		// TODO Auto-generated method stub
		
	}
	private void bvs() {
		// TODO Auto-generated method stub
		
	}
	private void bvc() {
		// TODO Auto-generated method stub
		
	}
	private void bcs() {
		// TODO Auto-generated method stub
		
	}
	private void bcc() {
		// TODO Auto-generated method stub
		
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
}
