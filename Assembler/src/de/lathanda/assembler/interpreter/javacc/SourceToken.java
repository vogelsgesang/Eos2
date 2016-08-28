package de.lathanda.assembler.interpreter.javacc;

import static de.lathanda.assembler.interpreter.javacc.ParserConstants.*;
import static de.lathanda.eos.interpreter.javacc.ParserConstants.EOF;

import de.lathanda.eos.common.interpreter.InfoToken;

/**
 * Erweiterter Token mit Text und Formatierungsinformationen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class SourceToken implements InfoToken {
    private final int begin;
    private final int length;
    private final int format;
    private final String image;
    private final boolean eof;
    
    public SourceToken(Token t) {
        this.format = getTokenKind(t);
        this.begin = t.beginColumn;
        this.length = t.endColumn - t.beginColumn + 1;
        this.image = t.image;
        this.eof = t.kind == EOF;
    }

    public int getBegin() {
        return begin;
    }

    public int getLength() {
        return length;
    }

    public int getFormat() {
        return format;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "SourceToken{" + begin + "(" + length + ") = "+image+"}";
    }


	private int getTokenKind(Token t) {
        switch (t.kind) {
        case EOF:
            return IGNORE;
        case DATA:
        case CODE:
            return KEYWORD;               
        case INTEGER_LITERAL:
        case DECIMAL_LITERAL:
        case FLOATING_POINT_LITERAL:
        case EXPONENT:
            return LITERAL;
        case SINGLE_LINE_COMMENT:
        case MULTI_LINE_COMMENT:
        case MULTI_LINE_COMMENT2:
            return COMMENT;
        default:
            return PLAIN;
        }
	}

	@Override
	public boolean isEof() {
		return eof;
	}
}
