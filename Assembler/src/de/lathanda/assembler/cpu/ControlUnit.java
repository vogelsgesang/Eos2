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
		case NOP: // no operation
			break;
		case ADD: // integer addition
			alu.add();
			break;
		case SUB: // integer subtraction
			alu.subtract();
			break;
			/*
		case MUL(3),   // unsigned integer multiplication
		case MULS(4),  // signed integer multiplication
		case DIV(5),   // unsigned integer division
		case DIVS(6),  // signed integer division
		case FADD(7),  // floating point addition
		case FSUB(8),  // floating point subtraction
		case FMUL(9),  // floating point multiplication
		case FDIV(10), // floating point division
		case INC(11),  // integer increment
		case DEC(12),  // integer decrement
		case NEG(13),  // signed integer negative
		case FNEG(14), // floating point negative
		case AND(15),  // logical and
		case OR(16),   // logical or
		case XOR(17),  // logical exclusive or
		case NOT(18),  // logical not
		case ASL(19),  // arithmetic shift left
		case ASR(20),  // arithmetic shift right
		case LSL(21),  // logical shift left
		case LSR(22),  // logical shift right
		case ROL(23),  // rotate left
		case ROR(24),  // rotate right
		case BCC(25),  // branch carry clear  
		case BCS(26),  // branch carry set
		case BLT(27),  // branch less than 
		case BLE(28),  // branch less than or equal 
		case BEQ(29),  // branch equal 
		case BGE(30),  // branch greater than or equal 
		case BNE(31),  // branch not equal 
		case BGT(32),  // branch greater than 
		case BPL(33),  // branch positive (plus)
		case BMI(34),  // branch negative (minus) 
		case CMP(35),  // compare
		case CMPS(36)
		case FCMP(37), // compare float
		case EXG(38),  // exchange
		case JMP(39),  // jump
		case JSR(40),  // jump subroutine
		case RTS(41),  // return from subroutine
		case MOVE(42)*/
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
}
