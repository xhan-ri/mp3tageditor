package com.github.xhan.mp3tageditor.editor;

import java.lang.reflect.InvocationTargetException;

import com.github.xhan.mp3tageditor.MusicFile;
import com.mpatric.mp3agic.ID3v24Tag;

public class Id3Tagv2Editor <T> implements IMusicEditor {

	String tagName;
	T tagValue;
	Class<T> tagClass;
	
	public Id3Tagv2Editor(String tagName, T tagValue, Class<T> tClass) {
		super();
		this.tagName = tagName;
		this.tagValue = tagValue;
		this.tagClass = tClass;
	}

	@Override
	public MusicFile edit(MusicFile musicFile) {
		setTagValue(musicFile.getNewTag());
		return musicFile;
	}
	
	private void setTagValue(ID3v24Tag tag) {
		try {
			tag.getClass().getMethod("set" + tagName, tagClass).invoke(tag, tagValue);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
