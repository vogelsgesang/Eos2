package de.lathanda.eos.gui.objectchart;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.lathanda.eos.common.gui.BackgroundCompiler;
import de.lathanda.eos.common.gui.GuiConstants;
import de.lathanda.eos.common.interpreter.AbstractMachine;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.CompilerListener;
import de.lathanda.eos.common.interpreter.DebugInfo;
import de.lathanda.eos.common.interpreter.DebugListener;
import de.lathanda.eos.common.interpreter.ErrorInformation;
import de.lathanda.eos.gui.diagram.Diagram;
import de.lathanda.eos.gui.diagram.MemoryEntry;
import de.lathanda.eos.gui.diagram.Unit;
import de.lathanda.eos.interpreter.parsetree.ReservedVariables;
/**
 * Objektdiagramm
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.4
 */
public class ObjectChart extends Diagram implements CompilerListener, DebugListener, ListSelectionListener {
	private static final long serialVersionUID = -2959905171654651632L;
	private AbstractMachine machine;
	private JList<String> memoryList;
	private ObjectDiagram objectDiagram;
	private DefaultListModel<String> memoryModel;
	private LinkedList<MemoryEntry> memory;
	private ArrayList<MemoryEntry> variableList = new ArrayList<>();
	public ObjectChart() {
		super(GuiConstants.GUI.getString("ObjectChart.Title"));
		memoryModel = new DefaultListModel<String>();
		memoryList = new JList<>(memoryModel);
		memoryList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		memoryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		objectDiagram = new ObjectDiagram();
		setLayout(new BorderLayout());
		add(new JScrollPane(memoryList), BorderLayout.WEST);
		add(new JScrollPane(objectDiagram), BorderLayout.CENTER);
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
	public void compileComplete(LinkedList<ErrorInformation> errors, AbstractProgram program) {
		this.machine = program.getMachine();
		machine.addDebugListener(this);
		update();
	}

	@Override
	public void debugPointReached(DebugInfo debugInfo) {
		update();		
	}
	private void update() {
		memory = machine.getMemory();
		SwingUtilities.invokeLater(() -> doUpdate());
	}
	private void doUpdate() {
		TreeSet<MemoryEntry> selectedValues = new TreeSet<>();
		int[] indices = memoryList.getSelectedIndices();
		for(int index : indices) {
			selectedValues.add(variableList.get(index));
		}
		memoryModel.clear();
		variableList.clear();
		for(MemoryEntry mem : memory) {
			if (mem.name.equals(ReservedVariables.RESULT)) {
				memoryModel.addElement(Unit.getName(ReservedVariables.RESULT));
			} else if (mem.name.equals(ReservedVariables.WINDOW)) {
				memoryModel.addElement(Unit.getName(ReservedVariables.WINDOW));
			} else if (mem.name.startsWith(ReservedVariables.REPEAT_TIMES_INDEX)) {	
				memoryModel.addElement(Unit.getName(ReservedVariables.REPEAT_TIMES_INDEX));
			} else {
				memoryModel.addElement(mem.name);
			}
			variableList.add(mem);
			if ( selectedValues.contains(mem)) {
				memoryList.addSelectionInterval(memoryModel.size() - 1, memoryModel.size() - 1);
			}
		}
		revalidate();
	}
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		LinkedList<MemoryEntry> data = new LinkedList<>();
		int[] indices = memoryList.getSelectedIndices();
		for(int index : indices) {
			data.add(variableList.get(index));
		}
		objectDiagram.setData(data);
		repaint();
	}
}
