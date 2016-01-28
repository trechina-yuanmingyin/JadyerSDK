package com.jadyer.sdk.demo.common.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件读取工具类
 * @see -----------------------------------------------------------------------------------------------------------
 * @see 用法为ConfigUtil.INSTANCE.getProperty("jdbc.url")
 * @see 采用枚举的方式,也是Effective Java作者Josh Bloch提倡的方式
 * @see 它不仅能避免多线程同步问题,而且还能防止反序列化重新创建新的对象
 * @see -----------------------------------------------------------------------------------------------------------
 * @version v2.1
 * @history v2.1-->增加<code>getPropertyBySysKey()</code>方法,用于获取配置文件的键值中含系统属性时的值,详见该方法注释
 * @history v2.0-->采用枚举的方式实现单例
 * @history v1.0-->通过内部类实现单例
 * @update 2015-2-2 下午05:22:03
 * @create Jun 7, 2012 5:30:10 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public enum ConfigUtil {
	INSTANCE;
	
	private Properties config;
	
	private ConfigUtil(){
		config = new Properties();
		try {
			config.load(ConfigUtil.class.getResourceAsStream("/config-"+System.getProperty("spring.profiles.active")+".properties"));
		} catch (IOException e) {
			throw new ExceptionInInitializerError("加载系统配置文件失败...");
		}
	}

	public String getProperty(String key){
		return config.getProperty(key);
	}
	
	public int getPropertyForInt(String key){
		return Integer.parseInt(config.getProperty(key));
	}
}