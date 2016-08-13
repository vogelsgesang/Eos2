package de.lathanda.eos.interpreter.parsetree;

import de.lathanda.eos.common.Marker;
import de.lathanda.eos.common.ProgramNode;
import de.lathanda.eos.common.ProgramSequence;
import de.lathanda.eos.interpreter.Command;
import de.lathanda.eos.interpreter.Environment;
import de.lathanda.eos.interpreter.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Speichert und behandelt Anweisungssequenzen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class Sequence extends Node implements ProgramSequence {

    private final ArrayList<Node> sequence;
    int index = 0;

    public Sequence() {
        this.sequence = new ArrayList<>();
    }

    public Sequence(List<Node> sequence) {
        this.sequence = new ArrayList<>();
        this.sequence.addAll(sequence);
    }

    public void append(Sequence s) {
        for (Node n:s.sequence) {
        	append(n);
        }
    }

    public void append(Node s) {
        sequence.add(s);
        if (marker == null) {
        	marker = new Marker(s, this);
        } else {
        	marker.extend(s.getMarker());
        }
    }

    public ArrayList<ProgramNode> getInstructions() {
    	ArrayList<ProgramNode> temp = new ArrayList<ProgramNode>(sequence.size());
    	sequence.forEach(i -> temp.add(i));
        return temp;
    }

    @Override
    public void compile(ArrayList<Command> ops, boolean autoWindow) throws Exception {
        for (Node instruction : sequence) {
            if (instruction.getType().isVoid()) {
                //none void instruction would corrupt the parameter stack
                //we can choose between adding an artifical consumer or to not compile the instruction
                //as this type of instruction has no effect, we do not compile it
                instruction.compile(ops, autoWindow);            
            }
        }
    }

    @Override
    public void resolveNamesAndTypes(Expression with, Environment env) {
        sequence.stream().forEachOrdered((instruction) -> {
            instruction.resolveNamesAndTypes(with, env);
            if (!instruction.getType().isVoid()) {
                env.addError(instruction.getMarker(), "StatementWithReturn", instruction);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Node n : sequence) {
            res.append(n).append("\n");
        }
        return res.toString();
    }

    @Override
    public String getLabel() {
        return null; //no label
    }
}
