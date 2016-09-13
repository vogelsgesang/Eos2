package de.lathanda.eos.gui.objectchart;

import java.awt.Color;
import java.text.MessageFormat;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.Readout;
import de.lathanda.eos.base.Scaling;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.gui.diagram.MemoryEntry;
import de.lathanda.eos.gui.diagram.TextUnit;
import de.lathanda.eos.interpreter.parsetree.ReservedVariables;
import de.lathanda.eos.util.ConcurrentLinkedList;

public class Toolkit {

    public static Unit formatValue(Object value) {
    	if (value == null) {
    		return new TextUnit(Unit.getName("1null"));
    	}
		if (value instanceof Boolean) {
			return new TextUnit(Unit.getName(value.toString()));
		} else if (value instanceof Integer) {
			return new TextUnit(value.toString());
		} else if (value instanceof Number) {
			return new TextUnit(MessageFormat.format("{0,number,#.####}", value));
		} else if (value instanceof ConcurrentLinkedList<?>) {			
			return new TextUnit(buildValue((ConcurrentLinkedList<?>)value));
		} else if (value instanceof Color) {
			return new ColorValue((Color)value);
		} else if (value instanceof LineStyle) {
			return new LineStyleValue((LineStyle)value);
		} else if (value instanceof FillStyle) {
			return new FillStyleValue((FillStyle)value);
		} else if (value instanceof Alignment) {
			return new AlignmentValue((Alignment)value);
		} else if (value instanceof Scaling) {
			return new ScalingValue((Scaling)value);
		} else {
			return Unit.createUnit(value);			
		}    	
    }
    public static Unit create(MemoryEntry v) {
		if (v.name.equals(ReservedVariables.RESULT)) {
			return new Property(Unit.getName(ReservedVariables.RESULT), v.data);
		} else if (v.name.equals(ReservedVariables.WINDOW)) {
			return new ObjectCard(Unit.getName(ReservedVariables.WINDOW), v.type, (Readout)v.data);
		} else if (v.name.startsWith(ReservedVariables.REPEAT_TIMES_INDEX)) {
			return new Property(Unit.getName(ReservedVariables.REPEAT_TIMES_INDEX), v.data);
		} else if (v.data instanceof Readout) {
			return new ObjectCard(v.name, v.type, (Readout)v.data);
		} else {
			return new Property(v.name, v.data);
		}
    }
	private static String buildValue(ConcurrentLinkedList<?> data) {
		return MessageFormat.format(Unit.getName("1count"), data.getLength());		
	}    
}
