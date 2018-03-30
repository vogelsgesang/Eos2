package de.lathanda.eos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.Scrollable;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.common.gui.BackgroundCompiler;
import de.lathanda.eos.common.gui.CodeColoring;
import de.lathanda.eos.common.gui.ConfigFrame;
import de.lathanda.eos.common.gui.GuiConfiguration;
import de.lathanda.eos.common.gui.GuiConfiguration.GuiConfigurationListener;
import de.lathanda.eos.common.gui.HtmlExport;
import de.lathanda.eos.common.gui.PrintFrame;
import de.lathanda.eos.common.gui.SideInformation;
import de.lathanda.eos.common.gui.SourceCode;
import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.gui.classchart.ClassChart;
import de.lathanda.eos.gui.diagram.DiagramFrame;
import de.lathanda.eos.gui.flowchart.FlowChart;
import de.lathanda.eos.gui.objectchart.ObjectChart;
import de.lathanda.eos.gui.structogram.Structogram;
import de.lathanda.eos.spi.LanguageManager;
import de.lathanda.eos.util.GuiToolkit;

/**
 * Das Hauptfenster.
 *
 * @author Peter (Lathanda) Schneider
 */
public class MainWindow extends JFrame implements WindowListener, GuiConfigurationListener {
	private static final long serialVersionUID = -5517007240148560239L;
	/**
	 * Der aktuelle Quellcode.
	 */
	private final SourceCode data;
	/**
	 * Laden/Speichern Dateidialog
	 */
	private final JFileChooser filechooser;
	/**
	 * Export Dateidialog
	 */
	private final JFileChooser exportfilechooser;
	/**
	 * Konfigurationsfenster
	 */
	private final ConfigFrame configFrame = new ConfigFrame();
	/**
	 * Aktuell geöffnete Datei
	 */
	private File activeFile = null;
	/**
	 * Hintergrund Compiler
	 */
	private final BackgroundCompiler compiler;

