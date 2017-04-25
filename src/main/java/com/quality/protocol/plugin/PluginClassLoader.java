package com.quality.protocol.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginClassLoader extends ClassLoader {

	private static PluginClassLoader instance = new PluginClassLoader();

	private String path = "plugins";

	public PluginClassLoader() {}

	public static PluginClassLoader getInstance() {
		return instance;
	}

	public URL getResource(String name) {
		return ClassLoader.getSystemResource(name);
	}

	public InputStream getResourceAsStream(String name) {
		return ClassLoader.getSystemResourceAsStream(name);
	}

	public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> c = findLoadedClass(name);
		if (c != null)
			return c;

		if (c == null) {
			byte[] data = lookupClassData(name);
			if (data == null) {
				c = super.loadClass(name, resolve);
			} else {
				c = defineClass(name, data, 0, data.length);
			}
		}
		if (resolve)
			resolveClass(c);
		return c;
	}

	private byte[] lookupClassData(String className) throws ClassNotFoundException {
		byte[] data = null;

		String fileName = className.replace('.', '/') + ".class";
		if (isJar(path)) {
			data = loadJarData(path, fileName);
		} else {
			data = loadFileData(path, fileName);
		}
		return data;
	}

	boolean isJar(String pathEntry) {
		return pathEntry.endsWith(".jar") || pathEntry.endsWith(".zip");
	}

	private byte[] loadFileData(String path, String fileName) {
		File file = new File(path, fileName);
		if (file.exists()) {
			return getClassData(file);
		}
		return null;
	}

	private byte[] getClassData(File f) {
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();

		} catch (IOException e) {
		}
		return null;
	}

	@SuppressWarnings("resource")
	private byte[] loadJarData(String path, String fileName) {
		ZipFile zipFile = null;
		InputStream stream = null;
		File archive = new File(path);
		if (!archive.exists())
			return null;
		try {
			zipFile = new ZipFile(archive);
		} catch (IOException io) {
			return null;
		}
		ZipEntry entry = zipFile.getEntry(fileName);
		if (entry == null)
			return null;
		int size = (int) entry.getSize();
		try {
			stream = zipFile.getInputStream(entry);
			byte[] data = new byte[size];
			int pos = 0;
			while (pos < size) {
				int n = stream.read(data, pos, data.length - pos);
				pos += n;
			}
			zipFile.close();
			return data;
		} catch (IOException e) {
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}