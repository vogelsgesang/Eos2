package de.lathanda.eos.common.interpreter;

public interface ProgramUnit extends ProgramNode {
	String getName();
	ProgramSequence getSequence();
}
