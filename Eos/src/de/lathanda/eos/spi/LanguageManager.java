package de.lathanda.eos.spi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.gui.objectchart.TextValue;
import de.lathanda.eos.gui.objectchart.Unit;
import de.lathanda.eos.gui.objectchart.UnitSource;
import de.lathanda.eos.interpreter.MethodType;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.interpreter.Type.ObjectSource;

/**
 * \brief Sprach Konfiguration
 * 
 * Singleton zur Verwaltung aller Spracherweiterungen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.7
 */
public class LanguageManager {
	private static LanguageManager lm = null;
	/**
	 * Templates
	 */
	private static final TreeSet<AutoCompleteEntry> templates = new TreeSet<>();
	/**
	 * Labels
	 */
	private static final TreeMap<String, String> labels = new TreeMap<>();
	/**
	 * Attributnamen
	 */
	private static final TreeMap<String, String> names = new TreeMap<>();
	/**
	 * Objektdiagramm Attributdarsteller
	 */
	private static final TreeMap<String, UnitSource> units = new TreeMap<>();
	/**
	 * Plugin Menues
	 */
	private static final LinkedList<JMenu> menues = new LinkedList<>();
	private static String helpBase = "";
	private static String helpClasses = "";
	private static String helpInfo = "";

	private LanguageManager() {
	}

	private void init() throws Exception {
		// load eos base language, (remark: we can't use service loader as we
		// need to guarantee base language is loaded first)
		new EosLanguage().registerLanguage(this);
		// load extension
		ServiceLoader<Language> loader = ServiceLoader.load(Language.class);
		try {
			Iterator<Language> languages = loader.iterator();
			while (languages.hasNext()) {
				Language language = languages.next();
				language.registerLanguage(this);
			}
		} catch (ServiceConfigurationError serviceError) {
			serviceError.printStackTrace();
		}
	}

	public static void prepare() throws Exception {
		if (lm == null) {
			lm = new LanguageManager();
			lm.init();
		}
	}

	public static LanguageManager getInstance() {
		try {
			prepare();
		} catch (Exception e) {
			return null;
		}
		return lm;
	}

	public void registerMenu(JMenu mi) {
		menues.add(mi);
	}

	public void addPluginMenues(JMenuBar mainMenu) {
		for (JMenu mi : menues) {
			mainMenu.add(mi);
		}
	}

	public InputStream getHelpXml() {
		String help = helpBase;
		help = help.replaceAll("<classes>", helpClasses);
		help = help.replace("<info>", helpInfo);
		return new ByteArrayInputStream(help.getBytes(StandardCharsets.UTF_8));
	}

	public void addClasses(String filename) throws IOException {
		helpClasses = helpClasses + loadFile(filename);
	}

	public void addInfo(String filename) throws IOException {
		helpInfo = helpInfo + loadFile(filename);
	}

	public void setHelp(String filename) throws IOException {
		helpBase = loadFile(filename);
	}

