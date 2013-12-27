package com.github.xhan.mp3tageditor.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.xhan.mp3tageditor.Engine;
import com.github.xhan.mp3tageditor.EngineLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EngineTest {

	Engine engine;
	
	@Before
	public void startup() {
		engine = EngineLoader.startEngine();
	}
	@Test
	public void basic() {
		engine.test();
	}
}
