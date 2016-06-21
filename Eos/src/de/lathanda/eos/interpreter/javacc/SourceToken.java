package de.lathanda.eos.interpreter.javacc;

/**
 * Erweiterter Token mit Text und Formatierungsinformationen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class SourceToken {
    private final int begin;
    private final int length;
    private final TokenFormat format;
    private final String image;
            
    public SourceToken(Token t) {
        this.format = TokenFormat.getTokenKind(t);
        this.begin = t.beginColumn;
        this.length = t.endColumn - t.beginColumn + 1;
        this.image = t.image;
    }

    public int getBegin() {
        return begin;
    }

    public int getLength() {
        return length;
    }

    public TokenFormat getFormat() {
        return format;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "SourceToken{" + begin + "(" + length + ") = "+image+"}";
    }

}
