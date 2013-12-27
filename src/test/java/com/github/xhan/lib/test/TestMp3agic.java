package com.github.xhan.lib.test;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class TestMp3agic {

	private static final Logger logger = Logger.getLogger(TestMp3agic.class);
	@Test
	public void readChinese() throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		Mp3File file = new Mp3File("/Users/xhan/Desktop/sq001.mp3");
		int id3ver = 0;
		id3ver = file.hasId3v1Tag() ? 1 : file.hasId3v2Tag() ? 2 : 0;
		String albumName = null;
		switch (id3ver) {
			case 1:
				albumName = file.getId3v1Tag().getAlbum();
				break;
			case 2:
				albumName = file.getId3v2Tag().getAlbum();
				break;
			default :
				albumName = "";
				break;
		}
		logger.info("Untouched album name: " + albumName);
		String encodedName = reEncoding(albumName, Charset.forName("ISO-8859-1"), Charset.forName("GB18030"));
		logger.info("Re-encoded album name: " + encodedName);
		ID3v24Tag tag = new ID3v24Tag();
		tag.setEncoder("UTF-8");
		tag.setAlbum(encodedName);
		file.removeId3v1Tag();
		file.setId3v2Tag(tag);
		file.save("/Users/xhan/Desktop/sq002.mp3");
	}
	
	
	private String reEncoding(String input, Charset fromCharset, Charset toCharset) {
		byte[] buffer = input.getBytes(fromCharset);
		return new String(buffer, toCharset);
	}
}
