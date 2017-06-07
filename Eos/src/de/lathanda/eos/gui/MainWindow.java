package de.lathanda.eos.gui;

import static de.lathanda.eos.common.gui.GuiConstants.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.common.gui.BackgroundCompiler;
import de.lathanda.eos.common.gui.CodeColoring;
import de.lathanda.eos.common.gui.ConfigFrame;
import de.lathanda.eos.common.gui.GuiConfiguration;
import de.lathanda.eos.common.gui.HtmlExport;
import de.lathanda.eos.common.gui.PrintFrame;
import de.lathanda.eos.common.gui.SideInformation;
import de.lathanda.eos.common.gui.SourceCode;
import de.lathanda.eos.gui.classchart.ClassChart;
import de.lathanda.eos.gui.diagram.DiagramFrame;
import de.lathanda.eos.gui.flowchart.FlowChart;
import de.lathanda.eos.gui.objectchart.ObjectChart;
import de.lathanda.eos.gui.structogram.Structogram;
import de.lathanda.eos.spi.LanguageManager;

/**
 * Das Hauptfenster.
 *
 * @author Peter (Lathanda) Schneider
 */
public class MainWindow extends JFrame implements WindowListener {
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
     * Undostack
     */
    private final UndoManager undoManager;
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
        undoManager = new UndoManager();
        data.addUndoableEditListener(undoManager);
        filechooser = new JFileChooser();
        filechooser.setFileFilter(new FileNameExtensionFilter(GUI.getString("File.EOS"), "eos"));
        filechooser.setCurrentDirectory(new File("."));
        exportfilechooser = new JFileChooser();
        exportfilechooser.addChoosableFileFilter(new FileNameExtensionFilter(GUI.getString("File.Html"), "html"));
        exportfilechooser.setCurrentDirectory(new File("."));
        initComponents();
        data.init(new AutoCompletion(txtProgram, this), new CodeColoring());
        sliderSpeed.setToolTipText(MessageFormat.format(GUI.getString("Speed.Tooltip"), data.getSpeed()));
        setIconImage(ResourceLoader.loadImage("icons/eos.png"));
        
        txtProgram.setDocument(data);
        txtOutput.setDocument(data.getOutput());
        txtProgram.requestFocus();
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

        mainToolbar = new JToolBar();
        btnSave = new JButton();
        btnOpen = new JButton();
        jSeparator2 = new JToolBar.Separator();
        btnCopy = new JButton();
        btnCut = new JButton();
        btnPaste = new JButton();
        jSeparator1 = new JToolBar.Separator();
        btnUndo = new JButton();
        btnRedo = new JButton();
        jSeparator3 = new JToolBar.Separator();        
        btnBreakpoint = new JButton();
        jSeparator4 = new JToolBar.Separator();
        btnHelp = new JButton();
        btnClassDoc = new JButton();
        mainSplit = new JSplitPane();
        scrollProgram = new JScrollPane();
        txtProgram = new JTextPane();
        sideInformation = new SideInformation(txtProgram, data);
        scrollOutput = new JScrollPane();
        txtOutput = new JTextPane();
        runToolbar = new JToolBar();
        toolbarGroup = new JPanel();
        btnStart = new JButton();
        btnSingleStep = new JButton();
        btnPause = new JButton();
        btnStop = new JButton();
        btnSkip = new JButton();
        sliderSpeed = new JSlider();
        mainMenu = new JMenuBar();
        menuFile = new JMenu();
        mitNew = new JMenuItem();
        mitOpen = new JMenuItem();
        mitSave = new JMenuItem();
        mitSaveAs = new JMenuItem();
        mitSaveAsHtml = new JMenuItem();
        mitPrint = new JMenuItem();
        mitConfig = new JMenuItem();
        mitExit = new JMenuItem();
        editMenu = new JMenu();
        mitCopy = new JMenuItem();
        mitCut = new JMenuItem();
        mitPaste = new JMenuItem();
        mitUndo = new JMenuItem();
        mitRedo = new JMenuItem();
        mitPretty = new JMenuItem();
        diagramMenu = new JMenu();
        mitStructogram = new JMenuItem();
        mitFlowChart = new JMenuItem();
        mitObjectChart = new JMenuItem();
        mitClassChart = new JMenuItem();
        helpMenu = new JMenu();
        mitHelp = new JMenuItem();
        mitClassDoc = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        setTitle(GUI.getString("Title"));
        addWindowListener(this);

