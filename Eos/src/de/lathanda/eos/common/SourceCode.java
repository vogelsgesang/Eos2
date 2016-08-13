package de.lathanda.eos.common;

import static de.lathanda.eos.common.GuiConstants.GUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;

import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.common.GuiConfiguration.GuiConfigurationListener;
import de.lathanda.eos.common.MessageHandler.ErrorLevel;
import de.lathanda.eos.common.MessageHandler.LogListener;


/**
 * Dokumentenstil für die Verwaltung des Quellcodes.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.4
 */
public class SourceCode extends DefaultStyledDocument 
       implements Source, CompilerListener, DebugListener, 
           LogListener, GuiConfigurationListener, CleanupListener {
	private static final long serialVersionUID = -7902775704808861534L;

	private static final Object COMPILE_LOCK = new Object();
	private final OutputStyle message;
	private AbstractMachine machine;
	private boolean compileNeeded = false;
	private boolean sourceDirty = false;
	private String path = "";
	private int delay = 0;
	private AbstractProgram program;
	private AutoCompleteHook autoCompleteHook;
	private CodeColorHook codeColorHook;
	private TreeSet<Integer> breakpoints = new TreeSet<Integer>();
	private TreeSet<Integer> errors      = new TreeSet<Integer>();

	private SideInformation sideInformation;
	
	public SourceCode() {
		Stop.addCleanupListener(this);
		message = new OutputStyle();
		MessageHandler.def.addLogListener(this);
		GuiConfiguration.def.addConfigurationListener(this);
		
	}
	public void init(AutoCompleteHook autoCompleteHook, CodeColorHook codeColorHook) {
		this.codeColorHook = codeColorHook;
		this.autoCompleteHook = autoCompleteHook;
		codeColorHook.init(this);
		codeColorHook.setFontSize(GuiConfiguration.def.getFontsize());
	}
	public void loadProgram(File file) throws IOException {
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Utf-8"));
		StringBuilder src = new StringBuilder();
		while (br.ready()) {
			src.append(br.readLine());
			src.append("\n");
		}
		try {
			path = file.getParent();
			replace(0, getLength(), src.toString(), null);
		} catch (BadLocationException ble) {
			// This shouldn't be possible, but if it happens it's fatal
			JOptionPane.showMessageDialog(null, ble.getLocalizedMessage(), GUI.getString("InternalError.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
		br.close();
		sourceDirty = false;
	}

	public void saveProgram(File file) throws IOException {
		FileOutputStream save = null;
		try {
			String text = this.getText(0, getLength());
			save = new FileOutputStream(file);
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(save, "Utf-8"))) {
				bw.append(text);
			}
			path = file.getParent();
			sourceDirty = false;
		} catch (BadLocationException ble) {
			// This shouldn't be possible, but if it happens it's fatal
			JOptionPane.showMessageDialog(null, GUI.getString("InternalError.Title"), ble.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
		} finally {
			if (save != null) {
				save.close();
			}
		}
	}

	public AbstractProgram getProgram() {
		return program;
	}

	public boolean isSourceDirty() {
		return sourceDirty;
	}

	public DefaultStyledDocument getOutput() {
		return message;
	}

	public void run() {
		if (machine != null) {
			machine.run();
		}
	}

	public void clear() {
		try {
			remove(0, getLength());
		} catch (BadLocationException ble) {
			// This shouldn't be possible, but if it happens it's fatal
			JOptionPane.showMessageDialog(null, ble.getLocalizedMessage(), GUI.getString("InternalError.Title"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	private void handleException(Exception e) {
		Marker codeRange = machine.getDebugInfo().getCodeRange();
		errors.add(codeRange.getBeginLine());
		message.println(
				Messages.formatError("Error.Report",
				((codeRange != null) ? codeRange.getBeginLine() : "?"), 
				e.getLocalizedMessage())
		);
	}

	public void singleStep() {
		if (machine != null) {
			machine.singleStep();
		}
	}

	public void pause() {
		machine.pause();
	}

	public void stop() {
		if (machine != null) {
			machine.stop();
		}
		codeColorHook.unmarkExecutionPoint();
	}

	public void skip() {
		if (machine != null) {
			machine.skip();
		}
	}

	public void setSpeed(int value) {
		double stepsPerSecond = Math.pow(1.096478196, value);
		delay = (int) (1000000000 / stepsPerSecond);
		// 0 = 1 per sec
		// 100 = 10000 per sec
		if (machine != null) {
			machine.setDelay(delay);
		}
	}

	public int getSpeed() {
		return 1000000000 / delay;
	}

	@Override
	public String getSourceCode() {
		synchronized (COMPILE_LOCK) {
			try {
				while (!compileNeeded) {
					COMPILE_LOCK.wait();
				}
				String text = getText(0, getLength());
				compileNeeded = false;
				return text;
			} catch (InterruptedException | BadLocationException ex) {
				handleException(ex);
				return "";
			}
		}
	}

	@Override
	public void insertString(int pos, String text, AttributeSet attributeSet) throws BadLocationException {
		autoCompleteHook.insertString(pos, text, program);
		super.insertString(pos, text, attributeSet);
		changed();
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		changed();
	}
	public void changed() {
		synchronized (COMPILE_LOCK) {
			compileNeeded = true;
			sourceDirty = true;
			COMPILE_LOCK.notifyAll();
		}		
	}
	@Override
	public void compileComplete(LinkedList<ErrorInformation> errors, AbstractProgram program) {
		stop();
		this.program = program;
		this.machine = program.getMachine();
		machine.removeDebugListener(this);
		machine.setDelay(delay);
		machine.addDebugListener(this);
		this.errors.clear();
		codeColorHook.doColoring();
		for (Integer linenumber : breakpoints) {
			machine.setBreakpoint(linenumber, true);
		}
		message.clear();
		if (errors.size() > 0) {
			errors.forEach(err -> message.println(err.getMessage()));
			for (ErrorInformation err : errors) {
				if (err.getCode() != null) {
					this.errors.add(err.getCode().getBeginLine());
					codeColorHook.markError(err.getCode());
				}
			}
		} else {
			message.println(Messages.getError("Source.Ok"));
		}
		sideInformation.repaint();
	}

	@Override
	public void debugPointReached(DebugInfo debugInfo) {
		codeColorHook.markExecutionPoint(debugInfo.getCodeRange());
	}

	public class OutputStyle extends DefaultStyledDocument {
		private static final long serialVersionUID = -8345159320105291891L;

		public void println(String line) {
			try {
				insertString(getLength(), line + "\n", null);
			} catch (BadLocationException e) {
				// This should not be possible?
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), GUI.getString("InternalError.Title"),
						JOptionPane.ERROR_MESSAGE);
			}
		}

		public void clear() {
			try {
				remove(0, getLength());
			} catch (BadLocationException e) {
				// This should not be possible?
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), GUI.getString("InternalError.Title"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public boolean hasBreakpoint(int linenumber) {
		return breakpoints.contains(linenumber);
	}
	public boolean hasError(int linenumber) {
		return errors.contains(linenumber);
	}
	public void setToggleBreakpoint(int position) {
		Element root = getDefaultRootElement();
		int linenumber = root.getElementIndex(position) + 1;
		if (breakpoints.contains(linenumber)) {
			breakpoints.remove(linenumber);
			if (machine != null) {
				machine.setBreakpoint(linenumber, false);
			}
		} else {
			breakpoints.add(linenumber);
			if (machine != null) {
				machine.setBreakpoint(linenumber, true);
			}
		}
	}

	@Override
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void prettyPrint() {
		try {
			this.replace(0, getLength(), this.program.prettyPrint(), null);
		} catch (BadLocationException e) {
			// shouldn't happen
		}
	}

	public void setSideInformation(SideInformation sideInformation) {
		this.sideInformation = sideInformation;
	}

	@Override
	public void message(String msg, ErrorLevel level) {
		switch (level) {
		case INFORMATION:
			message.println(GUI.getString("Message.Information")+" "+msg);
			break;
		case WARNING:
			message.println(GUI.getString("Message.Warning")+" "+msg);
			break;
		case ERROR:
			message.println(GUI.getString("Message.Error")+" "+msg);
			break;
		case FATAL:
			message.println(GUI.getString("Message.Fatal")+" "+msg);
			break;
		}
		
	}

	@Override
	public void fontsizeChanged(int fontsize) {
		codeColorHook.setFontSize(fontsize);		
		codeColorHook.doColoring();		
	}

	@Override
	public void terminate() {		
		if (machine != null) {
			//machine.stop();
			machine = null;
		}
	}	
}
