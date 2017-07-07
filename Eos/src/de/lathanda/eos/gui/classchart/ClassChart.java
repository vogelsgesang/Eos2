package de.lathanda.eos.gui.classchart;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.lathanda.eos.common.gui.BackgroundCompiler;
import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.gui.diagram.Diagram;
import de.lathanda.eos.interpreter.parsetree.SystemType;
import de.lathanda.eos.interpreter.parsetree.Type;
import de.lathanda.eos.util.GuiToolkit;

public class ClassChart extends Diagram implements ListSelectionListener {
	private static final long serialVersionUID = -9004622410733768404L;
	private JList<String> classList;
	private ClassDiagram classDiagram;
	private DefaultListModel<String> classModel;
	private ArrayList<Type> variableList = new ArrayList<>();
	public ClassChart() {
		super(Messages.getString("ClassChart.Title"));
		classModel = new DefaultListModel<String>();
		classList = new JList<>(classModel);
		classList.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classList.setFont(GuiToolkit.createFont(Font.SANS_SERIF, Font.PLAIN, 10));
		classDiagram = new ClassDiagram();
		setLayout(new BorderLayout());
		add(new JScrollPane(classList), BorderLayout.WEST);
		add(new JScrollPane(classDiagram), BorderLayout.CENTER);		
		classList.addListSelectionListener(this);	
		update();
	}

	@Override
	public BufferedImage export(float dpi) {
		return classDiagram.export(dpi);
	}

	@Override
	public void init(BackgroundCompiler bc) {}

	@Override
	public void deinit(BackgroundCompiler bc) {	}
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		int[] indices = classList.getSelectedIndices();
		for(int index : indices) {
			classDiagram.setData(variableList.get(index));
		}
		repaint();
	}
	private void update() {
		variableList.addAll(SystemType.getAll());
		for(Type t:variableList) {
			classModel.addElement(t.getName());
		}
		classList.repaint();		
	}
}
