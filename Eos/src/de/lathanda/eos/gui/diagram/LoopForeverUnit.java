package de.lathanda.eos.gui.diagram;

import de.lathanda.eos.common.ProgramNode;
import de.lathanda.eos.common.ProgramSequence;

public interface LoopForeverUnit extends ProgramNode {
	ProgramSequence getSequence();
}
