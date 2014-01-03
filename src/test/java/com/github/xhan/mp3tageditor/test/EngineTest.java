package com.github.xhan.mp3tageditor.test;

import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.xhan.mp3tageditor.Engine;
import com.github.xhan.mp3tageditor.EngineLoader;
import com.github.xhan.mp3tageditor.MusicFile;
import com.github.xhan.mp3tageditor.editor.Id3Tagv2Editor;
import com.github.xhan.mp3tageditor.os.filesystem.IFileSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EngineTest {

	Engine engine;
	@Resource(name="fileSystem") private IFileSystem fileSystem;
	private static final Logger logger = Logger.getLogger(EngineTest.class);
	@Before
	public void startup() {
		engine = EngineLoader.startEngine();
		engine.setSourceEncoding(Charset.forName("GB18030"));
	}
	@Test
	public void basic() {
		engine.addFiles(fileSystem.getFiles());
		List<MusicFile> files = engine.getMusicFiles();
		logger.info("Before run editors");
		for (MusicFile file : files) {
			logger.info(file.getNewTag().getAlbum());
		}
		engine.addGlobalEditor(new Id3Tagv2Editor<String>("Album", "test album", String.class));
		engine.runEditors();
		logger.info("after run editors");
		for (MusicFile file : files) {
			logger.info(file.getNewTag().getAlbum());
		}
	}
}
