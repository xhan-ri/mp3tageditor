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
import com.github.xhan.mp3tageditor.editor.TrackNumberEditorGenerator;
@Component
public class Engine {

	private Logger logger = Logger.getLogger(Engine.class);
	private Map<String, MusicFile> keyToFileMap;
	private Map<String, List<IMusicEditor>> keyToEditorsMap;
	private List<String> keyOrderList;
	private Charset sourceEncoding;
	
	protected Engine() {
		keyToFileMap = new HashMap<String, MusicFile>();
		keyToEditorsMap = new HashMap<String, List<IMusicEditor>>();
		keyOrderList = new LinkedList<String>();
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
		keyToFileMap.put(musicFile.getId(), musicFile);
		keyToEditorsMap.put(musicFile.getId(), new LinkedList<IMusicEditor>());
		keyOrderList.add(musicFile.getId());
	}
	
	public void addGlobalEditor(IMusicEditor editor) {
		for (String key : keyOrderList) {
			List<IMusicEditor> editorList = keyToEditorsMap.get(key);
			editorList.add(editor);
		}
	}
	
	public void setTrackNumberGenerator(TrackNumberEditorGenerator generator) {
		if (generator == null) {
			return;
		}
		for (String key : keyOrderList) {
			List<IMusicEditor> editorList = keyToEditorsMap.get(key);
			editorList.add(generator.getEditor());
		}
		
	}
	
	public void runEditors() {
		for (String key : keyOrderList) {
			List<IMusicEditor> editorList = keyToEditorsMap.get(key);
			MusicFile musicFile = keyToFileMap.get(key);
			for (IMusicEditor editor : editorList) {
				editor.edit(musicFile);
			}
			editorList.clear();
		}
	}
	
	public List<MusicFile> getMusicFiles() {
		ArrayList<MusicFile> files = new ArrayList<MusicFile>(keyOrderList.size());
		for (String key : keyOrderList) {
			files.add(keyToFileMap.get(key));
		}
		return files;
		
	}
	
	public void setSourceEncoding(Charset encoding) {
		sourceEncoding = encoding;
		for (MusicFile file : keyToFileMap.values()) {
			file.setSourceEncoding(encoding);
		}
	}
}
