package org.xiaofeng;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;

public class testJAudioTagger {

	@Test
	public void tryReadChinese() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException {
		AudioFile af = AudioFileIO.read(new File("/Users/xhan/Desktop/sq002.mp3"));
		String album = af.getTag().getFirst(FieldKey.ALBUM);
		System.out.println(album);
		System.out.println("After re-encoding: " + new String(getUnencodedBytes(album), Charset.forName("GB18030")));
	}
	private byte[] getUnencodedBytes(String input) throws UnsupportedEncodingException {
		return input.getBytes("ISO-8859-1");
	}
}
