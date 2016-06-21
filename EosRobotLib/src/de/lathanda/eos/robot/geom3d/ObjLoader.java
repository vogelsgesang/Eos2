package de.lathanda.eos.robot.geom3d;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.zip.ZipInputStream;

import de.lathanda.eos.base.ResourceLoader;

/**
 * Diese Ultiliyklasse dient dazu Teile von Wavefront Obj Dateien zu laden.
 * Der Funktionumfang entspricht in etwa dem Export durch Blender.
 * 
 * Alle Faces müssen die Formatierung v/vt/vn haben ohne relative Indizierung.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public class ObjLoader {
	public static final Polyhedron loadObj(String path, String filename) throws IOException {
		BufferedReader data = openStream(path + filename);
		return readPolyhedron(data, path, 100);
	}

	public static final Polyhedron loadObj(String path, String filename, int hint) throws IOException {
		BufferedReader data = openStream(path + filename);
		return readPolyhedron(data, path, hint);
	}

	private static final Polyhedron readPolyhedron(BufferedReader data, String path, int hint) throws IOException {
		final Polyhedron ph = new Polyhedron();
		final Hashtable<String, Material> matIndex = new Hashtable<>();
		final ArrayList<Vertice> v = new ArrayList<>(hint);
		final ArrayList<VerticeTexture> vt = new ArrayList<>(hint);
		final ArrayList<VerticeNormal> vn = new ArrayList<>(hint);
		String line;
		Material mat = null;
		while ((line = data.readLine()) != null) {
			if (line.startsWith("#")) {
				// comment
			} else if (line.equals("")) {
				// empty line
			} else if (line.startsWith("v ")) { // read in vertex data
				v.add(new Vertice(readFloat(line, 2)));
			} else if (line.startsWith("vt ")) { // read texture coordinates
				vt.add(new VerticeTexture(readFloat(line, 3)));
			} else if (line.startsWith("vn ")) { // read normal coordinates
				vn.add(new VerticeNormal(readFloat(line, 3)));
			} else if (line.startsWith("f ")) { // read face data
				ph.faces.add(readFace(line.substring(2), ph, mat, v, vt, vn));
			} else if (line.startsWith("mtllib ")) { // load material file
				loadMtl(path, line.substring(7), ph, matIndex);
			} else if (line.startsWith("usemtl ")) { // use material
				mat = matIndex.get(line.substring(7).trim());
			} else {
				// one of many tags we do not process
				// o new object
				// g new group
				// s smooth shading
				// ...
			}
		}
		return ph;
	}

	private static final void loadMtl(String path, String filename, Polyhedron ph, Hashtable<String, Material> matIndex)
			throws IOException {
		try (BufferedReader data = openStream(path + filename)) {
			String line;
			Material mat = new Material();
			while ((line = data.readLine()) != null) {
				if (line.startsWith("#")) {
					// comment
				} else if (line.equals("")) {
					// empty line
				} else if (line.startsWith("newmtl ")) {
					mat = new Material();
					matIndex.put(line.substring(7).trim(), mat);
				} else if (line.startsWith("map_Kd ")) {
					mat.setImage(ResourceLoader.loadImage(path + line.substring(6).trim()));
				} else if (line.startsWith("Ka ")) {
					// Ka spectral color
					mat.setKA(readFloat(line, 3));
				} else if (line.startsWith("Kd ")) {
					// Kd diffuse reflectivity
					mat.setKD(readFloat(line, 3));
				} else if (line.startsWith("Ks ")) {
					// Ks spectral reflectivity
					mat.setKS(readFloat(line, 3));
				} else {
					// one of many tags we do not process
					// Tf transmission filter
					// d dissolve
					// illum illumination model
					// 0. Color on and Ambient off
					// 1. Color on and Ambient on
					// 2. Highlight on
					// 3. Reflection on and Ray trace on
					// 4. Transparency: Glass on, Reflection: Ray trace on
					// 5. Reflection: Fresnel on and Ray trace on
					// 6. Transparency: Refraction on, Reflection: Fresnel off
					// and Ray trace on
					// 7. Transparency: Refraction on, Reflection: Fresnel on
					// and Ray trace on
					// 8. Reflection on and Ray trace off
					// 9. Transparency: Glass on, Reflection: Ray trace off
					// 10. Casts shadows onto invisible surfaces
					// Ns spectral exponent
					// Ni optical density
					// ...
				}
			}
		}
	}

	private static final float[] readFloat(String line, int from) {
		String[] values = line.substring(from).split(" ");
		float[] numbers = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			try {
				numbers[i] = Float.parseFloat(values[i]);
			} catch (Exception e) {
				System.out.println(line);
			}
		}
		return numbers;
	}

	private static final Face readFace(String values, Polyhedron ph, Material mat, ArrayList<Vertice> v,
			ArrayList<VerticeTexture> vt, ArrayList<VerticeNormal> vn) {
		String[] vertices = values.split(" ");
		Vertice[] fv = new Vertice[vertices.length];
		VerticeTexture[] fvt = new VerticeTexture[vertices.length];
		VerticeNormal[] fvn = new VerticeNormal[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			String[] vert = vertices[i].split("/");
			fv[i] = v.get(Integer.parseInt(vert[0]) - 1);
			if (!vert[1].isEmpty()) {
				fvt[i] = vt.get(Integer.parseInt(vert[1]) - 1);
			} else {
				fvt = null;
			}
			if (!vert[2].isEmpty()) {
				fvn[i] = vn.get(Integer.parseInt(vert[2]) - 1);
			} else {
				fvn = null;
			}
		}
		return new Face(fv, fvt, fvn, mat);
	}

	private static final BufferedReader openStream(String filename) throws IOException {
		BufferedReader br = null;
		if (filename.endsWith(".zip")) {
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(ResourceLoader.getResourceAsStream(filename)));
			zis.getNextEntry();
			br = new BufferedReader(new InputStreamReader(zis));
		} else {
			br = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsStream(filename)));
		}

		return br;
	}
}