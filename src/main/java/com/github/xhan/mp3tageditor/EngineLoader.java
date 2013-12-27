package com.github.xhan.mp3tageditor;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EngineLoader {
	private ClassPathXmlApplicationContext springContext;
	private static EngineLoader loaderInstance;
	private EngineLoader () {
		springContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}
	
	public static Engine startEngine() {
		if (loaderInstance == null) {
			loaderInstance = new EngineLoader();
		}
		Engine engine = loaderInstance.getBean("engine", Engine.class);
		return engine;
	}
	
	public <T> T getBean(String name, Class<T> tClass) {
		T bean = tClass.cast(springContext.getBean(name));
		return bean;
	}
}
