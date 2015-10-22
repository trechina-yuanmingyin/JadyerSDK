package com.jadyer.sdk.demo.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日志工具类
 * @see -----------------------------------------------------------------------------------------------------------
 * @see Java日志性能那些事-----http://www.infoq.com/cn/articles/things-of-java-log-performance
 * @see Log4j2.x比Logback好----http://donlianli.iteye.com/blog/1921735
 * @see SpringSide使用Logback--https://github.com/springside/springside4/wiki/Log
 * @see -----------------------------------------------------------------------------------------------------------
 * @version v2.2
 * @history v2.2-->优化Log获取为显式指定所要获取的Log,未指定时默认取上一次的Log,没有上一次的则取defaultLog
 * @history v2.1-->新增多线程情景下的日志集中打印功能
 * @history v2.0-->新增日志的数据库保存和邮件发送功能
 * @history v1.0-->通过<code>java.lang.ThreadLocal</code>实现日志记录器
 * @update Aug 26, 2015 3:29:21 PM
 * @create Dec 18, 2012 6:19:31 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public final class LogUtil {
	private LogUtil(){}

	//自定义线程范围内共享的对象
	//即它会针对不同线程分别创建独立的对象,此时每个线程得到的将是自己的实例,各线程间得到的实例没有任何关联
	private static ThreadLocal<Log> currentLoggerMap = new ThreadLocal<Log>();

	//定义日志记录器
	private static Log appLogger = LogFactory.getLog("appLogger");

	public static Log getAppLogger() {
		Log log = currentLoggerMap.get();
		if(null == log){
			currentLoggerMap.set(appLogger);
			return appLogger;
		}else{
			return log;
		}
	}
}