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
	MOD(8, true),    // signed integer modulo
	FDIV(9, true),   // floating point division
	INC(10, true),   // integer increment
	DEC(11, true),   // integer decrement
	NEG(12, true),   // signed integer negative
	FNEG(13, true),  // floating point negative
	AND(14, true),   // logical and
	OR(15, true),    // logical or
	XOR(16, true),   // logical exclusive or
	NOT(17, true),   // logical not
	ASL(18, true),   // arithmetic shift left
	ASR(19, true),   // arithmetic shift right
	LSL(20, true),   // logical shift left
	LSR(21, true),   // logical shift right
	ROL(22, true),   // rotate left
	ROR(23, true),   // rotate right
	BCC(24, false),  // branch carry clear  
	BCS(25, false),  // branch carry set
	BVC(26, false),  // branch overflow clear  
	BVS(27, false),  // branch overflow set
	BLT(28, false),  // branch less than 
	BLE(29, false),  // branch less than or equal 
	BEQ(30, false),  // branch equal / zero
	BGE(31, false),  // branch greater than or equal 
	BNE(32, false),  // branch not equal / not zero
	BGT(33, false),  // branch greater than 
	BPL(34, false),  // branch positive (plus)
	BMI(35, false),  // branch negative (minus) 
	CMP(36, true),   // signed integer compare
	FCMP(37, true),  // floating point compare
	EXG(38, false),  // exchange
	JMP(39, false),  // jump
	JSR(40, false),  // jump subroutine
	RTS(41, false),  // return from subroutine
	MOVE(42, false), // move
	ADC(43, true),   // signed integer add with carry
	HLT(44, false);  // stop the machine
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
