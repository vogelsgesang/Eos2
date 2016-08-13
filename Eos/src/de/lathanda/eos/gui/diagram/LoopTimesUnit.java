package de.lathanda.eos.gui.diagram;

import de.lathanda.eos.common.ProgramNode;
import de.lathanda.eos.common.ProgramSequence;

public interface LoopTimesUnit extends ProgramNode {
	String getLabel();

	ProgramSequence getSequence();
	
	ProgramNode getTimes();

	int getIndexId();
}