        mainToolbar.setFloatable(false);
        mainToolbar.setRollover(true);
        
        toolbarGroup.setLayout(new GridLayout(2,1)); 
        
        btnSave.setIcon(new ImageIcon(getClass().getResource("/icons/floppy_disk.png")));
        btnSave.setToolTipText(GUI.getString("Menu.Save.Tooltip"));
        btnSave.setFocusable(false);
        btnSave.addActionListener(evt -> SaveActionPerformed(evt));

        mainToolbar.add(btnSave);

        btnOpen.setIcon(new ImageIcon(getClass().getResource("/icons/folder_open.png")));
        btnOpen.setToolTipText(GUI.getString("Menu.Open.Tooltip"));
        btnOpen.setFocusable(false);
        btnOpen.addActionListener(evt -> OpenActionPerformed(evt));

        mainToolbar.add(btnOpen);
        mainToolbar.add(jSeparator2);
        toolbarGroup.add(mainToolbar);

        btnCopy.setIcon(new ImageIcon(getClass().getResource("/icons/copy.png")));
        btnCopy.setToolTipText(GUI.getString("Menu.Copy.Tooltip"));
        btnCopy.setFocusable(false);
        btnCopy.addActionListener(evt -> CopyActionPerformed(evt));

        mainToolbar.add(btnCopy);

        btnCut.setIcon(new ImageIcon(getClass().getResource("/icons/cut.png")));
        btnCut.setToolTipText(GUI.getString("Menu.Cut.Tooltip"));
        btnCut.setFocusable(false);
        btnCut.addActionListener(evt -> CutActionPerformed(evt));

        mainToolbar.add(btnCut);

        btnPaste.setIcon(new ImageIcon(getClass().getResource("/icons/clipboard_paste.png")));
        btnPaste.setToolTipText(GUI.getString("Menu.Paste.Tooltip"));
        btnPaste.setFocusable(false);
        btnPaste.addActionListener(evt -> PasteActionPerformed(evt));

        mainToolbar.add(btnPaste);
        mainToolbar.add(jSeparator1);

        btnUndo.setIcon(new ImageIcon(getClass().getResource("/icons/undo.png")));
        btnUndo.setToolTipText(GUI.getString("Menu.Undo.Tooltip"));
        btnUndo.setFocusable(false);
        btnUndo.addActionListener(evt -> UndoActionPerformed(evt));

        mainToolbar.add(btnUndo);

        btnRedo.setIcon(new ImageIcon(getClass().getResource("/icons/redo.png")));
        btnRedo.setToolTipText(GUI.getString("Menu.Redo.Tooltip"));
        btnRedo.setFocusable(false);
        btnRedo.addActionListener(evt -> RedoActionPerformed(evt));

        mainToolbar.add(btnRedo);
        
        mainToolbar.add(jSeparator3);
        
        btnBreakpoint.setIcon(new ImageIcon(getClass().getResource("/icons/sign_stop.png")));
        btnBreakpoint.setToolTipText(GUI.getString("Tooltip.Breakpoint"));
        btnBreakpoint.setFocusable(false);
        btnBreakpoint.addActionListener(evt -> BreakpointActionPerformed(evt));
        
        mainToolbar.add(btnBreakpoint);
        
        mainToolbar.add(jSeparator4);
        
        btnClassDoc.setIcon(new ImageIcon(getClass().getResource("/icons/books.png")));
        btnClassDoc.setToolTipText(GUI.getString("Menu.Classbook.Tooltip"));
        btnClassDoc.setFocusable(false);
        btnClassDoc.addActionListener(evt -> ClassDocActionPerformed(evt));
        
        mainToolbar.add(btnClassDoc);
        
        btnHelp.setIcon(new ImageIcon(getClass().getResource("/icons/question_and_answer.png")));
        btnHelp.setToolTipText(GUI.getString("Menu.Handbook.Tooltip"));
        btnHelp.setFocusable(false);
        btnHelp.addActionListener(evt -> HelpActionPerformed(evt));

