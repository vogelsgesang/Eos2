package de.lathanda.assembler.cpu;

import java.util.TreeMap;

public enum OpCode {
	NOP(0, false),   // no operation  
	ADD(1, true),    // integer addition
	SUB(2, true),    // integer subtraction
	MUL(3, true),    // unsigned integer multiplication
	MULS(4, true),   // signed integer multiplication
	DIV(5, true),    // unsigned integer division
	DIVS(6, true),   // signed integer division
	FADD(7, true),   // floating point addition
	FSUB(8, true),   // floating point subtraction
	FMUL(9, true),   // floating point multiplication
	FDIV(10, true),  // floating point division
	INC(11, true),   // integer increment
	DEC(12, true),   // integer decrement
	NEG(13, true),   // signed integer negative
	FNEG(14, true),  // floating point negative
	AND(15, true),   // logical and
	OR(16, true),    // logical or
	XOR(17, true),   // logical exclusive or
	NOT(18, true),   // logical not
	ASL(19, true),   // arithmetic shift left
	ASR(20, true),   // arithmetic shift right
	LSL(21, true),   // logical shift left
	LSR(22, true),   // logical shift right
	ROL(23, true),   // rotate left
	ROR(24, true),   // rotate right
	BCC(25, false),  // branch carry clear  
	BCS(26, false),  // branch carry set
	BLT(27, false),  // branch less than 
	BLE(28, false),  // branch less than or equal 
	BEQ(29, false),  // branch equal 
	BGE(30, false),  // branch greater than or equal 
	BNE(31, false),  // branch not equal 
	BGT(32, false),  // branch greater than 
	BPL(33, false),  // branch positive (plus)
	BMI(34, false),  // branch negative (minus) 
	CMP(35, true),   // compare
	FCMP(36, true),  // compare float
	EXG(37, false),  // exchange
	JMP(38, false),  // jump
	JSR(39, false),  // jump subroutine
	RTS(40, false),  // return from subroutine
	MOVE(41, false); // move
	public final int opcode;
	public final boolean alu;
	private static final TreeMap<Integer,OpCode> codeMap = new TreeMap<>();
	static {
		for ( final OpCode opcode:OpCode.values()) {
			codeMap.put(opcode.opcode, opcode);
		}
	}
	OpCode(int code, boolean alu) {
		this.opcode = code;
		this.alu    = alu;
	}
	static OpCode getOpCode(int code) {
		return codeMap.get(code);
	}
}
