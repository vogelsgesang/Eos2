package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.gui.diagram.LoopTimesUnit;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.ReservedVariables;
import de.lathanda.eos.interpreter.commands.GE;
import de.lathanda.eos.interpreter.commands.Jump;
import de.lathanda.eos.interpreter.commands.JumpIf;
import de.lathanda.eos.interpreter.commands.LoadConstant;
import de.lathanda.eos.interpreter.commands.LoadVariable;
import de.lathanda.eos.interpreter.commands.CreateVariable;
import de.lathanda.eos.interpreter.commands.DebugPoint;
import de.lathanda.eos.interpreter.commands.StoreVariable;
import de.lathanda.eos.interpreter.commands.SubtractI;

import java.util.ArrayList;

/**
 * Speichert und behandelt eine Zählschleife.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class RepeatTimes extends Loop implements LoopTimesUnit {

    private final Expression timesE;
    private String varName;
    private int loopId;

    public RepeatTimes(Expression times, Sequence sequence) {
        super(sequence, null);
        this.timesE = times;
    }

    public Expression getTimes() {
        return timesE;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        ArrayList<Command> body = new ArrayList<>();
        sequence.compile(body, autoWindow);
        ops.add(new DebugPoint(timesE.getMarker()));
        ops.add(new CreateVariable(varName, Type.getInteger().getMType()));
        timesE.compile(ops, autoWindow);

        ops.add(new StoreVariable(varName));
        ops.add(new Jump(body.size() + 1));

        ops.addAll(body);

        ops.add(new DebugPoint(timesE.marker));           
        ops.add(new LoadVariable(varName));
        ops.add(new LoadConstant(1));
        ops.add(new SubtractI());
        ops.add(new StoreVariable(varName));
        ops.add(new LoadVariable(varName));
        ops.add(new LoadConstant(0));
        ops.add(new GE());
        ops.add(new JumpIf(-(8 + body.size())));
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        sequence.resolveNamesAndTypes(with, env);
        if (timesE != null) {
            timesE.resolveNamesAndTypes(with, env);
            if (!timesE.getType().isNumber()) {
                env.addError(marker, "RepeatTimesNumber", timesE.getType());
            }
        }
        loopId  = env.getUID();
        varName = ReservedVariables.REPEAT_TIMES_INDEX + loopId;
    }

    @Override
    public String toString() {
        return "repeat " + timesE + " times\n" + sequence + "endrepeat";
    }
    @Override
    public String getLabel() {
        return createText("RepeatTimes.Label", timesE.getLabel());
    }

    @Override
    public boolean isPre() {
        return true;
    }    
    
    public int getIndexId() {
        return loopId;
    }
}
