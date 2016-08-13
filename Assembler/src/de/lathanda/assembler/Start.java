package de.lathanda.assembler;

import static de.lathanda.eos.common.GuiConstants.GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.lathanda.eos.common.AbstractProgram;
import de.lathanda.eos.common.Factory;
import de.lathanda.eos.common.Stop;
import de.lathanda.assembler.gui.MainWindow;
import de.lathanda.assembler.interpreter.Program;
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
	 */
    public static void main(String args[]) {
    	try {
    	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    	    // use default
    	}
    	Factory.setProgram(Program.class);
    	for(String arg:args) {
    		apply(arg);
    	}
    	switch (mode) {
    	case EDITOR:
    		editorStart();
    		break;
    	case PROGRAM:
    		programStart();
    		break;
    	default:
    		editorStart();
    	}
    	Runtime.getRuntime().addShutdownHook(new Stop());
    }
    /**
     * Agrgument auswerten. /p bedeutet Programmmodus. Alles andere ist der Dateiname.
     * @param arg
     */
    private static void apply(String arg) {    	
		if (arg.equals("/p")) {
			mode = Mode.PROGRAM;
		} else {
			file = new File(arg);
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
     * Startet Editor.
     */
    private static void editorStart() {
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
            
            AbstractProgram program =  Factory.createProgram(src.toString());
            program.parse(path);
			program.compile();
			program.getMachine().skip();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, GUI.getString("Export.Error.Title"),
                    e.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
        }        	
    }
}