	private static String loadFile(String filename) throws IOException {
		StringBuilder src = new StringBuilder();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(ResourceLoader.getResourceAsStream(filename), "Utf-8"))) {
			while (br.ready()) {
				src.append(br.readLine());
				src.append("\n");
			}
		}
		return src.toString();
	}

	public void registerLabels(ResourceBundle res) {
		for (String key : res.keySet()) {
			labels.put(key, res.getString(key));
		}
	}

	public boolean containsLabel(String id) {
		return labels.containsKey(id);
	}

	public String getLabel(String id) {
		return labels.get(id);
	}

	public void registerUnit(UnitSource us, String classname) {
		units.put(classname, us);
	}

	public Unit createUnit(Object value) {
		if (units.containsKey(value.getClass().getName())) {
			return units.get(value.getClass().getName()).createUnit(value);
		} else {
			return new TextValue(value.toString());
		}
	}

	public void registerNames(ResourceBundle res) {
		for (String key : res.keySet()) {
			names.put(key, res.getString(key));
		}
	}

	public boolean containsName(String id) {
		return names.containsKey(id);
	}

	public String getName(String id) {
		return names.get(id);
	}

	public void registerAutoComplete(Properties res) {
		for (String key : res.stringPropertyNames()) {
			AutoCompleteEntry ace;
			try {
				ace = new AutoCompleteEntry(key, res.getProperty(key));
				ace.cls.registerAutoCompleteEntry(ace);
			} catch (MissingTypeException e) {
				JOptionPane.showMessageDialog(null, "broken configuration\n" + key + " = " + res.getProperty(key),
						"fatal error", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
	}

	public void registerTemplates(Properties res) {
		for (String key : res.stringPropertyNames()) {
			templates.add(new AutoCompleteEntry(res.getProperty(key), key.charAt(0)));
		}
	}

	public Set<AutoCompleteEntry> getTemplates() {
		return templates;
	}

	public void registerAssigns(Properties res) {
		for (String key : res.stringPropertyNames()) {
			try {
				AssignSignature signature = new AssignSignature(key, res.getProperty(key));
				signature.target.registerAssignProperty(new MethodType(signature.target, signature.parameters,
						signature.ret, signature.name, signature.originalName));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"broken configuration\n" + key + " = " + res.getProperty(key) + "\n" + e.getMessage(),
						"fatal error", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
	}

	public void registerReads(Properties res) {
		for (String key : res.stringPropertyNames()) {
			try {
				ReadSignature signature = new ReadSignature(key, res.getProperty(key));
				signature.target.registerReadProperty(new MethodType(signature.target, signature.parameters,
						signature.ret, signature.name, signature.originalName));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"broken configuration\n" + key + " = " + res.getProperty(key) + "\n" + e.getMessage(),
						"fatal error", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
	}

	public void registerMethods(Properties map_method) {
		for (String key : map_method.stringPropertyNames()) {
			try {
				MethodSignature signature = new MethodSignature(key, map_method.getProperty(key));
				signature.target.registerMethod(new MethodType(signature.target, signature.parameters, signature.ret,
						signature.name, signature.originalName));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"broken configuration\n" + key + " = " + map_method.getProperty(key) + "\n" + e.getMessage(),
						"fatal error", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
	}

	public void registerFunctions(Class<?> target, Properties res) {
		for (String key : res.stringPropertyNames()) {
			try {
				FunctionSignature signature = new FunctionSignature(key, res.getProperty(key));
				MethodType.registerSystemFunction(target, signature.parameters, signature.ret, signature.name,
						signature.originalName);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"broken configuration\n" + key + " = " + res.getProperty(key) + "\n" + e.getMessage(),
						"fatal error", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
	};

	public void registerType(String id, String[] name, String description, ObjectSource objSrc, Class<?> cls) {
		Type.registerType(id, name, description, objSrc, cls);
	}

	public void registerInherits(String id, String[] inherits) {
		try {
			Type.registerInherits(id, inherits);
		} catch (MissingTypeException e) {
			JOptionPane.showMessageDialog(null,
					"broken configuration\n" + id + " inherits " + Arrays.asList(inherits).toString(), "fatal error",
					JOptionPane.OK_OPTION);
			System.exit(-1);
		}
	}

	private static class Signature {
		protected int args;
		protected Type[] parameters;
		protected Type ret;
		protected String name;

		protected void parseSignature(String value, int args) throws MissingTypeException {
			this.args = args;
			String[] signature = value.split(",");
			name = signature[0];
			parameters = new Type[args];
			for (int i = 0; i < args; i++) {
				parameters[i] = Type.getInstanceByID(signature[i + 1]);
			}
			if (args + 1 < signature.length) {
				ret = Type.getInstanceByID(signature[signature.length - 1]);
				if (ret == null) {
					throw new MissingTypeException(signature[signature.length - 1]);
				}
			} else {
				ret = Type.getVoid();
			}
		}
	}

	private static class FunctionSignature extends Signature {
		protected String originalName;

		protected FunctionSignature(String key, String value) throws MissingTypeException {
			super();
			int braket = key.indexOf("(");
			originalName = key.substring(0, braket);
			parseSignature(value, Integer.parseInt(key.substring(braket + 1, key.length() - 1)));
		}
	}

	protected static class MethodSignature extends Signature {
		protected String originalName;
		protected Type target;

		private MethodSignature(String key, String value) throws MissingTypeException {
			super();
			int braket = key.indexOf("(");
			int dot = key.indexOf(".");
			target = Type.getInstanceByID(key.substring(0, dot));
			originalName = key.substring(dot + 1, braket);
			parseSignature(value, Integer.parseInt(key.substring(braket + 1, key.length() - 1)));
		}
	}

	protected static class ReadSignature extends Signature {
		protected String originalName;
		protected Type target;

		private ReadSignature(String key, String value) throws MissingTypeException {
			super();
			int dot = key.indexOf(".");
			target = Type.getInstanceByID(key.substring(0, dot));
			originalName = key.substring(dot + 1, key.length());
			parseSignature(value, 0);
		}
	}

	protected static class AssignSignature extends Signature {
		protected String originalName;
		protected Type target;

		private AssignSignature(String key, String value) throws MissingTypeException {
			super();
			int dot = key.indexOf(".");
			target = Type.getInstanceByID(key.substring(0, dot));
			originalName = key.substring(dot + 1, key.length());
			parseSignature(value, 1);
		}
	}

	public void registerLanguageByConfig(ResourceBundle lc, LanguageProvider lp) throws IOException {
		// init properties
		Properties map_class = new Properties();
		Properties map_mem = new Properties();
		Properties map_method = new Properties();
		Properties map_function = new Properties();
		Properties map_assign = new Properties();
		Properties map_read = new Properties();
		Properties autocompletion = new Properties();
		Properties templates = new Properties();

		// load properties
		try (InputStream in = ResourceLoader.getResourceAsStream(lc.getString("map_class"))) {
			map_class.load(in);
		}
		try (InputStream in = ResourceLoader.getResourceAsStream(lc.getString("map_mem"))) {
			map_mem.load(in);
		}

		// create classes
		for (String id : map_class.stringPropertyNames()) {
			String[] cls = map_class.getProperty(id).split(",");
			String desc = map_mem.getProperty(id.toString());
			lm.registerType(id, cls, desc, lp.getObjectSource(id), lp.getClassById(id));
		}
		// init classes
		for (String id : map_class.stringPropertyNames()) {
			lm.registerInherits(id, lp.getInherits(id));
		}
		String name = "";
		name = lc.getString("map_method");
		if (name != null && !name.isEmpty()) {
			map_method.load(ResourceLoader.getResourceAsStream(name));
			lm.registerMethods(map_method);
		}
		name = lc.getString("map_function");
		if (name != null && !name.isEmpty()) {
			map_function.load(ResourceLoader.getResourceAsStream(name));
			lm.registerFunctions(lp.getFunctionTarget(), map_function);
		}
		name = lc.getString("map_assign");
		if (name != null && !name.isEmpty()) {
			map_assign.load(ResourceLoader.getResourceAsStream(name));
			lm.registerAssigns(map_assign);
		}
		name = lc.getString("map_read");
		if (name != null && !name.isEmpty()) {
			map_read.load(ResourceLoader.getResourceAsStream(name));
			lm.registerReads(map_read);
		}
		name = lc.getString("autocompletion");
		if (name != null && !name.isEmpty()) {
			autocompletion.load(ResourceLoader.getResourceAsStream(name));
			lm.registerAutoComplete(autocompletion);
		}
		name = lc.getString("templates");
		if (name != null && !name.isEmpty()) {
			templates.load(ResourceLoader.getResourceAsStream(name));
			lm.registerTemplates(templates);
		}
	}

}
