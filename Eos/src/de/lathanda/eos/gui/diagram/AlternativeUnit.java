package de.lathanda.eos.gui.diagram;

import de.lathanda.eos.common.ProgramNode;
import de.lathanda.eos.common.ProgramSequence;

public interface AlternativeUnit extends ProgramNode {

	String getLabel();

	ProgramSequence getThen();

	ProgramSequence getElse();

}
