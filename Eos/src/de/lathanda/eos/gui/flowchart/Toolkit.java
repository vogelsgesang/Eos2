package de.lathanda.eos.gui.flowchart;

import de.lathanda.eos.common.interpreter.ProgramNode;
import de.lathanda.eos.gui.diagram.AlternativeUnit;
import de.lathanda.eos.gui.diagram.LoopForeverUnit;
import de.lathanda.eos.gui.diagram.LoopTimesUnit;
import de.lathanda.eos.gui.diagram.LoopUnit;

public class Toolkit {


	public static ConnectedUnit create(ProgramNode n) {
		if (n instanceof AlternativeUnit) {
			return new Alternative((AlternativeUnit) n);
		} else if (n instanceof LoopForeverUnit) {
			return new LoopForever((LoopForeverUnit) n);
		} else if (n instanceof LoopTimesUnit) {
			return new LoopTimes((LoopTimesUnit) n);
		} else if (n instanceof LoopUnit) {
			LoopUnit lu = (LoopUnit) n;
			if (lu.isPre()) {
				return new LoopWhile(lu);
			} else {
				return new LoopDoWhile(lu);
			}
		} else {
			return new Statement(n);
		}
	}

}