        mainToolbar.add(btnHelp);

        mainSplit.setDividerLocation(400);
        mainSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setResizeWeight(1.0);

        txtProgram.setFont(new Font("Courier New", Font.PLAIN, GuiConfiguration.def.getFontsize()));
        scrollProgram.setViewportView(new NoTextWrapContainer(txtProgram));
        scrollProgram.getViewport().setBackground(Color.WHITE);
        scrollProgram.setRowHeaderView(sideInformation);
        
        mainSplit.setTopComponent(scrollProgram);

        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtOutput.setFocusable(false);
        scrollOutput.setViewportView(txtOutput);

        mainSplit.setRightComponent(scrollOutput);

        runToolbar.setFloatable(false);
        runToolbar.setRollover(true);
        toolbarGroup.add(runToolbar);
        
        btnStart.setIcon(new ImageIcon(getClass().getResource("/icons/media_play.png")));
        btnStart.setToolTipText(GUI.getString("Tooltip.Start"));
        btnStart.setFocusable(false);
        btnStart.addActionListener(evt -> StartActionPerformed(evt));

        runToolbar.add(btnStart);

        btnSingleStep.setIcon(new ImageIcon(getClass().getResource("/icons/media_end.png")));
        btnSingleStep.setToolTipText(GUI.getString("Tooltip.SingleStep"));
        btnSingleStep.setFocusable(false);
        btnSingleStep.addActionListener(evt -> SingleStepActionPerformed(evt));

        runToolbar.add(btnSingleStep);

        btnPause.setIcon(new ImageIcon(getClass().getResource("/icons/media_pause.png")));
        btnPause.setToolTipText(GUI.getString("Tooltip.Pause"));
        btnPause.setFocusable(false);
        btnPause.addActionListener(evt -> PauseActionPerformed(evt));

        runToolbar.add(btnPause);

        btnStop.setIcon(new ImageIcon(getClass().getResource("/icons/media_stop.png")));
        btnStop.setToolTipText(GUI.getString("Tooltip.Stop"));
        btnStop.setFocusable(false);
        btnStop.addActionListener(evt -> StopActionPerformed(evt));

        runToolbar.add(btnStop);

        btnSkip.setIcon(new ImageIcon(getClass().getResource("/icons/media_fast_forward.png")));
        btnSkip.setToolTipText(GUI.getString("Tooltip.Skip"));
        btnSkip.setFocusable(false);
        btnSkip.addActionListener(evt -> SkipActionPerformed(evt));

        runToolbar.add(btnSkip);

