package de.lathanda.eos.gui.diagram;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.common.interpreter.ProgramSequence;

public interface LoopUnit extends ProgramNode {
    boolean isPre();

	String getLabel();

	ProgramSequence getSequence();
}
