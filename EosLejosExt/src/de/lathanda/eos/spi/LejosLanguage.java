package de.lathanda.eos.spi;

import java.io.IOException;
import java.util.ResourceBundle;

import de.lathanda.eos.ev3.Car;
import de.lathanda.eos.interpreter.parsetree.Type.ObjectSource;

/**
 * \brief Spracherweiterung Basissprache
 * 
 * Diese Erweiterung stellt alle Lejos EOS Klassen zur Verfügung.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.7
 */
public class LejosLanguage extends Language implements LanguageProvider {
	private static final ResourceBundle GUI = ResourceBundle.getBundle("lejos.gui");

	@Override
	public void registerLanguage(LanguageManager lm) throws IOException {
		//init help
		try {
			lm.addClasses(GUI.getString("Help.Classes"));
		} catch (IOException e) {
			//hmm no help, we probably will survive that			
		}
		try {
			lm.addInfo(GUI.getString("Help.Info"));
		} catch (IOException e) {
			//hmm no help, we probably will survive that			
		}		
		//init classes
		lm.registerLanguageByConfig(ResourceBundle.getBundle("lejos.lang"), this);   		
	}

	public ObjectSource getObjectSource(String id) {
		switch (id) {
		case "carev3":
			return () -> {
				return new Car();
			};

		default:
			return null;
		}
	}

	public String[] getInherits(String id) {
		switch (id) {
		case "carev3":
			return new String[]{"carev3", "string"};
		default:
			return new String[]{id};
		}
	}
	public Class<?> getClassById(String id) {
    	switch (id) {
        case "carev3":
            return Car.class;
        default:
          	return null;
    	}
	}

	@Override
	public Class<?> getFunctionTarget() {
		return null;
	}
}
