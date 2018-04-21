package de.lathanda.eos.loader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Start {

	public static void main(String[] args) {
		ArrayList<URL> urls = new ArrayList<>();		
		try {
			JarFilter filefilter = new JarFilter();
			File base = new File(".");
			File[] bins = base.listFiles(filefilter);
			for(File f:bins) {
				urls.add(f.toURI().toURL());
			}
			LinkedList<File> scan = new LinkedList<>();
			scan.add(new File("./ext"));
			while (!scan.isEmpty()) {
				File f = scan.poll();
				if (f.exists()) {
					for(File file:f.listFiles()) {
						if (file.isDirectory()) {
							scan.add(file);
						} else {
							if (filefilter.accept(file)) {
								urls.add(file.toURI().toURL());
							}
						}
					}
				}
			}
			try (URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]))) {
				loader.loadClass("de.lathanda.eos.Start").getMethod("main", String[].class).invoke(null, new Object[] {args});
			} 
		} catch (Exception e) {
			System.err.println("probably a file is missing or corrupted");
			System.err.println("found:");
			System.err.println(urls);
			e.printStackTrace();			
		}
	}

}
