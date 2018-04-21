package de.lathanda.eos.common.interpreter;

public interface InfoToken {
	int getLength();
	Format getFormat();
	int getBegin();
	boolean isEof();
}
