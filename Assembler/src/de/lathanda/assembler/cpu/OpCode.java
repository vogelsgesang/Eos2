package de.lathanda.assembler.cpu;

import java.util.TreeMap;

public enum OpCode {
	NOP(0, false),   // no operation  
	ADD(1, true),    // signed integer addition
	FADD(2, true),   // floating point addition
	SUB(3, true),    // signed integer subtraction
	FSUB(4, true),   // floating point  subtraction
	MUL(5, true),    // signed integer multiplication
	FMUL(6, true),   // signed integer multiplication
	DIV(7, true),    // signed integer division
	FDIV(8, true),   // floating point division
	INC(9, true),    // integer increment
	DEC(10, true),   // integer decrement
	NEG(11, true),   // signed integer negative
	FNEG(12, true),  // floating point negative
	AND(13, true),   // logical and
	OR(14, true),    // logical or
	XOR(15, true),   // logical exclusive or
	NOT(16, true),   // logical not
	ASL(17, true),   // arithmetic shift left
	ASR(18, true),   // arithmetic shift right
	LSL(19, true),   // logical shift left
	LSR(20, true),   // logical shift right
	ROL(21, true),   // rotate left
	ROR(22, true),   // rotate right
	BCC(23, false),  // branch carry clear  
	BCS(24, false),  // branch carry set
	BVC(25, false),  // branch overflow clear  
	BVS(26, false),  // branch overflow set
	BLT(27, false),  // branch less than 
	BLE(28, false),  // branch less than or equal 
	BEQ(29, false),  // branch equal 
	BGE(30, false),  // branch greater than or equal 
	BNE(31, false),  // branch not equal 
	BGT(32, false),  // branch greater than 
	BPL(33, false),  // branch positive (plus)
	BMI(34, false),  // branch negative (minus) 
	CMP(35, true),   // signed integer compare
	CMPF(36, true),  // floating point compare
	EXG(37, false),  // exchange
	JMP(38, false),  // jump
	JSR(39, false),  // jump subroutine
	RTS(40, false),  // return from subroutine
	MOVE(41, false), // move
	ADC(42, false);  // signed integer add with carry
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
