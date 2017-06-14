package de.lathanda.eos.interpreter.parsetree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import de.lathanda.eos.common.interpreter.AbstractMachine;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.common.interpreter.AutoCompleteType;
import de.lathanda.eos.common.interpreter.ErrorInformation;
import de.lathanda.eos.common.interpreter.InfoToken;
import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.common.interpreter.ProgramUnit;
import de.lathanda.eos.common.interpreter.TranslationException;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.MProcedure;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.PrettyPrinter;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.CreateVariable;
import de.lathanda.eos.interpreter.javacc.CommonParserConstants;
import de.lathanda.eos.interpreter.javacc.EosParser;
import de.lathanda.eos.interpreter.javacc.ParseException;
import de.lathanda.eos.interpreter.javacc.SourceToken;
import de.lathanda.eos.interpreter.javacc.Token;
import de.lathanda.eos.interpreter.javacc.TokenMgrError;

/**
 * Speichert und behandelt den Syntaxbaum des Programms.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Program implements AbstractProgram {
    private final static TreeMap<String, Type> guessTable = new TreeMap<>();
    private final Sequence program;    
    private final LinkedList<SubRoutine> sub;
    private final TreeMap<String, UserClass> userclass;
    private final LinkedList<MarkedNode> nodeList;
    private final LinkedList<InfoToken> tokenList;
    private final Environment env;
    private final String source;
    private final PrettyPrinter prettyPrinter;
    private final Machine machine;
    private EosParser parser;
    public Program() {
    	this.machine = new Machine();
        this.program = new Sequence();
        this.prettyPrinter = new PrettyPrinter("");    	
        this.source = "";
        sub = new LinkedList<>();
        userclass = new TreeMap<>();
        env = new Environment(this);
        nodeList = new LinkedList<>();
        tokenList = new LinkedList<>();
    }
    public Program(String source) {
    	this.machine = new Machine();
        this.program = new Sequence();
        this.prettyPrinter = new PrettyPrinter(source);    	
        this.source = source;
        sub = new LinkedList<>();
        userclass = new TreeMap<>();
        env = new Environment(this);
        nodeList = new LinkedList<>();
        tokenList = new LinkedList<>();
    }
    @Override
    public synchronized void parse(String path) throws TranslationException {
		parser = EosParser.create(source);
		try {
			parser.Parse(this, path);			
		} catch (ParseException pe) {
			throw new TranslationException(handleParseException(pe));
		} catch (TokenMgrError ex) {
			throw new TranslationException(new CompilerError("Token.Error", ex.getLocalizedMessage()));
		} catch (NumberFormatException nfe) {
			throw new TranslationException(new CompilerError("Number.Error", nfe.getLocalizedMessage()));			
		} catch (Throwable t) {
			throw new TranslationException(new CompilerError("UnknownError", t.getLocalizedMessage()));			
		}
    }
    public void add(Sequence s) {
        program.append(s);
    }
    public void add(SubRoutine s) {
        sub.add(s);
    }
    public void add(UserClass u) {
    	userclass.put(u.getName(), u);
    }
    public void addNode(MarkedNode node) {
        nodeList.add(node);
    }
    public void addToken(SourceToken token) {
        tokenList.add(token);
    }
    public LinkedList<MarkedNode> getNodeList() {
        return nodeList;
    }
    public LinkedList<InfoToken> getTokenList() {
        return tokenList;
    }
 
    public ProgramSequence getProgram() {
        return program;
    }
    public void merge(Program subprogram, Marker marker) {
    	//override all debugging markers
    	for(MarkedNode n:subprogram.nodeList) {
    		n.setMarker(marker);
    	}
    	this.program.append(subprogram.program);
    	this.sub.addAll(subprogram.sub);
    	this.userclass.putAll(subprogram.userclass);
    	this.nodeList.addAll(subprogram.nodeList);
    }
    public LinkedList<ProgramUnit> getSubPrograms() {
    	LinkedList<ProgramUnit> temp = new LinkedList<>();
    	sub.forEach(s -> temp.add(s));
        return temp;
    }
    /**
     * Wandelt den Syntaxbaum in ein ausführbares Programm dieser Maschine um. 
     * @param m In diese Maschcine wird das Programm geschrieben.
     * @throws Exception
     */
    public void compile(Machine m) throws Exception {
        env.resetAll();
        //first scan, resolve types
        for(UserClass uc : userclass.values()) { //prepare user types
        	uc.bind(env);
        } 
        for(UserClass uc : userclass.values()) { //check for storage cycles
        	uc.checkCyclicStorage();
        } 
        
        for(SubRoutine s : sub) { //register all sub routines
            s.registerSub(env);
        }
        
        for(UserClass uc : userclass.values()) { //finish user types
        	uc.resolveNamesAndTypes(env);
        } 
        
        program.resolveNamesAndTypes(null, env);

        env.storeVariables();
        for(SubRoutine s : sub) {
            if (s.getGlobalAccess()) {
                env.restoreVariables();
            } else {
                env.resetVariables();
            }
            s.resolveNamesAndTypes(null, env);
        }
        
        //second scan produce commands
        for(UserClass uc : userclass.values()) { //compile user types
        	uc.compile();
        } 
        ArrayList<Command> ops = new ArrayList<>();
        if (env.getAutoWindow()) {
            ops.add(new CreateVariable(ReservedVariables.WINDOW, SystemType.getWindow().getMType()));
        }
        program.compile(ops, env.getAutoWindow());
        m.setProgram(ops);
        for(SubRoutine s : sub) {
            ops = new ArrayList<>();
            //automatic add to window only works with user function that have global access to the window object
            //methods can, procedures cannot.
            s.compile(ops, s.getGlobalAccess() && env.getAutoWindow());
            m.addUserFunction(new MProcedure(s.getSignature(), ops, s.getGlobalAccess()));
        }        
    }    
    public LinkedList<ErrorInformation> getErrors() {
        return env.getErrors();
    }
    
    public AutoCompleteType seekType(int position) {
    	Type type;
    	type = seekTypeSemantical(position);
    	if (type.isUnknown() || type.isVoid()) {
    		type = useBestGuess(position);
    	}
    	return type;
    }
    private Type seekTypeSemantical(int position) {
    	MarkedNode target = null;
    	for(MarkedNode node : nodeList) {
    		if (node.getMarker().getEndPosition() < position) {
    			if (node instanceof Expression && node.getMarker().getEndPosition() == position - 1) {
    				target = node;
    				break;
    			}
    		} else {
    			break;
    		}
    	}
    	if (target == null) {
    		return Type.getUnknown();
    	} else {
    		return target.getType();
    	}
    }
    private Type useBestGuess(int position) {
    	int start = 0;
    	for(int i = Math.min(position, source.length()); i --> 0 ;) {
    		if(Character.isWhitespace(source.charAt(i))) {
    			start = i + 1;
    			break;
    		}
    	}
    	String name = source.substring(start, position).trim();
    	if (guessTable.containsKey(name)) {
    		return guessTable.get(name);
    	} else {
    		return Type.getUnknown();
    	}
    }
    public static void addGuess(String name, Type type) {
    	guessTable.put(name, type);
    }
    public String getSource() {
        return source;
    }
	@Override
	public synchronized int getLine(int pos) {
		return parser.getLine(pos);
	}	
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("program\n");
        res.append(program);
        res.append("endprogram\n");
        for(SubRoutine s : sub) {
            res.append(s);
        }
        for(UserClass uc : userclass.values()) {
        	res.append(uc);
        }
        res.append(env);
        return res.toString();        
    }  
    
	public void prettyPrinterNewline(int position, int level) {
		prettyPrinter.newline(position, level);		
	}
	public String prettyPrint() {
		return prettyPrinter.prettyPrint();
	}
	public void compile() throws TranslationException {
		try {
			machine.reinit();
			compile(machine);
		} catch (Exception e) {
			throw new TranslationException(new CompilerError("Compile.Error", e.getLocalizedMessage()));
		}
	}

	@Override
	public AbstractMachine getMachine() {
		return machine;
	}
	private ErrorInformation handleParseException(ParseException pe) {
		if (pe.expectedTokenSequences == null) {
			return new CompilerError("Compile.Error", pe.getLocalizedMessage());
		}
		StringBuilder expected = new StringBuilder();
		StringBuilder encountered = new StringBuilder();
		int maxSize = 0;
		char item = 'a';
		for (int[] expectedTokenSequence : pe.expectedTokenSequences) {
			expected.append("\n").append(item++).append(") ");
			if (maxSize < expectedTokenSequence.length) {
				maxSize = expectedTokenSequence.length;
			}
			for (int j = 0; j < expectedTokenSequence.length; j++) {
				expected.append(unescape(pe.tokenImage[expectedTokenSequence[j]])).append(' ');
			}
			if (expectedTokenSequence[expectedTokenSequence.length - 1] != 0) {
				expected.append("...");
			}
		}
		Token tok = pe.currentToken.next;
		TreeSet<String> alreadyUsed = new TreeSet<>();
		for (int i = 0; i < maxSize; i++) {
			if (!alreadyUsed.add(pe.tokenImage[i])) {
				continue;
			}
			if (i != 0) {
				encountered.append(" ");
			}
			if (tok.kind == CommonParserConstants.EOF) {
				encountered.append(pe.tokenImage[0]);
				break;
			}

			encountered.append(unescape(pe.tokenImage[tok.kind]));
			tok = tok.next;

		}
		Token token = pe.currentToken.next;
		return new CompilerError(new Marker(token.beginColumn, token.beginLine, token.endColumn, token.endLine), "Parser.Error", encountered, expected,
				pe.currentToken.next.endLine);
	}

	private String unescape(String text) {
		int i = text.indexOf("\\u");
		if (i == -1) {
			return text;
		}

		StringBuilder sb = new StringBuilder();
		int a = 0;
		while (i != -1) {
			sb.append(text.substring(a, i));
			a = i + 6;
			sb.append((char) Integer.parseInt(text.substring(i + 2, a), 16));
			i = text.indexOf("\\u", a);
		}
		sb.append(text.substring(a));
		return sb.toString();
	}
	@Override
	public LinkedList<AutoCompleteInformation> getClassAutoCompletes() {
		LinkedList<AutoCompleteInformation> classInfos = new LinkedList<>();
		classInfos.addAll(SystemType.getClassType().getAutoCompletes());
		for(UserClass uc : userclass.values()) {
			classInfos.add(uc.getAutoComplete());
		}
		return classInfos;
	}
	public Type getType(String name) {
		Type t = SystemType.getInstanceByName(name);
		if (t == null || t.isUnknown()) {
			UserClass uc = userclass.getOrDefault(name, null);
			if (uc != null) {
				t = uc.getType();
			} else {
				uc = new UserClass(name);
				userclass.put(name, uc);
				t = uc.getType();
			}
		}
		return t;
	}
	public UserClass createUserClass(String name) {
		UserClass uc = userclass.getOrDefault(name, null);
		if (uc != null) {
			return uc;
		} else {
			uc = new UserClass(name);
			userclass.put(name, uc);
			return uc;
		}
		
	}
}
