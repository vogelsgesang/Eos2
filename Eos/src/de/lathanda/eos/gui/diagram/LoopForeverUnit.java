package de.lathanda.eos.gui.diagram;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.common.interpreter.ProgramSequence;

public interface LoopForeverUnit extends ProgramNode {
	ProgramSequence getSequence();
}
