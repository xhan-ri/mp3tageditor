package com.github.xhan.mp3tageditor;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class Engine {

	private Logger logger = Logger.getLogger(Engine.class);
	private List<File> files;
	public Engine() {
		files = new LinkedList<>();
	}
	
	public Engine(File...files) {
		this();
		this.files.addAll(Arrays.asList(files));
		normalizeFiles();
	}

	private void normalizeFiles() {
		List<File> badFiles = new LinkedList<>();
		for (File file : files) {
			try {
				normalizeFile(file);
			} catch (Exception e) {
				logger.error(e);
				badFiles.add(file);
			}
		}
		
		// clear out bad files.
		for (File file : badFiles) {
			files.remove(file);
		}
		
	}
	
	private void normalizeFile(File file) {
		if (file == null) {
			return;
		}
		if (!file.canRead()) {
			throw new RuntimeException("Cannot read file [" + file + "]");
		}
		
		if (!file.canWrite()) {
			throw new RuntimeException("Cannot write file [" + file + "]");
		}
	}
}
