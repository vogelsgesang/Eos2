package de.lathanda.eos.interpreter.javacc;

import static de.lathanda.eos.interpreter.javacc.ParserConstants.*;

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
        case IMPORT:
        case END_IMPORT:
        case PROGRAM:
        case END_PROGRAM:
        case PROCEDURE:
        case END_PROCEDURE:
        case METHOD:
        case END_METHOD:
        case REPEAT:
        case TIMES:
        case FOREVER:
        case UNTIL:
        case END_REPEAT:
        case IF:
        case THEN:
        case ELSE:
        case END_IF:
        case WITH:
        case END_WITH:
        case WHILE:
        case DO:
        case END_WHILE:
        case RESULT:
        case NOT:
        case OR:
        case AND:
        case BREAKPOINT:
        	return KEYWORD;
        case TRUE:
        case FALSE:
        case YELLOW:
        case RED:
        case GREEN:
        case BLUE:
        case WHITE:
        case BLACK:
        case BROWN:
        case LIGHT_BLUE:
        case LIGHT_GREEN:
        case GRAY:
        case LIGHT_GRAY:
        case DASHED:
        case SOLID:
        case DOTTED:
        case DASHED_DOTTED:
        case INVISIBLE:
        case FILLED:
        case SHADED:
        case DARK_SHADED:
        case TRANSPARENT:
        case CENTER:
        case LEFT:
        case RIGHT:
        case TOP:
        case BOTTOM:                
        case INTEGER_LITERAL:
        case DECIMAL_LITERAL:
        case FLOATING_POINT_LITERAL:
        case EXPONENT:
        case STRING_LITERAL1:
        case STRING_LITERAL2:
        case COLOR_LITERAL_RGB:
        case COLOR_LITERAL_RGBA:
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
