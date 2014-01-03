package com.github.xhan.mp3tageditor.editor;

import java.nio.charset.Charset;

import com.github.xhan.mp3tageditor.MusicFile;

public class SourceEncodingEditor implements IMusicEditor {

	private Charset sourceEncoding;
	
	public SourceEncodingEditor (Charset sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	@Override
	public MusicFile edit(MusicFile musicFile) {
		musicFile.setSourceEncoding(sourceEncoding);
		return musicFile;
	}

}
