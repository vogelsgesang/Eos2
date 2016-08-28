package de.lathanda.eos.common.interpreter;

public interface InfoToken {
    int LITERAL = 1;
    int KEYWORD = 2;
    int COMMENT = 3;
    int PLAIN = 4;
    int IGNORE = 0;
	int getLength();
	int getFormat();
	int getBegin();
	boolean isEof();
}
