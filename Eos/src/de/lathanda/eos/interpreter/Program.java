package de.lathanda.eos.interpreter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import de.lathanda.eos.interpreter.commands.CreateVariable;
import de.lathanda.eos.interpreter.javacc.SourceToken;
import de.lathanda.eos.interpreter.parsetree.Expression;
import de.lathanda.eos.interpreter.parsetree.ReservedVariables;
import de.lathanda.eos.interpreter.parsetree.Sequence;
import de.lathanda.eos.interpreter.parsetree.SubRoutine;

/**
 * Speichert und behandelt den Syntaxbaum des Programms.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Program {
    private final static TreeMap<String, Type> guessTable = new TreeMap<>();
    private final Sequence program;
    private final LinkedList<SubRoutine> sub;
    private final LinkedList<Node> nodeList;
    private final LinkedList<SourceToken> tokenList;
    private final Environment env;
    private final String source;
    private final PrettyPrinter prettyPrinter;
    public Program() {
        this.program = new Sequence();
        this.prettyPrinter = new PrettyPrinter("");    	
        this.source = "";
        sub = new LinkedList<>();
        env = new Environment();
        nodeList = new LinkedList<>();
        tokenList = new LinkedList<>();
    }
    public Program(String source) {
        this.program = new Sequence();
        this.prettyPrinter = new PrettyPrinter(source);    	
        this.source = source;
        sub = new LinkedList<>();
        env = new Environment();
        nodeList = new LinkedList<>();
        tokenList = new LinkedList<>();
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
    public LinkedList<SourceToken> getTokenList() {
        return tokenList;
    }
 
    public Sequence getProgram() {
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
    public LinkedList<SubRoutine> getSub() {
        return sub;
    }
    
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
    public LinkedList<CompilerError> getErrors() {
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
}
