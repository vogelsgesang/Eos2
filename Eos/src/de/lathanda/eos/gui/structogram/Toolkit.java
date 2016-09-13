package de.lathanda.eos.gui.structogram;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.gui.diagram.AlternativeUnit;
import de.lathanda.eos.gui.diagram.LoopUnit;
import de.lathanda.eos.gui.diagram.Unit;

public class Toolkit {
    public static Unit create(ProgramNode n) {
        if (n instanceof AlternativeUnit) {
            return new Alternative((AlternativeUnit) n);
        } else if (n instanceof LoopUnit) {
            return new Loop((LoopUnit)n);
        } else {
            return new Statement(n);
        }
    }
}
