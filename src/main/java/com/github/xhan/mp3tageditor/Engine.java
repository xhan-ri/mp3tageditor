package com.github.xhan.mp3tageditor;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.github.xhan.mp3tageditor.os.filesystem.IFileSystem;
@Component
public class Engine {

	private Logger logger = Logger.getLogger(Engine.class);
	private List<File> fileList;
	
	@Resource(name="fileSystem") private IFileSystem fileSystem;
	protected Engine() {
//		fileSystem = (IFileSystem)springContext.getBean("fileSystem");
		fileList = new LinkedList<>();
	}
	
	public List<File> addFiles(File... files) {
		fileList.addAll(Arrays.asList(files));
		normalizeFiles();
		return fileList;
	}
	
	public void test() {
		File[] fileArr = fileSystem.getFiles();
		for (File file : fileArr) {
			logger.info(file.getAbsolutePath());
		}
	}

	private void normalizeFiles() {
		List<File> badFiles = new LinkedList<>();
		for (File file : fileList) {
			try {
				normalizeFile(file);
			} catch (Exception e) {
				logger.error(e);
				badFiles.add(file);
			}
		}
		
		// clear out bad files.
		for (File file : badFiles) {
			fileList.remove(file);
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
