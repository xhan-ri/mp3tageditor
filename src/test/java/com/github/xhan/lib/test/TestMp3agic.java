package com.github.xhan.lib.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.mozilla.universalchardet.UniversalDetector;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class TestMp3agic {

	@Test
	public void readChinese() throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		Mp3File file = new Mp3File("/Users/xhan/Desktop/sq001.mp3");
//		file.save("/Users/xhan/Desktop/032.mp3");
//		file = new  Mp3File("/Users/xhan/Desktop/032.mp3");
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
		System.out.println("Untouched album name: " + albumName);
		System.out.println(detectEncoding(albumName.getBytes()));
//		String encodingName = detectEncoding(getUnencodedBytes(albumName));
//		System.out.println("Encoding of album: " + encodingName);
		String encodedName = new String(getUnencodedBytes(albumName), Charset.forName("GB18030"));
		System.out.println("Encoded album name: " + encodedName);
		ID3v24Tag tag = new ID3v24Tag();
		tag.setEncoder("UTF-8");
		tag.setAlbum(encodedName);//reEncoding(encodedName, Charset.forName("GB18030"), Charset.forName("UTF-8")));
		file.removeId3v1Tag();
		file.setId3v2Tag(tag);
		file.save("/Users/xhan/Desktop/sq002.mp3");
//		System.out.println("After re-encoding: " + encodedName);
//		
//		System.out.println(detectEncoding(encodedName.getBytes(Charset.forName("GB18030"))));
//		
//		file.getId3v1Tag().setAlbum(reEncoding(encodedName, Charset.forName("GB18030"), Charset.forName("UTF8")));
//		file.save("/Users/xhan/Desktop/033.mp3");
	}
	
	private String detectEncoding(String filename) throws IOException {
		UniversalDetector detector = new UniversalDetector(null);
		byte[] buf = new byte[4096];
		FileInputStream fis = new FileInputStream(filename);
		int read = 0;
		while ((read = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, read);
		}
		detector.dataEnd();
		return detector.getDetectedCharset();
	}
	
	private String detectEncoding(byte[] bytes) {
		UniversalDetector detector = new UniversalDetector(null);
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		return encoding;
	}
	
	private byte[] getUnencodedBytes(String input) throws UnsupportedEncodingException {
		return input.getBytes("ISO-8859-1");
	}
	
	private String reEncoding(String input, Charset fromCharset, Charset toCharset) {
		byte[] buffer = input.getBytes(fromCharset);
		return new String(buffer, toCharset);
	}
}
