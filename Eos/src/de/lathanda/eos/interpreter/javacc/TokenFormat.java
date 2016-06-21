package de.lathanda.eos.interpreter.javacc;

import static de.lathanda.eos.interpreter.javacc.ParserConstants.*;

/**
 * Konstanten für das Code Coloring.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.5
 */
public enum TokenFormat {

    LITERAL,
    KEYWORD,
    COMMENT,
    PLAIN,
    IGNORE;

    public static TokenFormat getTokenKind(Token t) {
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
                return TokenFormat.KEYWORD;
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
                return TokenFormat.LITERAL;
            case SINGLE_LINE_COMMENT:
            case MULTI_LINE_COMMENT:
            case MULTI_LINE_COMMENT2:
                return TokenFormat.COMMENT;
            default:
                return TokenFormat.PLAIN;
        }
    }
}
