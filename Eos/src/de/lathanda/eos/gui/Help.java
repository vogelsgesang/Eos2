package de.lathanda.eos.gui;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.spi.LanguageManager;
import de.lathanda.eos.util.GuiToolkit;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Hilfeseiten
 *
 * @author Peter (Lathanda) Schneider
 */
public class Help extends JFrame {
	private static final long serialVersionUID = -5024616463654399568L;
	/**
	 * Hilfefenster Singelton
	 */
	private static Help help = null;

	/**
	 * Hilfefenster anzeigen.
	 */
	public static void showHelp() {
		if (help == null) {
			help = new Help();
		} else if (help.isVisible()) {
			return;
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		help.setBounds((int) (screen.width * 0.5), 0, (int) (screen.height * 0.9), (int) (screen.width * 0.5));
		help.setTitle(Messages.getString("Help.Title"));
		help.setIconImage(ResourceLoader.loadImage("icons/eos.png"));
		help.setVisible(true);
		help.resetDivider();
	}

	private void resetDivider() {
		helpSplit.setDividerLocation(helpSplit.getWidth() - GuiToolkit.scaledSize(168));
	}

	/**
	 * Inhaltsverzeichnis
	 */
	private static DefaultMutableTreeNode topics;
	/**
	 * Standard Htmlseite.
	 */
	private static String defaultHtml;
	/**
	 * Klassenkonstruktor lädt die Hilfe.
	 */
	static {
		createTopics();
	}

	/**
	 * Hilfeseiten vorbereiten.
	 */
	private static void createTopics() {
		InputStream xmlData = null;
		try {
			xmlData = LanguageManager.getInstance().getHelpXml();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlData);
			NodeList xmlchildren = doc.getChildNodes();
			for (int i = 0; i < xmlchildren.getLength(); i++) {
				Node xmlchild = xmlchildren.item(i);
				if (xmlchild.getNodeName().equals("help")) {
					topics = new DefaultMutableTreeNode(new TreeEntry(xmlchild));
					fillTree(topics, xmlchild);
					break;
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Messages.getString("InternalError.Title"),
					JOptionPane.ERROR_MESSAGE);
		} finally {
			ResourceLoader.closeQuietly(xmlData);
		}

	}

	/**
	 * Inhalteverzeichnis ausfüllen
	 * 
	 * @param treenode
	 * @param xmlnode
	 */
	private static void fillTree(DefaultMutableTreeNode treenode, Node xmlnode) {
		NodeList xmlchildren = xmlnode.getChildNodes();
		for (int i = 0; i < xmlchildren.getLength(); i++) {
			Node xmlchild = xmlchildren.item(i);
			if (xmlchild.getNodeName().equals("node")) {
				TreeEntry treeEntry = new TreeEntry(xmlchild);
				if (defaultHtml == null || defaultHtml.isEmpty()) {
					defaultHtml = treeEntry.getHtmlfile();
				}
				DefaultMutableTreeNode treechild = new DefaultMutableTreeNode(treeEntry);
				treenode.add(treechild);
				fillTree(treechild, xmlchild);
			}
		}
	}

	/**
	 * Eintrag im Inhaltsverzeichnis. Jeder Eintrag entspricht einer Hilfeseite.
	 * 
	 * @author Peter (Lathanda) Schneider
	 *
	 */
	private static class TreeEntry {
		/**
		 * Text im Inhaltsverzeichnis.
		 */
		private final String title;
		/**
		 * Hilfeseite.
		 */
		private final String htmlfile;

		/**
		 * Hilfeseite aus XML erzeugen.
		 * 
		 * @param xmlnode
		 */
		private TreeEntry(Node xmlnode) {
			Node item = xmlnode.getAttributes().getNamedItem("title");
			if (item != null) {
				title = item.getNodeValue();
			} else {
				title = "?";
			}
			item = xmlnode.getAttributes().getNamedItem("html");
			if (item != null) {
				htmlfile = item.getNodeValue();
			} else {
				htmlfile = "";
			}
		}

		@Override
		public String toString() {
			return title;
		}

		public String getHtmlfile() {
			return htmlfile;
		}

	}

	/**
	 * Html Hilfeklasse
	 */
	HTMLEditorKit kit = new HTMLEditorKit();

	/**
	 * Neue Hilfeseite erzeugen.
	 */
	public Help() {
		initComponents();
		treeTopic.setRootVisible(false);
		treeTopic.setFont(GuiToolkit.createFont(Font.SANS_SERIF, Font.PLAIN, 10));
		txtHelp.setEditorKit(kit);
		txtHelp.setFont(GuiToolkit.createFont(Font.SERIF, Font.PLAIN, 10));
		javax.swing.text.Document doc = kit.createDefaultDocument();
		txtHelp.setDocument(doc);
		setHtml(defaultHtml);
	}

	/**
	 * Komponenten initialisieren.
	 */
	private void initComponents() {

		helpSplit = new javax.swing.JSplitPane();
		scrollTopic = new javax.swing.JScrollPane();
		treeTopic = new javax.swing.JTree(topics);
		scrollHelp = new javax.swing.JScrollPane();
		txtHelp = new javax.swing.JTextPane();

		helpSplit.setResizeWeight(1);

		treeTopic.addTreeSelectionListener(evt -> treeTopicValueChanged(evt));
		expandAll(treeTopic);
		scrollTopic.setViewportView(treeTopic);

		helpSplit.setRightComponent(scrollTopic);

		txtHelp.setEditable(false);
		txtHelp.setFont(GuiToolkit.createFont(Font.SANS_SERIF, Font.PLAIN, 10));
		txtHelp.addHyperlinkListener(e -> hyperlinkUpdate(e));
		scrollHelp.setViewportView(txtHelp);

		helpSplit.setLeftComponent(scrollHelp);

		helpSplit.setPreferredSize(GuiToolkit.scaledDimension(768, 517));
		getContentPane().add(helpSplit);
		pack();
	}

	/**
	 * Hilfsmethode klappt alle Knoten auf.
	 * 
	 * @param tree
	 */
	private void expandAll(JTree tree) {
		expandAll(tree, new TreePath((TreeNode) tree.getModel().getRoot()));
	}

	/**
	 * rekursive Hilfsmethode klappt alle Unterknoten auf.
	 * 
	 * @param tree
	 * @param parent
	 */
	private void expandAll(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
			TreeNode n = (TreeNode) e.nextElement();
			TreePath path = parent.pathByAddingChild(n);
			expandAll(tree, path);
		}
		tree.expandPath(parent);
	}

