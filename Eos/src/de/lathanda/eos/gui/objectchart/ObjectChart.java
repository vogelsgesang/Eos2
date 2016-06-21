package de.lathanda.eos.gui.objectchart;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.lathanda.eos.gui.GuiConstants;
import de.lathanda.eos.gui.diagram.Diagram;
import de.lathanda.eos.interpreter.BackgroundCompiler;
import de.lathanda.eos.interpreter.CompilerError;
import de.lathanda.eos.interpreter.CompilerListener;
import de.lathanda.eos.interpreter.DebugInfo;
import de.lathanda.eos.interpreter.DebugListener;
import de.lathanda.eos.interpreter.Machine;
import de.lathanda.eos.interpreter.Variable;
import de.lathanda.eos.interpreter.parsetree.ReservedVariables;
import de.lathanda.eos.interpreter.Program;
/**
 * Objektdiagramm
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ObjectChart extends Diagram implements CompilerListener, DebugListener, ListSelectionListener {
	private static final long serialVersionUID = -2959905171654651632L;
	private Machine machine;
	private JList<String> memoryList;
	private ObjectDiagram objectDiagram;
	private DefaultListModel<String> memoryModel;
	private TreeMap<String, Variable> memory;
	private ArrayList<Variable> variableList = new ArrayList<>();
	public ObjectChart() {
		super(GuiConstants.GUI.getString("ObjectChart.Title"));
		memoryModel = new DefaultListModel<String>();
		memoryList = new JList<>(memoryModel);
		memoryList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		memoryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		objectDiagram = new ObjectDiagram();
		setLayout(new BorderLayout());
		add(memoryList, BorderLayout.WEST);
		add(objectDiagram, BorderLayout.CENTER);
		memoryList.addListSelectionListener(this);
	}

	@Override
	public BufferedImage export(float dpi) {
		return objectDiagram.export(dpi);
	}

    @Override
    public void init(BackgroundCompiler bc) {
        bc.addCompilerListener(this);
    }

    @Override
    public void deinit(BackgroundCompiler bc) {
        bc.removeCompilerListener(this);
    }

	@Override
	public void compileComplete(Machine machine, LinkedList<CompilerError> errors, Program program) {
		machine.addDebugListener(this);
		this.machine = machine;
		update();
	}

	@Override
	public void debugPointReached(DebugInfo debugInfo) {
		update();		
	}
	private void update() {
		memory = machine.getMemoryDump();
		SwingUtilities.invokeLater(() -> doUpdate());
	}
	private void doUpdate() {
		HashSet<Variable> selectedValues = new HashSet<>();
		int[] indices = memoryList.getSelectedIndices();
		for(int index : indices) {
			selectedValues.add(variableList.get(index));
		}
		memoryModel.clear();
		variableList.clear();
		Set<String> data = memory.keySet();
		for(String key : data) {
			if (key.equals(ReservedVariables.RESULT)) {
				memoryModel.addElement(Unit.lm.getName(ReservedVariables.RESULT));
			} else if (key.equals(ReservedVariables.WINDOW)) {
				memoryModel.addElement(Unit.lm.getName(ReservedVariables.WINDOW));
			} else if (key.startsWith(ReservedVariables.REPEAT_TIMES_INDEX)) {	
				memoryModel.addElement(Unit.lm.getName(ReservedVariables.REPEAT_TIMES_INDEX));
			} else {
				memoryModel.addElement(key);
			}
			Variable v = memory.get(key);
			variableList.add(v);
			if ( selectedValues.contains(v)) {
				memoryList.addSelectionInterval(memoryModel.size() - 1, memoryModel.size() - 1);
			}
		}
		memoryList.repaint();		
	}
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		LinkedList<Variable> data = new LinkedList<>();
		int[] indices = memoryList.getSelectedIndices();
		for(int index : indices) {
			data.add(variableList.get(index));
		}
		objectDiagram.setData(data);
		repaint();
	}
}
