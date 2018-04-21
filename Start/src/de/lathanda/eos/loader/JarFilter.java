package de.lathanda.eos.loader;

import java.io.File;
import java.io.FileFilter;

public class JarFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		return file.isFile() && file.getName().endsWith(".jar");
	}

}
