package de.lathanda.assembler.interpreter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.assembler.cpu.Machine;
import de.lathanda.assembler.interpreter.javacc.ParseException;
import de.lathanda.assembler.interpreter.javacc.Parser;
import de.lathanda.assembler.interpreter.javacc.ParserTokenManager;
import de.lathanda.assembler.interpreter.javacc.StringCharStream;
import de.lathanda.eos.common.interpreter.AbstractMachine;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.AbstractType;
import de.lathanda.eos.common.interpreter.ErrorInformation;
import de.lathanda.eos.common.interpreter.InfoToken;
import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.common.interpreter.ProgramUnit;
import de.lathanda.eos.common.interpreter.TranslationException;

public class Program implements AbstractProgram {
    private final LinkedList<InfoToken> tokenList;
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
		this.source = source;
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
		ParserTokenManager tokenmanager = new ParserTokenManager(new StringCharStream(source));
		Parser parser = new Parser(tokenmanager);
		try {
			parser.Parse(this);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
	public LinkedList<InfoToken> getTokenList() {
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
	@Override
	public int getLine(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}

}
