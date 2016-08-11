package de.lathanda.eos.gui.diagram;

public interface LoopTimesUnit extends ProgramNode {
	String getLabel();

	ProgramSequence getSequence();
	
	ProgramNode getTimes();

	int getIndexId();
}
