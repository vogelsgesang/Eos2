package de.lathanda.eos.interpreter.eoslang;

import java.util.TreeSet;

import de.lathanda.eos.common.interpreter.ErrorInformation;
import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.common.interpreter.TranslationException;
import de.lathanda.eos.extension.BasicParser;
import de.lathanda.eos.interpreter.parsetree.CompilerError;
import de.lathanda.eos.interpreter.parsetree.Program;

public class EosParser implements BasicParser {
	Parser parser;
	StringCharStream scs;
	public EosParser(String source) {
		scs = new StringCharStream(source);
		ParserTokenManager tokenmanager = new ParserTokenManager(scs);
		parser = new Parser(tokenmanager);
	}
	@Override
	public void parse(Program program, String path) throws TranslationException {
		try {
			parser.Parse(program, path);
		} catch (ParseException pe) {
			throw new TranslationException(handleParseException(pe));
		} catch (TokenMgrError ex) {
			throw new TranslationException(new CompilerError("Token.Error", ex.getLocalizedMessage()));
		} catch (NumberFormatException nfe) {
			throw new TranslationException(new CompilerError("Number.Error", nfe.getLocalizedMessage()));
		} catch (RuntimeException re) {
			throw new TranslationException(new CompilerError("Generic.Error", re.getLocalizedMessage()));			
		} catch (Throwable t) {
			throw new TranslationException(new CompilerError("UnknownError", t.getLocalizedMessage()));					
		}
	}
	private ErrorInformation handleParseException(ParseException pe) {
		if (pe.expectedTokenSequences == null) {
			return new CompilerError("Compile.Error", pe.getLocalizedMessage());
		}
		StringBuilder expected = new StringBuilder();
		StringBuilder encountered = new StringBuilder();
		int maxSize = 0;
		int item = 1;
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
			if (tok.kind == ParserConstants.EOF) {
				encountered.append(pe.tokenImage[0]);
				break;
			}

			encountered.append(unescape(pe.tokenImage[tok.kind]));
			tok = tok.next;

		}
		Token token = pe.currentToken;
		return new CompilerError(new Marker(token.beginColumn, token.beginLine, token.endColumn, token.endLine), "Parser.Error", encountered, expected,
				token.endLine);
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
	public int getLine(int pos) {
		return scs.getLine(pos);
	}

}
