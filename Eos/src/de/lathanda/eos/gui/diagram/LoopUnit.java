package de.lathanda.eos.gui.diagram;

public interface LoopUnit extends ProgramNode {
    boolean isPre();

	String getLabel();

	ProgramSequence getSequence();
}
