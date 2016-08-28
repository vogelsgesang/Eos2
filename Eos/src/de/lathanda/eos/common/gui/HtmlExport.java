package de.lathanda.eos.common.gui;

import java.text.MessageFormat;

import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.InfoToken;

/**
 * Diese Utilityklasse stellt Methoden bereit um ein Programm in HTML umzuwandeln.
 *
 * @author Peter (Lathanda) Schneider
 */
public class HtmlExport {

    private static final String HEAD = "<!DOCTYPE html>\n<html>\n<head>\n<title>\n{0}\n</title>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<code>";
    private static final String HEAD2 = "</code></body></html>";
    private static final String COMMENT = "<span style=\"color:green\">";
    private static final String COMMENT2 = "</span>";
    private static final String LITERAL = "<span style=\"color:blue\">";
    private static final String LITERAL2 = "</span>";
    private static final String KEYWORD = "<b>";
    private static final String KEYWORD2 = "</b>";
    private static final String NEWLINE = "<br>\n";
    private static final String INDENT = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String SPACE = "&nbsp;";    
    /**
     * Erzeugt aus einem Programm Html-Text.
     * @param abstractProgram 
     * @param name Überschrift
     * @return
     */
    public static String export2html(AbstractProgram abstractProgram, String name) {
        StringBuilder html = new StringBuilder();
        html.append(MessageFormat.format(HEAD, name));
        int index = 0;
        String source = abstractProgram.getSource();
        for(InfoToken st:abstractProgram.getTokenList()) {
        	if (!st.isEof()) {
        		token2html(source.substring(index, st.getBegin() + st.getLength()), st, html);
        		index = st.getBegin() + st.getLength();
        	}
        }
        html.append(HEAD2);
        return html.toString();
    }
    /**
     * Quellcodetoken in Html umwandeln.
     * @param text Rohtext
     * @param st zugehöriger Token
     * @param html Html-TExt an den angehängt wird
     */
    private static void token2html(String text, InfoToken st, StringBuilder html) {
        switch (st.getFormat()) {
            case InfoToken.COMMENT:
                html.append(COMMENT);
                html.append(text2html(text));
                html.append(COMMENT2);
                break;
            case InfoToken.LITERAL:
                html.append(LITERAL);
                html.append(text2html(text));
                html.append(LITERAL2);
                break;
            case InfoToken.KEYWORD:
                html.append(KEYWORD);
                html.append(text2html(text));
                html.append(KEYWORD2);
                break;
            default:
                html.append(text2html(text));
        }
    }
    /**
     * Filtert Sonderzeichen aus dem Text und ersetzt diese durch entsprechende Html-Befehle.
     * @param text
     * @return
     */
    private static String text2html(String text) {
    	StringBuilder filtered = new StringBuilder();
    	for(int i = 0; i < text.length(); i++) {
    		if (text.charAt(i) > 127) {
    			filtered.append("&#");
    			filtered.append((int)text.charAt(i));
    			filtered.append(";");
    		} else {
    			switch (text.charAt(i)) {
    			case '<':
    				filtered.append("&lt;");
    			case '>':
    				filtered.append("&gt;");
    			default:
    				filtered.append(text.charAt(i));
    			}  			
    		}
    	}
        return filtered.toString()
        		.replace("\n", NEWLINE)
        		.replace("\t", INDENT)
        		.replace(" ", SPACE);
    }

}
