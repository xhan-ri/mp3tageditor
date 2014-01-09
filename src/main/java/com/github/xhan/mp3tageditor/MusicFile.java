package com.github.xhan.mp3tageditor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MusicFile {

	private static final Logger logger = Logger.getLogger(MusicFile.class);
	String id;
	Mp3File mp3File;
	ID3v24Tag newTag;
	Charset sourceEncoding;
	File osFile;
	private static final Charset targetEncoding = Charset.forName("UTF-8");
	public MusicFile(File file) {
		if (!isValid(file)) {
			throw new RuntimeException("File cannot be read or write");
		}
		try {
			mp3File = new Mp3File(file.getAbsolutePath(), true);
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			throw new RuntimeException(e);
		}
		osFile = file;
		id = UUID.randomUUID().toString();
		sourceEncoding = Charset.defaultCharset();
		newTag = new ID3v24Tag();
		readTag();
	}
	
	public static boolean isValid(File file) {
		if (!file.canRead() || !file.canWrite()) {
			return false;
		}
		
		return true;
	}
	
	private void readTag() {
		readId3v1Tag();
		readId3v2Tag();
	}
	
	private void readId3v1Tag() {
		if (!mp3File.hasId3v1Tag()) {
			return;
		}
		ID3v1 tag = mp3File.getId3v1Tag();
		updateFromV1Tag(tag);
	}

	private void updateFromV1Tag(ID3v1 tag) {
		newTag.setAlbum(transEncoding(tag.getAlbum()));
		newTag.setArtist(transEncoding(tag.getArtist()));
		newTag.setComment(transEncoding(tag.getComment()));
		newTag.setGenre(tag.getGenre());
		newTag.setGenreDescription(transEncoding(tag.getGenreDescription()));
		newTag.setTitle(transEncoding(tag.getTitle()));
		newTag.setTrack(transEncoding(tag.getTitle()));
		newTag.setTrack(transEncoding(tag.getTrack()));
		newTag.setYear(transEncoding(tag.getYear()));
	}
	
	private void readId3v2Tag() {
		if (!mp3File.hasId3v2Tag()) {
			return;
		}
		ID3v2 tag = mp3File.getId3v2Tag();
		updateFromV1Tag(tag);
		
		//v2 only tags
		newTag.setComposer(transEncoding(tag.getComposer()));
		newTag.setAlbumArtist(transEncoding(tag.getAlbumArtist()));
		newTag.setAlbumImage(tag.getAlbumImage(), tag.getAlbumImageMimeType());
		newTag.setCopyright(transEncoding(tag.getCopyright()));
		newTag.setEncoder(targetEncoding.name());
		newTag.setPublisher(transEncoding(tag.getPublisher()));
		newTag.setUrl(tag.getUrl());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusicFile other = (MusicFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Charset getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(Charset sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
		readTag();
	}
	
	public ID3v24Tag getNewTag() {
		return newTag;
	}

	public void setNewTag(ID3v24Tag newTag) {
		this.newTag = newTag;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void save() {
		mp3File.removeId3v1Tag();
		mp3File.setId3v2Tag(newTag);
		try {
			mp3File.save(osFile.getAbsolutePath());
		} catch (NotSupportedException | IOException e) {
			String msg = "Cannot write music file: " + osFile.getAbsolutePath();
			logger.error(msg);
		}
		
	}

	private String transEncoding(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
		byte[] buffer = input.getBytes(Charset.forName("ISO-8859-1"));
		return new String(buffer, sourceEncoding);
	}

	@Override
	public String toString() {
		return "MusicFile [id=" + id + ", mp3File=" + mp3File + ", newTag="
				+ ToStringBuilder.reflectionToString(newTag) + ", sourceEncoding=" + sourceEncoding + "]";
	}
}
