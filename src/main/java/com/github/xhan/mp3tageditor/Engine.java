package com.github.xhan.mp3tageditor;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.github.xhan.mp3tageditor.editor.IMusicEditor;
@Component
public class Engine {

	private Logger logger = Logger.getLogger(Engine.class);
	private Map<String, MusicFile> keyToFileMap;
	private Map<String, List<IMusicEditor>> keyToEditorsMap;
	private Charset sourceEncoding;
	
	protected Engine() {
		keyToFileMap = new HashMap<String, MusicFile>();
		keyToEditorsMap = new HashMap<String, List<IMusicEditor>>();
	}
	
	public void addFiles(File... files) {
		if (files != null) {
			for (File file : files) {
				addFileToEngine(file);
			}
		}
	}
	
	private void addFileToEngine(File file) {
		MusicFile musicFile = null;
		try {
			musicFile = new MusicFile(file);
			if (sourceEncoding != null) {
				musicFile.setSourceEncoding(sourceEncoding);
			}
		} catch (Exception e) {
			logger.error("Error to create music file [file=" + file.getAbsolutePath() + "]");
			return;
		}
		keyToFileMap.put(musicFile.getId().toString(), musicFile);
		keyToEditorsMap.put(musicFile.getId().toString(), new LinkedList<IMusicEditor>());
	}
	
	public void addGlobalEditor(IMusicEditor editor) {
		for (String key : keyToEditorsMap.keySet()) {
			List<IMusicEditor> editorList = keyToEditorsMap.get(key);
			editorList.add(editor);
		}
	}
	
	public void runEditors() {
		for (String key : keyToEditorsMap.keySet()) {
			List<IMusicEditor> editorList = keyToEditorsMap.get(key);
			MusicFile musicFile = keyToFileMap.get(key);
			for (IMusicEditor editor : editorList) {
				editor.edit(musicFile);
			}
			editorList.clear();
		}
	}
	
	public List<MusicFile> getMusicFiles() {
		ArrayList<MusicFile> files = new ArrayList<MusicFile>(keyToFileMap.size());
		files.addAll(keyToFileMap.values());
		return files;
		
	}
	
	public void setSourceEncoding(Charset encoding) {
		sourceEncoding = encoding;
		for (MusicFile file : keyToFileMap.values()) {
			file.setSourceEncoding(encoding);
		}
	}
}
