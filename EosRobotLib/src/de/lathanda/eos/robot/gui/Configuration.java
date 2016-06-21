package de.lathanda.eos.robot.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Properties;

import de.lathanda.eos.base.ResourceLoader;

/**
 * Diese Klasse verwaltet alle Benutzereinstellungen.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9
 */
public class Configuration {

	public static final Configuration def = new Configuration();
	
	private Properties configuration; 
	private boolean dirty;
	
	private int fallheight;
	private int jumpheight;

	private Configuration() {
		try {
			load();
		} catch (Exception e) {
			setDefault();
		}
	}
	private void load() throws IOException {
		String home = System.getProperty("user.home");
		File file = new File(home + "/.eos/config.ini");
		if (file.exists()) {
			try (InputStream in = new FileInputStream(file)) {
				load(in);
			}
		} else {
			try (InputStream in = ResourceLoader.getResourceAsStream("config.ini")) {
				load(in);
			}
		}
	}

	public void save() throws IOException {
		String home = System.getProperty("user.home");
		File dir = new File(home + "/.eos");
		File file = new File(home + "/.eos/config.ini");
		if (!(dir.exists() && dir.isDirectory())) {
			dir.mkdir();
		}
		save(new FileOutputStream(file));
	}
	
	private void load(InputStream in) throws IOException {
		configuration = new Properties();
		configuration.load(in);
		fallheight = Integer.valueOf(configuration.getProperty("fallheight"));
		jumpheight = Integer.valueOf(configuration.getProperty("jumpheight"));
		dirty = false;
	}
	
	private void save(OutputStream out) throws IOException {
		configuration.setProperty("fallheight", Integer.toString(fallheight));
		configuration.setProperty("jumpheight", Integer.toString(jumpheight));
		configuration.store(out, "EOS Configuration");
		dirty = false;
	}
	public void cleanup() throws Throwable {
		if (dirty) {
			save();
		}
	}
	private void setDefault() {
		fallheight = 1;
		jumpheight = 1;
		dirty = false;
	}
	public int getFallheight() {
		return fallheight;
	}
	public void setFallheight(int fallheight) {
		if (this.fallheight != fallheight) {
			this.fallheight = fallheight;
			fireRobotConfigurationChanged();
		}
	}
	public int getJumpheight() {
		return jumpheight;
	}
	public void setJumpheight(int jumpheight) {
		if (this.jumpheight != jumpheight) {		
			this.jumpheight = jumpheight;
			fireRobotConfigurationChanged();
		}
	}

	private LinkedList<ConfigurationListener> configurationListener = new LinkedList<>();
	public synchronized void addConfigurationListener(ConfigurationListener cf) {
		configurationListener.add(cf);
	}
	public synchronized void removeConfigurationListener(ConfigurationListener cf) {
		configurationListener.remove(cf);
	}
	public synchronized void fireRobotConfigurationChanged() {
		dirty = true;
		configurationListener.forEach(cf -> cf.robotConfigurationChanged(fallheight, jumpheight));
	}	

	public interface ConfigurationListener {
		public default void robotConfigurationChanged(int fallheight, int jumpheight) {}		
	}
}
