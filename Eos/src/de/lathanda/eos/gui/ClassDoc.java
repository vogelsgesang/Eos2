package de.lathanda.eos.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import static de.lathanda.eos.common.gui.GuiConstants.GUI;

public class ClassDoc extends JFrame {
	private static final long serialVersionUID = -1384422891907360658L;
	/**
	 * Hilfefenster Singelton
	 */
	private static ClassDoc help = null;

	/**
	 * Hilfefenster anzeigen.
	 */
	public static void showDoc() {

		if (help == null) {
			help = new ClassDoc();
		} else if (help.isVisible()) {
			return;
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		help.setBounds((int) (screen.width * 0.5), 0, (int) (screen.height * 0.9), (int) (screen.width * 0.5));
        help.setTitle(GUI.getString("Classbook.Title"));
        help.setIconImage(ResourceLoader.loadImage("icons/eos.png"));		
		help.setVisible(true);
	}
	private static DefaultMutableTreeNode root; 
	static {
		
		root = new DefaultMutableTreeNode(new TreeEntry(GUI.getString("Classbook.Classes"), AutoCompleteInformation.CLASS));
		LinkedList<de.lathanda.eos.interpreter.Type> types = de.lathanda.eos.interpreter.Type.getAll();
		
		for(de.lathanda.eos.interpreter.Type t:types) {
			DefaultMutableTreeNode clsNode = new DefaultMutableTreeNode(new TreeEntry(t));
			TreeSet<TreeEntry> methods = new TreeSet<>();
			TreeSet<TreeEntry> properties = new TreeSet<>();
			for(AutoCompleteInformation aci:t.getAutoCompletes()) {
				switch (aci.getType()) {
				case AutoCompleteInformation.METHOD:
					methods.add(new TreeEntry(aci));
					break;
				case AutoCompleteInformation.PROPERTY:
					properties.add(new TreeEntry(aci));
					break;
				}
			}
			DefaultMutableTreeNode methodNodes = new DefaultMutableTreeNode(new TreeEntry(GUI.getString("Classbook.Methods"), AutoCompleteInformation.METHOD));
			DefaultMutableTreeNode propertyNodes = new DefaultMutableTreeNode(new TreeEntry(GUI.getString("Classbook.Properties"), AutoCompleteInformation.PROPERTY));
			for(TreeEntry te:methods) { 
				methodNodes.add(new DefaultMutableTreeNode(te));
			}
			for(TreeEntry te:properties) {
				propertyNodes.add(new DefaultMutableTreeNode(te));
			}
			if (!propertyNodes.isLeaf()) {
				clsNode.add(propertyNodes);
			}
			if (!methodNodes.isLeaf()) {
				clsNode.add(methodNodes);
			}
			root.add(clsNode);
		}
	}
	private TooltipTree content;
	private ClassDoc() {
		content = new TooltipTree(root);
		content.setCellRenderer(new ClassCellRenderer());
		content.setRootVisible(false);
		getContentPane().add(new JScrollPane(content));
		
	}
	private static class TreeEntry implements Comparable<TreeEntry> {
		public final String text;
		public final int type;
		public final String tooltip;
	    
		public TreeEntry(String text, int type) {
			this.text = text;
			this.type = type;
			this.tooltip = "";
		}

		TreeEntry(de.lathanda.eos.interpreter.Type type) {
			this.text = type.getName();
			this.type = AutoCompleteInformation.CLASS;
			this.tooltip = "";
		}
		TreeEntry(AutoCompleteInformation ace) {
			this.tooltip = "<html><p>"+ace.getTooltip()+"</p></html>";
			this.text    = ace.getLabel();
			this.type    = ace.getType();
		}
		@Override
		public int compareTo(TreeEntry b) {
			return this.text.compareTo(b.text);
		}
	}
	private class TooltipTree extends JTree {
		private static final long serialVersionUID = -7313420722740426372L;

		public TooltipTree(DefaultMutableTreeNode root) {
			super(root);
			ToolTipManager.sharedInstance().registerComponent(this);
		}

		@Override
		public String getToolTipText(MouseEvent me) {
			TreePath path = getPathForLocation(me.getX(), me.getY());
			if (path != null) {
				TreeEntry te = (TreeEntry)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
				return te.tooltip;
			} else {
				return null;
			}
		}	
	}
    private static class ClassCellRenderer implements TreeCellRenderer {
        private JLabel label;

        ClassCellRenderer() {
            label = new JLabel();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            Object o = ((DefaultMutableTreeNode) value).getUserObject();
           	TreeEntry te = (TreeEntry) o;
            label.setText(te.text);
            label.setIcon(AutoCompleteInformation.ICON[te.type]);
            label.setToolTipText(te.tooltip);
            return label;
        }
    }	
}