	/**
	 * Ein Link wurde aktiviert.
	 * 
	 * @param e
	 */
	private void hyperlinkUpdate(HyperlinkEvent e) {
		try {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				URL target = e.getURL();
				if (target.getProtocol().equals("file")) {
					treeTopic.clearSelection();
					txtHelp.setPage(target);
				} else if (target.getProtocol().equals("jar")){
					treeTopic.clearSelection();
					txtHelp.setPage(target);
				}else {
					URI uri = target.toURI();
					Desktop.getDesktop().browse(uri);
				}
			}
		} catch (IOException | URISyntaxException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getLocalizedMessage(), Messages.getString("Info.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Im Inhaltsverzeichnis wurde die Auswahl geändert.
	 * 
	 * @param evt
	 */
	private void treeTopicValueChanged(javax.swing.event.TreeSelectionEvent evt) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeTopic.getLastSelectedPathComponent();
		if (node == null)
			return;
		TreeEntry entry = (TreeEntry) node.getUserObject();
		setHtml(entry.getHtmlfile());
	}

	/**
	 * Neue Hilfeseite anzeigen. Hier können auch externe Seiten angegeben werden.
	 * Bei externen Seiten wird die Auswahl im Inhaltsverzeichnis gelöscht.
	 * 
	 * @param filename
	 */
	private void setHtml(String filename) {
		if (filename.equals(""))
			return;
		try {
			URL url = ClassLoader.getSystemClassLoader().getResource(filename);
			txtHelp.setPage(url);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getLocalizedMessage(), Messages.getString("Info.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private javax.swing.JSplitPane helpSplit;
	private javax.swing.JScrollPane scrollHelp;
	private javax.swing.JScrollPane scrollTopic;
	private javax.swing.JTree treeTopic;
	private javax.swing.JTextPane txtHelp;
}
