package de.lathanda.eos.gui.diagram;

public interface AlternativeUnit extends ProgramNode {

	String getLabel();

	ProgramSequence getThen();

	ProgramSequence getElse();

}
