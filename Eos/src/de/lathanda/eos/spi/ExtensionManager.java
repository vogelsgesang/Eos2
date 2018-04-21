package de.lathanda.eos.spi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import de.lathanda.eos.common.gui.GuiConfiguration;
import de.lathanda.eos.common.gui.Messages;
import de.lathanda.eos.extension.BasicParser;
import de.lathanda.eos.extension.ClassDefinition;
import de.lathanda.eos.extension.Extension;
import de.lathanda.eos.extension.Command;
import de.lathanda.eos.extension.NameFilter;
import de.lathanda.eos.extension.ParserSource;
import de.lathanda.eos.interpreter.exceptions.MissingParserException;

public class ExtensionManager {
	static {
		try {
			ExtensionManager.prepare();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), Messages.getString("Message.Fatal"),
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}				
	}
	private static ExtensionManager em;
	private TreeMap<String, ParserSource> parserSourceMap = new TreeMap<>();
	private ParserSource defParserSource = null;
	private LanguageManager lm;
	public static void prepare() throws Exception {
		em = new ExtensionManager();
		em.init();
	}
	private ExtensionManager() {
		lm = new LanguageManager();
	}
	public static ExtensionManager get() {
		return em;
	}
	private void init() throws Exception {
		// load extension
		ArrayList<Extension> list = new ArrayList<>(); 
		LinkedList<NameFilter> filter = new LinkedList<>();
		StringBuilder macros = new StringBuilder();
		StringBuilder menues = new StringBuilder();
		TreeMap<String, Command> commands = new TreeMap<>();
		TreeMap<String, ClassDefinition> classDefinitions = new TreeMap<>();
		StringBuilder help = new StringBuilder();
		StringBuilder classDescriptions = new StringBuilder();
		try {
			ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class);
			Iterator<Extension> extensions = loader.iterator();
			while (extensions.hasNext()) {
				list.add(extensions.next());
			}
		} catch (ServiceConfigurationError serviceError) {
			serviceError.printStackTrace();
		}
		list.sort(new Extension.ExtensionComparator());
		for(Extension e : list) {
			//parser
			for(ParserSource ps : e.getParserSources()) {
				if (defParserSource == null) {
					defParserSource = ps;
				}
				parserSourceMap.put(ps.getID(), ps);
			}
			//filter
			NameFilter nf = e.getNameFilter();
			if (nf != null) {
				filter.add(nf);
			}
			//macros
			macros.append(e.getMacros());
			//menues
			menues.append(e.getMenues());
			//commands
			for (Command cmd : e.getCommands()) {
				commands.put(cmd.getID(), cmd);
			}
			//help
			help.append(e.getHelp());
			//classes
			classDescriptions.append(e.getClassDescriptions());
			//class definition
			for (ClassDefinition cd : e.getClassDefinitions()) {
				classDefinitions.put(cd.getID(), cd);
			}			
			//function target
			Class<?> functionTarget = e.getFunctionTarget();
		}
		
		if (parserSourceMap.isEmpty()) {
			throw new MissingParserException();
		}
	}

	public BasicParser createParser(String source) {
		ParserSource ps = parserSourceMap.get(GuiConfiguration.def.getParserSource());
		if (ps == null) {
			ps = defParserSource;
		}
		return ps.createParser(source);
	}
}