        sliderSpeed.setValue(10);
        sliderSpeed.setFocusable(false);
        sliderSpeed.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), GUI.getString("Run.Speed.Slider"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", 0, 12)));
        sliderSpeed.setPreferredSize(new java.awt.Dimension(400, 48));
        sliderSpeed.addChangeListener(evt -> sliderSpeedStateChanged(evt));

        runToolbar.add(sliderSpeed);

        menuFile.setText(GUI.getString("Menu.File"));

        mitNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setText(GUI.getString("Menu.New"));
        mitNew.setToolTipText(GUI.getString("Menu.New.Tooltip"));
        mitNew.addActionListener(evt -> NewActionPerformed(evt));

        menuFile.add(mitNew);

        mitOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mitOpen.setText(GUI.getString("Menu.Open"));
        mitOpen.setToolTipText(GUI.getString("Menu.Open.Tooltip"));
        mitOpen.addActionListener(evt -> OpenActionPerformed(evt));

        menuFile.add(mitOpen);

        mitSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setText(GUI.getString("Menu.Save"));
        mitSave.setToolTipText(GUI.getString("Menu.Save.Tooltip"));
        mitSave.addActionListener(evt -> SaveActionPerformed(evt));

        menuFile.add(mitSave);

        mitSaveAs.setText(GUI.getString("Menu.SaveAs"));
        mitSaveAs.setToolTipText(GUI.getString("Menu.SaveAs.Tooltip"));
        mitSaveAs.addActionListener(evt -> SaveAsActionPerformed(evt));

        menuFile.add(mitSaveAs);

        mitSaveAsHtml.setText(GUI.getString("Menu.SaveAs.Html"));
        mitSaveAsHtml.setToolTipText(GUI.getString("Menu.SaveAs.Html.Tooltip"));
        mitSaveAsHtml.addActionListener(evt -> SaveAsHtmlActionPerformed(evt));

        menuFile.add(mitSaveAsHtml);

        mitPrint.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setText(GUI.getString("Menu.Print"));
        mitPrint.setToolTipText(GUI.getString("Menu.Print.Tooltip"));
        mitPrint.addActionListener(evt -> PrintActionPerformed(evt));

        menuFile.add(mitPrint);

        mitConfig.setText(GUI.getString("Menu.Config"));
        mitConfig.setToolTipText(GUI.getString("Menu.Config.Tooltip"));
        mitConfig.addActionListener(evt -> ConfigActionPerformed(evt));

        menuFile.add(mitConfig);

        mitExit.setText(GUI.getString("Menu.Close"));
        mitExit.setToolTipText(GUI.getString("Menu.Close.Tooltip"));
        mitExit.addActionListener(evt -> ExitActionPerformed(evt));

        menuFile.add(mitExit);

        mainMenu.add(menuFile);

        editMenu.setText(GUI.getString("Menu.Edit"));

        mitCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mitCopy.setText(GUI.getString("Menu.Copy"));
        mitCopy.setToolTipText(GUI.getString("Menu.Copy.Tooltip"));
        mitCopy.addActionListener(evt -> CopyActionPerformed(evt));

        editMenu.add(mitCopy);

        mitCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        mitCut.setText(GUI.getString("Menu.Cut"));
        mitCut.setToolTipText(GUI.getString("Menu.Cut.Tooltip"));
        mitCut.addActionListener(evt -> CutActionPerformed(evt));

        editMenu.add(mitCut);

        mitPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        mitPaste.setText(GUI.getString("Menu.Paste"));
        mitPaste.setToolTipText(GUI.getString("Menu.Paste.Tooltip"));
        mitPaste.addActionListener(evt -> PasteActionPerformed(evt));

        editMenu.add(mitPaste);

        mitUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        mitUndo.setText(GUI.getString("Menu.Undo"));
        mitUndo.setToolTipText(GUI.getString("Menu.Undo.Tooltip"));
        mitUndo.addActionListener(evt -> UndoActionPerformed(evt));

        editMenu.add(mitUndo);

        mitRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mitRedo.setText(GUI.getString("Menu.Redo"));
        mitRedo.setToolTipText(GUI.getString("Menu.Redo.Tooltip"));
        mitRedo.addActionListener(evt -> RedoActionPerformed(evt));

        editMenu.add(mitRedo);

        mitPretty.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        mitPretty.setText(GUI.getString("Menu.Pretty"));
        mitPretty.setToolTipText(GUI.getString("Menu.Pretty.Tooltip"));
        mitPretty.addActionListener(evt -> PrettyActionPerformed(evt));
        
        editMenu.add(mitPretty);

        mainMenu.add(editMenu);

        diagramMenu.setText(GUI.getString("Menu.Visualization"));

        mitStructogram.setText(GUI.getString("Menu.Structogram"));
        mitStructogram.setToolTipText(GUI.getString("Menu.Structogram.Tooltip"));
        mitStructogram.addActionListener(evt -> StructogramActionPerformed(evt));
        diagramMenu.add(mitStructogram);

        mitFlowChart.setText(GUI.getString("Menu.Flowchart"));
        mitFlowChart.setToolTipText(GUI.getString("Menu.Flowchart.Tooltip"));
        mitFlowChart.addActionListener(evt -> FlowChartActionPerformed(evt));
        diagramMenu.add(mitFlowChart);

        mitObjectChart.setText(GUI.getString("Menu.Objectchart"));
        mitObjectChart.setToolTipText(GUI.getString("Menu.Objectchart.Tooltip"));
        mitObjectChart.addActionListener(evt -> ObjectChartActionPerformed(evt));        
        diagramMenu.add(mitObjectChart);

        mitClassChart.setText(GUI.getString("Menu.Classchart"));
        mitClassChart.setToolTipText(GUI.getString("Menu.Classchart.Tooltip"));
        mitClassChart.addActionListener(evt -> ClassChartActionPerformed(evt));        
        diagramMenu.add(mitClassChart);
        
        mainMenu.add(diagramMenu);
        
        LanguageManager.getInstance().addPluginMenues(mainMenu);
        
        helpMenu.setText(GUI.getString("Menu.Help"));

        mitClassDoc.setText(GUI.getString("Menu.Classbook"));
        mitClassDoc.setToolTipText(GUI.getString("Menu.Classbook.Tooltip"));
        mitClassDoc.addActionListener(evt -> ClassDocActionPerformed(evt));

        helpMenu.add(mitClassDoc);

        mitHelp.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mitHelp.setText(GUI.getString("Menu.Handbook"));
        mitHelp.setToolTipText(GUI.getString("Menu.Handbook.Tooltip"));
        mitHelp.addActionListener(evt -> HelpActionPerformed(evt));

        helpMenu.add(mitHelp);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbarGroup, BorderLayout.NORTH);
        getContentPane().add(mainSplit, BorderLayout.CENTER);
        mainSplit.setPreferredSize(new Dimension(500, 600));
        pack();
    }

    /**
     * Programm starten.
     * @param evt
     */
    private void StartActionPerformed(java.awt.event.ActionEvent evt) {
        data.run();
    }
    /**
     * Speichern.
     * @param evt
     */
    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {
        save();
    }
    /**
     * Speichern
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
            JOptionPane.showMessageDialog(this, GUI.getString("Save.Error.Title"),
                    io.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
    /**
     * Unter neuem Ort speichern.
     * @return erfolgreich?
     */
    private boolean saveAs() {
        try {
            if (filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            	File target = filechooser.getSelectedFile();
            	if (!target.getName().contains(".")) {
            		target = new File(target.toString()+".eos");
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
            JOptionPane.showMessageDialog(this, GUI.getString("Save.Error.Title"),
                    io.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    /**
     * Datei öffnen.
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
            JOptionPane.showMessageDialog(this, GUI.getString("Open.Error.Title"),
                    io.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    /**
     * Externes Laden.
     * @param file Dateiname
     * @throws IOException
     */
    public void load(File file) {

        try {
        	filechooser.setSelectedFile(file);        	
			data.loadProgram(file);
			activeFile = file;
			ResourceLoader.setWorkingDirectory(file.getParent());
		} catch (IOException io) {
            JOptionPane.showMessageDialog(this, GUI.getString("Open.Error.Title"),
                    io.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
		}            	
    }

    /**
     * Undo.
     * @param evt
     */
    private void UndoActionPerformed(java.awt.event.ActionEvent evt) {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }
    /**
     * Redo.
     * @param evt
     */
    private void RedoActionPerformed(java.awt.event.ActionEvent evt) {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
    /**
     * Neu Formatieren.
     * @param evt
     */
    private void PrettyActionPerformed(java.awt.event.ActionEvent evt) {
        data.prettyPrint();
    }    
    /**
     * Kopieren.
     * @param evt
     */
    private void CopyActionPerformed(java.awt.event.ActionEvent evt) {
        txtProgram.copy();
    }
    /**
     * Ausschneiden.
     * @param evt
     */
    private void CutActionPerformed(java.awt.event.ActionEvent evt) {
        txtProgram.cut();
    }
    /**
     * Einfügen.
     * @param evt
     */
    private void PasteActionPerformed(java.awt.event.ActionEvent evt) {
        txtProgram.paste();
    }
    /**
     * Neues Programm.
     * @param evt
     */
    private void NewActionPerformed(java.awt.event.ActionEvent evt) {
    	if (dirtySafetyCheck()) {
            data.clear();
            activeFile = null;
            undoManager.discardAllEdits();
    	}
    }
    /**
     * Hilfe anzeigen.
     * @param evt
     */
    private void HelpActionPerformed(java.awt.event.ActionEvent evt) {
        Help.showHelp();
    }
    /**
     * Klassenhilfe anzeigen.
     * @param evt
     */
    private void ClassDocActionPerformed(java.awt.event.ActionEvent evt) {
       ClassDoc.showDoc();
    }    
    /**
     * Einzelschritt.
     * @param evt
     */
    private void SingleStepActionPerformed(java.awt.event.ActionEvent evt) {
        data.singleStep();
    }
    /**
     * Programmausführung unterbrechen.
     * @param evt
     */
    private void PauseActionPerformed(java.awt.event.ActionEvent evt) {
        data.pause();
    }
    /**
     * Programm anhalten.
     * @param evt
     */
    private void StopActionPerformed(java.awt.event.ActionEvent evt) {
        data.stop();
    }
    /**
     * Programm mit maximaler Geschwindigkeit ausführen.
     * @param evt
     */
    private void SkipActionPerformed(java.awt.event.ActionEvent evt) {
        data.skip();
    }
    /**
     * Geschwindigkeit ändern.
     * @param evt
     */
    private void sliderSpeedStateChanged(ChangeEvent evt) {
        data.setSpeed(sliderSpeed.getValue());
        sliderSpeed.setToolTipText(MessageFormat.format(GUI.getString("Speed.Tooltip"), data.getSpeed()));
    }
    /**
     * Unter neuem Ort speichern.
     * @param evt
     */
    private void SaveAsActionPerformed(java.awt.event.ActionEvent evt) {
        saveAs();
    }
    /**
     * Haltepunkt setzen.
     * @param evt
     */
    private void BreakpointActionPerformed(java.awt.event.ActionEvent evt) {
        data.setToggleBreakpoint(txtProgram.getCaretPosition());
        sideInformation.repaint();
    }
    /**
     * Druckvorschau anzeigen.
     * @param evt
     */
    private void PrintActionPerformed(java.awt.event.ActionEvent evt) {    	
   		PrintFrame pf = new PrintFrame((activeFile != null)?activeFile.getName():GUI.getString("Print.Noname"));
       	pf.init(data.getProgram());
       	pf.setVisible(true);
    }
    /**
     * Konfigurationsfenster anzeigen.
     * @param evt
     */
    private void ConfigActionPerformed(java.awt.event.ActionEvent evt) {
        configFrame.setVisible(true);
    }
    /**
     * Programm beenden.
     * @param evt
     */
    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {
        closeApp();
    }
    /**
     * Programm als HTML exportieren.
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
                JOptionPane.showMessageDialog(this, GUI.getString("Export.Error.Title"),
                        ioe.getLocalizedMessage(),
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    /**
     * Struktogramm ansicht anzeigen.
     * @param evt
     */
    private void StructogramActionPerformed(java.awt.event.ActionEvent evt) {
        new DiagramFrame(new Structogram(), compiler).setVisible(true);
    }
    /**
     * Kontrollflussansicht anzeigen.
     * @param evt
     */
    private void FlowChartActionPerformed(java.awt.event.ActionEvent evt) {
        new DiagramFrame(new FlowChart(), compiler).setVisible(true);
    }
    /**
     * Objectdiagramm anzeigen.
     * @param evt
     */
    private void ObjectChartActionPerformed(java.awt.event.ActionEvent evt) {
        new DiagramFrame(new ObjectChart(), compiler).setVisible(true);
    }
    /**
     * Klassendiagramm anzeigen.
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
        int answer = JOptionPane.showConfirmDialog(
                this, GUI.getString("Save.Overwrite.Text"), GUI.getString("Save.Overwrite.Title"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return answer == JOptionPane.OK_OPTION;
    }
    private boolean dirtySafetyCheck() {
        if (data.isSourceDirty()) {
            int answer = JOptionPane.showConfirmDialog(
                    this, GUI.getString("Closing.Save.Text"), GUI.getString("Closing.Save.Title"),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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
//Window Listener
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
	 * Diese Klasse dient als Workarround.
	 * Um den automatischen Umbruch des JTextPane innerhalb von JScrollPane
	 * zu unterbinden.
	 * Die JRE aktiviert automatisch einen Zeilenumbruch,
	 * wenn der Container ein Viewport ist.
	 * Diese Klasse schaltet sich zwischen den JTextPane und den Viewport 
	 * und stört damit diese Erkennung.
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
}
