package de.lathanda.assembler.interpreter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.assembler.cpu.Machine;
import de.lathanda.eos.common.AbstractMachine;
import de.lathanda.eos.common.AbstractProgram;
import de.lathanda.eos.common.AbstractType;
import de.lathanda.eos.common.ErrorInformation;
import de.lathanda.eos.common.ProgramSequence;
import de.lathanda.eos.common.ProgramUnit;
import de.lathanda.eos.common.TranslationException;
import de.lathanda.eos.interpreter.javacc.SourceToken;

public class Program implements AbstractProgram {
    private final LinkedList<SourceToken> tokenList;
    private final LinkedList<ErrorInformation> errors;
    private final ArrayList<Instruction> program;
    private final TreeMap<String, Integer> labels;
    private final ArrayList<Data> data;
    private String source;
    private final Machine machine;
	public Program(String source) {
		tokenList = new LinkedList<>();
		errors = new LinkedList<>();
		program = new ArrayList<>();
		labels = new TreeMap<>();
		data = new ArrayList<>();
		machine = new Machine();
		source = "";
	}
	public void pushRegister(de.lathanda.assembler.interpreter.Register r) {
		// TODO Auto-generated method stub
		
	}

	public void pushIndirectRegister(de.lathanda.assembler.interpreter.Register r) {
		// TODO Auto-generated method stub
		
	}

	public void pushInc() {
		// TODO Auto-generated method stub
		
	}

	public void pushLabel(String id) {
		// TODO Auto-generated method stub
		
	}

	public void pushOpcodeOrLabel(String id) {
		// TODO Auto-generated method stub
		
	}

	public void pushInt(int parseInt) {
		// TODO Auto-generated method stub
		
	}

	public void pushFloat(int floatToIntBits) {
		// TODO Auto-generated method stub
		
	}

	public void pushIndexRegister(de.lathanda.assembler.interpreter.Register r) {
		// TODO Auto-generated method stub
		
	}

	public void pushDec() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void parse(String path) throws TranslationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void compile() throws TranslationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public LinkedList<ErrorInformation> getErrors() {
		return errors;
	}
	@Override
	public LinkedList<SourceToken> getTokenList() {
		return tokenList;
	}
	@Override
	public String getSource() {
		return source;
	}
	@Override
	public AbstractMachine getMachine() {
		return machine;
	}
	@Override
	public AbstractType seekType(int pos) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String prettyPrint() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ProgramSequence getProgram() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LinkedList<ProgramUnit> getSubPrograms() {
		// TODO Auto-generated method stub
		return null;
	}

}
