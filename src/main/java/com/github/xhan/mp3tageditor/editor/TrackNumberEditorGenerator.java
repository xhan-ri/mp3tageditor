package com.github.xhan.mp3tageditor.editor;

public class TrackNumberEditorGenerator {

	int current;
	int minDigitLen;
	int total;
	public TrackNumberEditorGenerator() {
		current = 0;
		// no end
		minDigitLen = 1;
		total = -1;
	}
	
	public TrackNumberEditorGenerator(int current, int minDigitLen, int total) {
		this();
		if (current <= 0) {
			this.current = 1;
		} else {
			this.current = current;
		}
		this.minDigitLen = minDigitLen;
		this.total = total;
	}



	public Id3Tagv2Editor<String> getEditor() {
		Id3Tagv2Editor<String> editor = new Id3Tagv2Editor<String>("Track", getTrackString(), String.class);
		return editor;
	}
	
	private String getTrackString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getNumberWithPadding(current++));
		if (total > 0) {
			builder.append("/" + getNumberWithPadding(total));
		}
		return builder.toString();
	}
	private String getNumberWithPadding(int number) {
		String noPadding = Integer.toString(number);
		int noPaddingLen = noPadding.length();
		if (noPaddingLen >= minDigitLen) {
			return noPadding;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < minDigitLen - noPaddingLen; i ++) {
			sb.append("0");
		}
		
		return sb.append(noPadding).toString();
	}
}
