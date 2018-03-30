package de.lathanda.eos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.gui.MainWindow;
import de.lathanda.eos.interpreter.parsetree.Program;
import de.lathanda.eos.spi.LanguageManager;

/**
 * \brief Startklasse
 * 
 * Wertet die Befehlszeilen Argumente aus und startet die Anwendung.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Start {

	/**
	 * /p Dateiname - Programm ohne grafische Oberfläche starten.
	 * Dateiname - Grafische Oberfläche starten wobei die Datei bereits geladen wurde.
	 * ohne - Grafische Oberfläche starten.
	 *  
	 * @param args Befehlszeilenargumente
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String args[]) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		        }
		    }
		} catch (Throwable t) {
		}
	
		Runtime.getRuntime().addShutdownHook(new Stop());		
		apply(args);
		try {
			LanguageManager.prepare();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Messages.getString("Export.Error.Title"), e.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}		switch (mode) {
		case EDITOR:
			editorStart();
			break;
		case PROGRAM:
			programStart();
			break;
		default:
			editorStart();
		}
	}

	/**
	 * Agrgument auswerten. /p bedeutet Programmmodus. Alles andere ist der Dateiname.
	 * @param arg
	 */
	private static void apply(String[] args) {
		for (int an = 0; an < args.length; an++) {
			System.out.println(args[an]);
			if (args[an].startsWith("/") || args[an].startsWith("-")) {
				for (int i = 1; i < args[an].length(); i++) {
					switch (args[an].charAt(i)) {
					case 'p': //directly start the program, no editor
						mode = Mode.PROGRAM;
						break;
					case 'm': //only methods are allowed
						allowProperties = false;
						break;
					case 'e':
						Locale.setDefault(Locale.ENGLISH);
						break;
					case 'd':
						Locale.setDefault(Locale.GERMAN);
						break;
					}
				}
			} else {
				file = new File(args[an]);
			}
		}

	}

	/**
	 * Modus
	 * @author schneidp
	 *
	 */
	private enum Mode {
		/**
		 * Editor
		 */
		EDITOR,
		/**
		 * Demo/Programm ohne Programmiermöglichkeiten.
		 */
		PROGRAM
	};

	/**
	 * Die beim start zu ladende Datei.
	 */
	private static File file = null;
	/**
	 * Modus
	 */
	private static Mode mode = Mode.EDITOR;
	/**
	 * Atribute erlaubt?
	 */
	private static boolean allowProperties = true;

	/**
	 * Startet Editor.
	 */
	private static void editorStart() {
		if (!allowProperties) {
			LanguageManager.getInstance().lockProperties();
		}
		MainWindow mainWindow = new MainWindow();
		if (file != null) {
			mainWindow.load(file);
		}
		mainWindow.setVisible(true);
		mainWindow.startCompiler();
	}

	/**
	 * Startet Demomodus.
	 */
	private static void programStart() {
		if (file == null) {
			editorStart();
			return;
		}
		try {
			StringBuilder src = new StringBuilder();
			String path = file.getParent();

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Utf-8"));
			while (br.ready()) {
				src.append(br.readLine());
				src.append("\n");
			}
			br.close();

			AbstractProgram program = new Program(src.toString());
			program.parse(path);
			program.compile();
			program.getMachine().skip();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, Messages.getString("Export.Error.Title"), e.getLocalizedMessage(),
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
