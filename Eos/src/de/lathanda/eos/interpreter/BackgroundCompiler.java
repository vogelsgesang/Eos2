package de.lathanda.eos.interpreter;

import de.lathanda.eos.interpreter.javacc.CommonParserConstants;
import de.lathanda.eos.interpreter.javacc.EosParser;
import de.lathanda.eos.interpreter.javacc.ParseException;
import de.lathanda.eos.interpreter.javacc.Token;
import de.lathanda.eos.interpreter.javacc.TokenMgrError;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Asynchroner Kompiler.
 * Der Thread läuft im Hintergrund und übersetzt das Programm.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public class BackgroundCompiler implements Runnable {
	private Machine machine;
	private Program program;
	private EosParser parser;
	private final Source source;
	private final LinkedList<CompilerError> errors;
	private final CompilerMulticaster cmc;

	public BackgroundCompiler(Source source) {
		this.cmc = new CompilerMulticaster();
		this.source = source;
		this.machine = null;
		parser = null;
		errors = new LinkedList<>();
	}

	@Override
	public void run() {
		while (true) {
			update();
		}
	}

	private void update() {
		try {
			String src = source.getSourceCode();
			errors.clear();
			machine = new Machine();
			program = new Program(src);
			parser = EosParser.create(src);
			parser.Parse(program, source.getPath());
			machine.reinit();
			program.compile(machine);
		} catch (ParseException pe) {
			handleParseException(pe, errors);
		} catch (TokenMgrError ex) {
			handleTokenError(ex, errors);
		} catch (Exception e) {
			e.printStackTrace();
			handleException(e, errors);
		}
		if (program != null) {
			errors.addAll(program.getErrors());
		}
		cmc.fireCompileComplete();
	}

	public void addCompilerListener(CompilerListener cl) {
		cmc.add(cl);
		if (machine != null) {
			// if a compile is already available distribute it
			cl.compileComplete(machine, errors, program);
		}
	}

	public void removeCompilerListener(CompilerListener cl) {
		cmc.remove(cl);
	}

	private void handleException(Exception e, LinkedList<CompilerError> errors) {
		errors.add(new CompilerError("Compile.Error", e.getLocalizedMessage()));
	}

	private void handleParseException(ParseException pe, LinkedList<CompilerError> errors) {
		if (pe.expectedTokenSequences == null) {
			handleException(pe, errors);
			return;
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
		errors.add(new CompilerError(new Marker(pe.currentToken.next), "Parser.Error", encountered, expected,
				pe.currentToken.next.endLine));
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

	private void handleTokenError(TokenMgrError ex, LinkedList<CompilerError> errors) {
		errors.add(new CompilerError("Token.Error", ex.getLocalizedMessage()));
	}

	protected class CompilerMulticaster {

		private final LinkedList<CompilerListener> compilerListener;

		protected CompilerMulticaster() {
			compilerListener = new LinkedList<>();
		}

		void add(CompilerListener cl) {
			compilerListener.add(cl);
		}

		void remove(CompilerListener cl) {
			compilerListener.remove(cl);
		}

		void fireCompileComplete() {
			compilerListener.forEach((cl) -> {
				cl.compileComplete(machine, errors, program);
			});
		}
	}
}