	/**
	 * Erzeugt das Hauptfenster.
	 */
	public MainWindow() {
		data = new SourceCode();
		compiler = new BackgroundCompiler(data);
		compiler.addCompilerListener(data);
		data.setSpeed(10);
		filechooser = new JFileChooser();
		filechooser.setFileFilter(new FileNameExtensionFilter(Messages.getString("File.EOS"), "eos"));
		filechooser.setCurrentDirectory(new File("."));
		exportfilechooser = new JFileChooser();
		exportfilechooser.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString("File.Html"), "html"));
		exportfilechooser.setCurrentDirectory(new File("."));
		initComponents();
		data.init(new AutoCompletion(txtProgram, this), new CodeColoring());
		sliderSpeed.setToolTipText(MessageFormat.format(Messages.getString("Speed.Tooltip"), data.getSpeed()));
		setIconImage(ResourceLoader.loadImage("icons/eos.png"));

		txtProgram.setDocument(data);
		txtOutput.setDocument(data.getOutput());
		txtProgram.requestFocus();
		GuiConfiguration.def.addConfigurationListener(this);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new FunctionKeyDispatcher());
	}

	/**
	 * Startet den Hintergrund Übersetzungsthread.
	 */
	public void startCompiler() {
		new Thread(compiler).start();
	}

	/**
	 * Alle Komponenten einrichten.
	 */
	private void initComponents() {
		setIconImage(ResourceLoader.loadImage("icon/eos.png"));
		mainMenu = new JMenuBar();
		mainToolbar = new JToolBar();
		jSeparator1 = new JToolBar.Separator();
		jSeparator2 = new JToolBar.Separator();
		jSeparator3 = new JToolBar.Separator();
		jSeparator4 = new JToolBar.Separator();
		mainSplit = new JSplitPane();
		scrollProgram = new JScrollPane();
		txtProgram = new JTextPane();
		sideInformation = new SideInformation(txtProgram, data);
		scrollOutput = new JScrollPane();
		txtOutput = new JTextPane();
		runToolbar = new JToolBar();
		toolbarGroup = new JPanel();
		sliderSpeed = new JSlider();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		setTitle(Messages.getString("Title"));
		addWindowListener(this);

		toolbarGroup.setLayout(new GridLayout(2, 1));

		mainToolbar.setFloatable(false);
		mainToolbar.setRollover(true);
		mainToolbar.setBackground(new Color(0xEEEEEE));
		toolbarGroup.add(mainToolbar);

		btnSave = GuiToolkit.createButton("icons/floppy_disk.png", Messages.getString("Menu.Save.Tooltip"),
				evt -> SaveActionPerformed(evt));
		mainToolbar.add(btnSave);

		btnOpen = GuiToolkit.createButton("icons/folder_open.png", Messages.getString("Menu.Open.Tooltip"),
				evt -> OpenActionPerformed(evt));
		mainToolbar.add(btnOpen);

		mainToolbar.add(jSeparator1);

		btnCopy = GuiToolkit.createButton("icons/copy.png", Messages.getString("Menu.Copy.Tooltip"),
				evt -> CopyActionPerformed(evt));
		mainToolbar.add(btnCopy);

		btnCut = GuiToolkit.createButton("icons/cut.png", Messages.getString("Menu.Cut.Tooltip"),
				evt -> CutActionPerformed(evt));
		mainToolbar.add(btnCut);

		btnPaste = GuiToolkit.createButton("icons/clipboard_paste.png", Messages.getString("Menu.Paste.Tooltip"),
				evt -> PasteActionPerformed(evt));
		mainToolbar.add(btnPaste);

		mainToolbar.add(jSeparator2);

		btnUndo = GuiToolkit.createButton("icons/undo.png", Messages.getString("Menu.Undo.Tooltip"),
				evt -> UndoActionPerformed(evt));
		mainToolbar.add(btnUndo);

		btnRedo = GuiToolkit.createButton("icons/redo.png", Messages.getString("Menu.Redo.Tooltip"),
				evt -> RedoActionPerformed(evt));
		mainToolbar.add(btnRedo);

		mainToolbar.add(jSeparator3);

		btnBreakpoint = GuiToolkit.createButton("icons/sign_stop.png", Messages.getString("Tooltip.Breakpoint"),
				evt -> BreakpointActionPerformed(evt));
		mainToolbar.add(btnBreakpoint);

		mainToolbar.add(jSeparator4);

		btnClassDoc = GuiToolkit.createButton("icons/books.png", Messages.getString("Menu.Classbook.Tooltip"),
				evt -> ClassDocActionPerformed(evt));
		mainToolbar.add(btnClassDoc);

		btnHelp = GuiToolkit.createButton("icons/question_and_answer.png", Messages.getString("Menu.Handbook.Tooltip"),
				evt -> HelpActionPerformed(evt));
		mainToolbar.add(btnHelp);

		mainSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplit.setResizeWeight(1.0);

		txtProgram.setFont(GuiToolkit.createFont(Font.MONOSPACED, Font.PLAIN, GuiConfiguration.def.getFontsize()));
		scrollProgram.setViewportView(new NoTextWrapContainer(txtProgram));
		scrollProgram.getViewport().setBackground(Color.WHITE);
		scrollProgram.setRowHeaderView(sideInformation);

		mainSplit.setTopComponent(scrollProgram);

		txtOutput.setEditable(false);
		txtOutput.setFont(GuiToolkit.createFont("Courier New", Font.PLAIN, 12));
		txtOutput.setFocusable(false);
		scrollOutput.setViewportView(txtOutput);

		mainSplit.setRightComponent(scrollOutput);

		runToolbar.setFloatable(false);
		runToolbar.setRollover(true);
		runToolbar.setBackground(new Color(0xEEEEEE));
		runToolbar.setLayout(new GridBagLayout());
		
		GridBagConstraints LAST = new GridBagConstraints();
		LAST.fill = GridBagConstraints.BOTH;
		LAST.weightx = 1;
		LAST.weighty = 1;
		toolbarGroup.add(runToolbar);

		btnStart = GuiToolkit.createButton("icons/media_play.png", Messages.getString("Tooltip.Start"),
				evt -> StartActionPerformed(evt));
		runToolbar.add(btnStart);

		btnSingleStep = GuiToolkit.createButton("icons/media_end.png", Messages.getString("Tooltip.SingleStep"),
				evt -> SingleStepActionPerformed(evt));
		runToolbar.add(btnSingleStep);

		btnPause = GuiToolkit.createButton("icons/media_pause.png", Messages.getString("Tooltip.Pause"),
				evt -> PauseActionPerformed(evt));
		runToolbar.add(btnPause);

		btnStop = GuiToolkit.createButton("icons/media_stop.png", Messages.getString("Tooltip.Stop"),
				evt -> StopActionPerformed(evt));
		runToolbar.add(btnStop);

		btnSkip = GuiToolkit.createButton("icons/media_fast_forward.png", Messages.getString("Tooltip.Skip"),
				evt -> SkipActionPerformed(evt));
		runToolbar.add(btnSkip);

		sliderSpeed = GuiToolkit.createSlider(Messages.getString("Run.Speed.Slider"),
				evt -> sliderSpeedStateChanged(evt));
		sliderSpeed.setValue(10);
		runToolbar.add(sliderSpeed, LAST);

		menuFile = GuiToolkit.createMenue(Messages.getString("Menu.File"));

		mitNew = GuiToolkit.createMenuItem(Messages.getString("Menu.New"), Messages.getString("Menu.New.Tooltip"),
				evt -> NewActionPerformed(evt), KeyEvent.VK_N);
		menuFile.add(mitNew);

		mitOpen = GuiToolkit.createMenuItem(Messages.getString("Menu.Open"), Messages.getString("Menu.Open.Tooltip"),
				evt -> OpenActionPerformed(evt), KeyEvent.VK_O);
		menuFile.add(mitOpen);

		mitSave = GuiToolkit.createMenuItem(Messages.getString("Menu.Save"), Messages.getString("Menu.Save.Tooltip"),
				evt -> SaveActionPerformed(evt), KeyEvent.VK_S);
		menuFile.add(mitSave);

		mitSaveAs = GuiToolkit.createMenuItem(Messages.getString("Menu.SaveAs"),
				Messages.getString("Menu.SaveAs.Tooltip"), evt -> SaveAsActionPerformed(evt));
		menuFile.add(mitSaveAs);

		mitSaveAsHtml = GuiToolkit.createMenuItem(Messages.getString("Menu.SaveAs.Html"),
				Messages.getString("Menu.SaveAs.Html.Tooltip"), evt -> SaveAsHtmlActionPerformed(evt));
		menuFile.add(mitSaveAsHtml);

		mitPrint = GuiToolkit.createMenuItem(Messages.getString("Menu.Print"), Messages.getString("Menu.Print.Tooltip"),
				evt -> PrintActionPerformed(evt), KeyEvent.VK_P);
		menuFile.add(mitPrint);

		mitConfig = GuiToolkit.createMenuItem(Messages.getString("Menu.Config"),
				Messages.getString("Menu.Config.Tooltip"), evt -> ConfigActionPerformed(evt));
		menuFile.add(mitConfig);

		mitExit = GuiToolkit.createMenuItem(Messages.getString("Menu.Close"), Messages.getString("Menu.Close.Tooltip"),
				evt -> ExitActionPerformed(evt));
		menuFile.add(mitExit);

		mainMenu.add(menuFile);

		editMenu = GuiToolkit.createMenue(Messages.getString("Menu.Edit"));

		mitCopy = GuiToolkit.createMenuItem(Messages.getString("Menu.Copy"), Messages.getString("Menu.Copy.Tooltip"),
				evt -> CopyActionPerformed(evt), KeyEvent.VK_C);
		editMenu.add(mitCopy);

		mitCut = GuiToolkit.createMenuItem(Messages.getString("Menu.Cut"), Messages.getString("Menu.Cut.Tooltip"),
				evt -> CutActionPerformed(evt), KeyEvent.VK_X);
		editMenu.add(mitCut);

		mitPaste = GuiToolkit.createMenuItem(Messages.getString("Menu.Paste"), Messages.getString("Menu.Paste.Tooltip"),
				evt -> PasteActionPerformed(evt), KeyEvent.VK_V);
		editMenu.add(mitPaste);

		mitUndo = GuiToolkit.createMenuItem(Messages.getString("Menu.Undo"), Messages.getString("Menu.Undo.Tooltip"),
				evt -> UndoActionPerformed(evt), KeyEvent.VK_Z);
		editMenu.add(mitUndo);

		mitRedo = GuiToolkit.createMenuItem(Messages.getString("Menu.Redo"), Messages.getString("Menu.Redo.Tooltip"),
				evt -> RedoActionPerformed(evt), KeyEvent.VK_Y);
		editMenu.add(mitRedo);

		mitPretty = GuiToolkit.createMenuItem(Messages.getString("Menu.Pretty"),
				Messages.getString("Menu.Pretty.Tooltip"), evt -> PrettyActionPerformed(evt), KeyEvent.VK_F);
		editMenu.add(mitPretty);

		mainMenu.add(editMenu);

		diagramMenu = GuiToolkit.createMenue(Messages.getString("Menu.Visualization"));

		mitStructogram = GuiToolkit.createMenuItem(Messages.getString("Menu.Structogram"),
				Messages.getString("Menu.Structogram.Tooltip"), evt -> StructogramActionPerformed(evt));
		diagramMenu.add(mitStructogram);

		mitFlowChart = GuiToolkit.createMenuItem(Messages.getString("Menu.Flowchart"),
				Messages.getString("Menu.Flowchart.Tooltip"), evt -> FlowChartActionPerformed(evt));
		diagramMenu.add(mitFlowChart);

		mitObjectChart = GuiToolkit.createMenuItem(Messages.getString("Menu.Objectchart"),
				Messages.getString("Menu.Objectchart.Tooltip"), evt -> ObjectChartActionPerformed(evt));
		diagramMenu.add(mitObjectChart);

		mitClassChart = GuiToolkit.createMenuItem(Messages.getString("Menu.Classchart"),
				Messages.getString("Menu.Classchart.Tooltip"), evt -> ClassChartActionPerformed(evt));
		diagramMenu.add(mitClassChart);

		mainMenu.add(diagramMenu);

		LanguageManager.getInstance().addPluginMenues(mainMenu);

		helpMenu = GuiToolkit.createMenue(Messages.getString("Menu.Help"));

		mitClassDoc = GuiToolkit.createMenuItem(Messages.getString("Menu.Classbook"),
				Messages.getString("Menu.Classbook.Tooltip"), evt -> ClassDocActionPerformed(evt));
		helpMenu.add(mitClassDoc);

		mitHelp = GuiToolkit.createMenuItem(Messages.getString("Menu.Handbook"),
				Messages.getString("Menu.Handbook.Tooltip"), evt -> HelpActionPerformed(evt), KeyEvent.VK_F1);
		helpMenu.add(mitHelp);

		mainMenu.add(helpMenu);

		setJMenuBar(mainMenu);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolbarGroup, BorderLayout.NORTH);
		getContentPane().add(mainSplit, BorderLayout.CENTER);
		mainSplit.setPreferredSize(GuiToolkit.scaledDimension(500, 600));
		pack();
		mainSplit.setDividerLocation(mainSplit.getHeight()-GuiToolkit.scaledSize(100));
	}

	/**
	 * Programm starten.
	 * 
	 * @param evt
	 */
	private void StartActionPerformed(java.awt.event.ActionEvent evt) {
		data.run();
	}

	/**
	 * Speichern.
	 * 
	 * @param evt
	 */
	private void SaveActionPerformed(java.awt.event.ActionEvent evt) {
		save();
	}

	/**
	 * Speichern
	 * 
	 * @return erfolgreich?
	 */
	private boolean save() {
		try {
			if (activeFile != null) {
				data.saveProgram(activeFile);
				ResourceLoader.setWorkingDirectory(activeFile.getParent());
				return true;
			} else {
				return saveAs();
			}
		} catch (IOException io) {
			JOptionPane.showMessageDialog(this, Messages.getString("Save.Error.Title"), io.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * Unter neuem Ort speichern.
	 * 
	 * @return erfolgreich?
	 */
	private boolean saveAs() {
		try {
			if (filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File target = filechooser.getSelectedFile();
				if (!target.getName().contains(".")) {
					target = new File(target.toString() + ".eos");
				}
				if (!overwriteSafetyCheck(target)) {
					return false;
				}
				data.saveProgram(target);
				activeFile = target;
				ResourceLoader.setWorkingDirectory(activeFile.getParent());
				return true;
			} else {
				return false;
			}
		} catch (IOException io) {
			JOptionPane.showMessageDialog(this, Messages.getString("Save.Error.Title"), io.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * Datei öffnen.
	 * 
	 * @param evt
	 */
	private void OpenActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				if (dirtySafetyCheck()) {
					data.loadProgram(filechooser.getSelectedFile());
					activeFile = filechooser.getSelectedFile();
					ResourceLoader.setWorkingDirectory(activeFile.getParent());
				}
			}
		} catch (IOException io) {
			JOptionPane.showMessageDialog(this, Messages.getString("Open.Error.Title"), io.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Externes Laden.
	 * 
	 * @param file
	 *            Dateiname
	 * @throws IOException
	 */
	public void load(File file) {

		try {
			filechooser.setSelectedFile(file);
			data.loadProgram(file);
			activeFile = file;
			ResourceLoader.setWorkingDirectory(file.getParent());
		} catch (IOException io) {
			JOptionPane.showMessageDialog(this, Messages.getString("Open.Error.Title"), io.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Undo.
	 * 
	 * @param evt
	 */
	private void UndoActionPerformed(java.awt.event.ActionEvent evt) {
		data.undo();
	}

	/**
	 * Redo.
	 * 
	 * @param evt
	 */
	private void RedoActionPerformed(java.awt.event.ActionEvent evt) {
		data.redo();
	}

	/**
	 * Neu Formatieren.
	 * 
	 * @param evt
	 */
	private void PrettyActionPerformed(java.awt.event.ActionEvent evt) {
		data.prettyPrint();
	}

	/**
	 * Kopieren.
	 * 
	 * @param evt
	 */
	private void CopyActionPerformed(java.awt.event.ActionEvent evt) {
		txtProgram.copy();
	}

	/**
	 * Ausschneiden.
	 * 
	 * @param evt
	 */
	private void CutActionPerformed(java.awt.event.ActionEvent evt) {
		txtProgram.cut();
	}

	/**
	 * Einfügen.
	 * 
	 * @param evt
	 */
	private void PasteActionPerformed(java.awt.event.ActionEvent evt) {
		txtProgram.paste();
	}

	/**
	 * Neues Programm.
	 * 
	 * @param evt
	 */
	private void NewActionPerformed(java.awt.event.ActionEvent evt) {
		if (dirtySafetyCheck()) {
			data.clear();
			activeFile = null;
			data.discardAllEdits();
		}
	}

	/**
	 * Hilfe anzeigen.
	 * 
	 * @param evt
	 */
	private void HelpActionPerformed(java.awt.event.ActionEvent evt) {
		Help.showHelp();
	}

	/**
	 * Klassenhilfe anzeigen.
	 * 
	 * @param evt
	 */
	private void ClassDocActionPerformed(java.awt.event.ActionEvent evt) {
		ClassDoc.showDoc();
	}

	/**
	 * Einzelschritt.
	 * 
	 * @param evt
	 */
	private void SingleStepActionPerformed(java.awt.event.ActionEvent evt) {
		data.singleStep();
	}

	/**
	 * Programmausführung unterbrechen.
	 * 
	 * @param evt
	 */
	private void PauseActionPerformed(java.awt.event.ActionEvent evt) {
		data.pause();
	}

	/**
	 * Programm anhalten.
	 * 
	 * @param evt
	 */
	private void StopActionPerformed(java.awt.event.ActionEvent evt) {
		data.stop();
	}

	/**
	 * Programm mit maximaler Geschwindigkeit ausführen.
	 * 
	 * @param evt
	 */
	private void SkipActionPerformed(java.awt.event.ActionEvent evt) {
		data.skip();
	}

	/**
	 * Geschwindigkeit ändern.
	 * 
	 * @param evt
	 */
	private void sliderSpeedStateChanged(ChangeEvent evt) {
		data.setSpeed(sliderSpeed.getValue());
		sliderSpeed.setToolTipText(MessageFormat.format(Messages.getString("Speed.Tooltip"), data.getSpeed()));
	}

	/**
	 * Unter neuem Ort speichern.
	 * 
	 * @param evt
	 */
	private void SaveAsActionPerformed(java.awt.event.ActionEvent evt) {
		saveAs();
	}

	/**
	 * Haltepunkt setzen.
	 * 
	 * @param evt
	 */
	private void BreakpointActionPerformed(java.awt.event.ActionEvent evt) {
		data.setToggleBreakpoint(txtProgram.getCaretPosition());
		sideInformation.repaint();
	}

	/**
	 * Druckvorschau anzeigen.
	 * 
	 * @param evt
	 */
	private void PrintActionPerformed(java.awt.event.ActionEvent evt) {
		PrintFrame pf = new PrintFrame(
				(activeFile != null) ? activeFile.getName() : Messages.getString("Print.Noname"));
		pf.init(data.getProgram());
		pf.setVisible(true);
	}

	/**
	 * Konfigurationsfenster anzeigen.
	 * 
	 * @param evt
	 */
	private void ConfigActionPerformed(java.awt.event.ActionEvent evt) {
		configFrame.setVisible(true);
	}

	/**
	 * Programm beenden.
	 * 
	 * @param evt
	 */
	private void ExitActionPerformed(java.awt.event.ActionEvent evt) {
		closeApp();
	}

	/**
	 * Programm als HTML exportieren.
	 * 
	 * @param evt
	 */
	private void SaveAsHtmlActionPerformed(java.awt.event.ActionEvent evt) {

		if (exportfilechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = exportfilechooser.getSelectedFile();
			if (!overwriteSafetyCheck(file)) {
				return;
			}
			String text = HtmlExport.export2html(data.getProgram(), file.getName());
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "Utf-8"))) {
				bw.append(text);
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, Messages.getString("Export.Error.Title"), ioe.getLocalizedMessage(),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Struktogramm ansicht anzeigen.
	 * 
	 * @param evt
	 */
	private void StructogramActionPerformed(java.awt.event.ActionEvent evt) {
		new DiagramFrame(new Structogram(), compiler).setVisible(true);
	}

	/**
	 * Kontrollflussansicht anzeigen.
	 * 
	 * @param evt
	 */
	private void FlowChartActionPerformed(java.awt.event.ActionEvent evt) {
		new DiagramFrame(new FlowChart(), compiler).setVisible(true);
	}

	/**
	 * Objectdiagramm anzeigen.
	 * 
	 * @param evt
	 */
	private void ObjectChartActionPerformed(java.awt.event.ActionEvent evt) {
		new DiagramFrame(new ObjectChart(), compiler).setVisible(true);
	}

	/**
	 * Klassendiagramm anzeigen.
	 * 
	 * @param evt
	 */
	private void ClassChartActionPerformed(java.awt.event.ActionEvent evt) {
		new DiagramFrame(new ClassChart(), compiler).setVisible(true);
	}

	/**
	 * Anwendung schliessen.
	 */
	private void closeApp() {
		if (dirtySafetyCheck()) {
			System.exit(0);
		}
	}

	private boolean overwriteSafetyCheck(File file) {
		if (!file.exists()) {
			return true;
		}
		int answer = JOptionPane.showConfirmDialog(this, Messages.getString("Save.Overwrite.Text"),
				Messages.getString("Save.Overwrite.Title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		return answer == JOptionPane.OK_OPTION;
	}

	private boolean dirtySafetyCheck() {
		if (data.isSourceDirty()) {
			int answer = JOptionPane.showConfirmDialog(this, Messages.getString("Closing.Save.Text"),
					Messages.getString("Closing.Save.Title"), JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			switch (answer) {
			case JOptionPane.YES_OPTION:
				if (save()) {
					return true;
				} else {
					return false;
				}
			case JOptionPane.NO_OPTION:
				return true;
			case JOptionPane.CANCEL_OPTION:
				return false;
			default:
				return false;
			}
		} else {
			return true;
		}
	}
	public void fontsizeChanged(int fontsize) {
		txtProgram.setFont(GuiToolkit.createFont(Font.MONOSPACED, Font.PLAIN, fontsize));
	}

	private JButton btnCopy;
	private JButton btnCut;
	private JButton btnHelp;
	private JButton btnClassDoc;
	private JButton btnOpen;
	private JButton btnPaste;
	private JButton btnPause;
	private JButton btnRedo;
	private JButton btnSave;
	private JButton btnBreakpoint;
	private JButton btnSingleStep;
	private JButton btnSkip;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnUndo;
	private JMenu diagramMenu;
	private JMenu editMenu;
	private JMenu helpMenu;
	private JToolBar.Separator jSeparator1;
	private JToolBar.Separator jSeparator2;
	private JToolBar.Separator jSeparator3;
	private JToolBar.Separator jSeparator4;
	private JMenuBar mainMenu;
	private JSplitPane mainSplit;
	private JToolBar mainToolbar;
	private JPanel toolbarGroup;
	private JMenu menuFile;
	private JMenuItem mitConfig;
	private JMenuItem mitCopy;
	private JMenuItem mitCut;
	private JMenuItem mitPretty;
	private JMenuItem mitExit;
	private JMenuItem mitFlowChart;
	private JMenuItem mitObjectChart;
	private JMenuItem mitClassChart;
	private JMenuItem mitHelp;
	private JMenuItem mitClassDoc;
	private JMenuItem mitNew;
	private JMenuItem mitOpen;
	private JMenuItem mitPaste;
	private JMenuItem mitPrint;
	private JMenuItem mitRedo;
	private JMenuItem mitSave;
	private JMenuItem mitSaveAs;
	private JMenuItem mitSaveAsHtml;
	private JMenuItem mitStructogram;

	private JMenuItem mitUndo;
	private JToolBar runToolbar;
	private JScrollPane scrollOutput;
	private JScrollPane scrollProgram;
	private JSlider sliderSpeed;
	private JTextPane txtOutput;
	private JTextPane txtProgram;
	private SideInformation sideInformation;

	// Window Listener
	@Override
	public void windowActivated(WindowEvent we) {
	}

	@Override
	public void windowClosed(WindowEvent we) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		closeApp();
	}

	@Override
	public void windowDeactivated(WindowEvent we) {
	}

	@Override
	public void windowDeiconified(WindowEvent we) {
	}

	@Override
	public void windowIconified(WindowEvent we) {
	}

	@Override
	public void windowOpened(WindowEvent we) {
	}

	/**
	 * Diese Klasse dient als Workarround. Um den automatischen Umbruch des
	 * JTextPane innerhalb von JScrollPane zu unterbinden. Die JRE aktiviert
	 * automatisch einen Zeilenumbruch, wenn der Container ein Viewport ist. Diese
	 * Klasse schaltet sich zwischen den JTextPane und den Viewport und stört damit
	 * diese Erkennung.
	 * 
	 * @author Peter (Lathanda) Schneider
	 *
	 */
	private static class NoTextWrapContainer extends JPanel implements Scrollable {
		private static final long serialVersionUID = 1687707567072204851L;
		private Scrollable scrollable;

		public NoTextWrapContainer(JTextPane textpane) {
			super(new BorderLayout());
			scrollable = textpane;
			add(textpane);
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return scrollable.getPreferredScrollableViewportSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return scrollable.getScrollableBlockIncrement(visibleRect, orientation, direction);
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return scrollable.getScrollableTracksViewportHeight();
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return scrollable.getScrollableTracksViewportWidth();
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return scrollable.getScrollableUnitIncrement(visibleRect, orientation, direction);
		}

	}
	private class FunctionKeyDispatcher implements KeyEventDispatcher {

		@Override
		public boolean dispatchKeyEvent(KeyEvent ke) {
			if (ke.getID() == KeyEvent.KEY_PRESSED) {
				switch (ke.getKeyCode()) {
				case KeyEvent.VK_F5:
					data.run();
					break;
				case KeyEvent.VK_F6:
					data.singleStep();
					break;
				case KeyEvent.VK_F7:
					data.pause();
					break;
				case KeyEvent.VK_F8:
					data.stop();
					break;
				case KeyEvent.VK_F9:
					data.skip();
					break;
				}
			}
			return false;
		}
	
	}
}
