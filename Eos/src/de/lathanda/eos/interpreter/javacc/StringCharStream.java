package de.lathanda.eos.interpreter.javacc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Diese Klasse ist eine sehr einfach Version vom durch javacc erzeugten SimpleCharStream.
 * Sie hat weniger overhead und arbeitet direkt auf einer Zeichenkette.
 * Dabei wird die Bedeutung der Spalte (column) umdefiniert zur Zeichenposition (position).
 * Dies ist notwenig, da die Editoren auf Positionsbasis und nicht auf Zeilen/Spalten Basis arbeiten.
 * 
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class StringCharStream implements CharStream {

    /**
     * start of current token
     */
    private int tokenBegin = 0;
    /**
     * current index
     */
    private int position = -1;
    /**
     * line for each position within string
     */
    private int line[];

    /**
     * line of position
     */
    private int posLine = 1;
    /**
     * string to scan
     */
    private String text;

    private boolean prevCharIsCR = false;
    private boolean prevCharIsLF = false;

    public StringCharStream(String text) {
        this.text = text + "\n"; //Workaround for inability of javacc to terminate SKIP_TOKEN with <eof>
        line = new int[this.text.length() + 1];
    }

    public StringCharStream(File file) throws IOException {
        this(readFile(file));
    }
    private static String readFile(File file) throws IOException {
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Utf-8"));
        StringBuilder src = new StringBuilder();
        while (br.ready()) {
            src.append(br.readLine());
            src.append("\n");
        }
        br.close();
        return src.toString();        
    }

    @Override
    public void setTabSize(int i) {
        //who cares
    }

    @Override
    public int getTabSize() {
        return 4; 
        //who cares        
    }

    @Override
    public char BeginToken() throws java.io.IOException {
        tokenBegin = 0;
        char c = readChar();
        tokenBegin = position;

        return c;
    }

    private void UpdateLine(char c) {
    	if (line[position] != 0) return;
        if (prevCharIsLF) {
            prevCharIsLF = false;
            posLine++;
        } else if (prevCharIsCR) {
            prevCharIsCR = false;
            if (c == '\n') {
                prevCharIsLF = true;
            } else {
                posLine++;
            }
        }

        switch (c) {
            case '\r':
                prevCharIsCR = true;
                break;
            case '\n':
                prevCharIsLF = true;
                break;
            default:
                break;
        }

        line[position] = posLine;
    }

    @Override
    public char readChar() throws java.io.IOException {
        if (++position < text.length()) {
            char c = text.charAt(position);
            UpdateLine(c);
            return c;
        } else {
            position--;
            throw new IOException("EOT");
        }
    }

    @Override
    public int getEndColumn() {
        return position;
    }

    @Override
    public int getEndLine() {
        if (position > -1) {
            return line[position];
        } else {
            return 0;
        }
    }

    @Override
    public int getBeginColumn() {
        return tokenBegin;
    }

    @Override
    public int getBeginLine() {
        return line[tokenBegin];
    }

    @Override
    public void backup(int amount) {
        position -= amount;
    }

    @Override
    public String GetImage() {
        return text.substring(tokenBegin, position + 1);
    }

    @Override
    public void Done() {
        text = null;
        line = null;
    }

    @Override
    public int getColumn() {
        return position;
    }

    @Override
    public int getLine() {
        return line[position];
    }
    public int getLine(int pos) {
    	if (pos < line.length) {
    		return line[pos];
    	} else {
    		return posLine;
    	}
    }
    @Override
    public char[] GetSuffix(int len) {
        return text.substring(position, position + len).toCharArray();
    }

    @Override
    public boolean getTrackLineColumn() {
        return true;
        //who cares
    }

    @Override
    public void setTrackLineColumn(boolean trackLineColumn) {
        //who cares
    }

}
