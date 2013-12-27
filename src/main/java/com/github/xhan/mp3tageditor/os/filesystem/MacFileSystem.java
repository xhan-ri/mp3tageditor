package com.github.xhan.mp3tageditor.os.filesystem;

import java.io.File;

public class MacFileSystem implements IFileSystem {

	@Override
	public File[] getFiles() {
		return new File[] {new File("/Users/xhan/Desktop/sq001.mp3")};
	}

}
