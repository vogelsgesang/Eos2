package de.lathanda.eos.interpreter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

import de.lathanda.eos.common.interpreter.AbstractMachine;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.ErrorInformation;
import de.lathanda.eos.common.interpreter.InfoToken;
import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.common.interpreter.ProgramSequence;
import de.lathanda.eos.common.interpreter.ProgramUnit;
import de.lathanda.eos.common.interpreter.TranslationException;
import de.lathanda.eos.interpreter.commands.CreateVariable;
import de.lathanda.eos.interpreter.javacc.CommonParserConstants;
import de.lathanda.eos.interpreter.javacc.EosParser;
import de.lathanda.eos.interpreter.javacc.ParseException;
import de.lathanda.eos.interpreter.javacc.SourceToken;
import de.lathanda.eos.interpreter.javacc.Token;
import de.lathanda.eos.interpreter.javacc.TokenMgrError;
import de.lathanda.eos.interpreter.parsetree.Expression;
import de.lathanda.eos.interpreter.parsetree.ReservedVariables;
import de.lathanda.eos.interpreter.parsetree.Sequence;
import de.lathanda.eos.interpreter.parsetree.SubRoutine;
import de.lathanda.eos.spi.LanguageManager;

/**
 * Speichert und behandelt den Syntaxbaum des Programms.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Program implements AbstractProgram {
    private final static TreeMap<String, Type> guessTable = new TreeMap<>();
    private final Sequence program;
    private final LinkedList<SubRoutine> sub;
    private final LinkedList<Node> nodeList;
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
        env = new Environment(LanguageManager.getInstance().getLockProperties());
        nodeList = new LinkedList<>();
        tokenList = new LinkedList<>();
    }
    public Program(String source) {
    	this.machine = new Machine();
        this.program = new Sequence();
        this.prettyPrinter = new PrettyPrinter(source);    	
        this.source = source;
        sub = new LinkedList<>();
        env = new Environment(LanguageManager.getInstance().getLockProperties());
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
		}
    }
    public void add(Sequence s) {
        program.append(s);
    }
    public void add(SubRoutine s) {
        sub.add(s);
    }
    public void addNode(Node node) {
        nodeList.add(node);
    }
    public void addToken(SourceToken token) {
        tokenList.add(token);
    }
    public LinkedList<Node> getNodeList() {
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
    	for(Node n:subprogram.nodeList) {
    		n.setMarker(marker);
    	}
    	this.program.append(subprogram.program);
    	this.sub.addAll(subprogram.sub);
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
        for(SubRoutine s : sub) {
            s.registerSub(env);
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
        ArrayList<Command> ops = new ArrayList<>();
        if (env.getAutoWindow()) {
            ops.add(new CreateVariable(ReservedVariables.WINDOW, Type.getWindow()));
        }
        program.compile(ops, env.getAutoWindow());
        m.setProgram(ops);
        
        for(SubRoutine s : sub) {
            ops = new ArrayList<>();
            //automatic add to window only works with user function that have global access to the window object
            //methods can, procedures cannot.
            s.compile(ops, s.getGlobalAccess() && env.getAutoWindow());
            m.createUserFunction(s.getSignature(), s.getParameters(), ops, s.getGlobalAccess());
        }
    }    
    public LinkedList<ErrorInformation> getErrors() {
        return env.getErrors();
    }
    
    public Type seekType(int position) {
    	Type type;
    	type = seekTypeSemantical(position);
    	if (type.isUnknown() || type.isVoid()) {
    		type = useBestGuess(position);
    	}
    	return type;
    }
    private Type seekTypeSemantical(int position) {
    	Node target = null;
    	for(Node node : nodeList) {
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
}
