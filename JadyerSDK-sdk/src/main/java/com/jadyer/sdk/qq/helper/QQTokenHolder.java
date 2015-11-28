package com.jadyer.sdk.qq.helper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com.jadyer.sdk.qq.model.QQOAuthAccessToken;

/**
 * QQ公众平台Token持有器
 * @see 1.appid和appsecret是与Token息息相关的,故一并缓存于此处
 * @see 2.flag常量均加了appid是考虑到更换绑定的公众号时,获取到access_token是旧的,从而影响自定义菜单发布
 * @create Nov 28, 2015 8:25:40 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQTokenHolder {
	private static final String QQ_APPID = "qq_appid";
	private static final String QQ_APPSECRET = "qq_appsecret";
	private static final String FLAG_QQ_ACCESSTOKEN = "qq_access_token";
	private static final String FLAG_QQ_JSAPI_TICKET = "qq_jsapi_ticket";
	private static final String FLAG_QQ_OAUTH_ACCESSTOKEN = "qq_oauth_access_token";
	private static final String FLAG_QQ_ACCESSTOKEN_EXPIRETIME = FLAG_QQ_ACCESSTOKEN + "_expire_time";
	private static final String FLAG_QQ_JSAPI_TICKET_EXPIRETIME = FLAG_QQ_JSAPI_TICKET + "_expire_time";
	private static final String FLAG_QQ_OAUTH_ACCESSTOKEN_EXPIRETIME = FLAG_QQ_OAUTH_ACCESSTOKEN + "_expire_time";
	private static final long QQ_TOKEN_EXPIRE_TIME_MILLIS = 7000 * 1000;
	private static AtomicBoolean qqAccessTokenRefreshing = new AtomicBoolean(false);
	private static AtomicBoolean qqJSApiTicketRefreshing = new AtomicBoolean(false);
	private static AtomicBoolean qqOAuthAccessTokenRefreshing = new AtomicBoolean(false);
	private static ConcurrentHashMap<String, Object> tokenMap = new ConcurrentHashMap<String, Object>();

	private QQTokenHolder(){}

	/**
	 * 记录QQ媒体文件存储在本地的完整路径
	 * @param mediaId           QQ媒体文件ID
	 * @param localFileFullPath QQ媒体文件存储在本地的完整路径
	 * @return 返回已设置的QQ媒体文件存储在本地的完整路径
	 * @create Nov 28, 2015 8:27:41 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String setMediaIdFilePath(String mediaId, String localFileFullPath){
		tokenMap.put(mediaId, localFileFullPath);
		return getMediaIdFilePath(mediaId);
	}


	/**
	 * 获取QQ媒体文件存储在本地的完整路径
	 * @param mediaId QQ媒体文件ID
	 * @return 返回QQ媒体文件存储在本地的完整路径
	 * @create Nov 28, 2015 8:28:36 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getMediaIdFilePath(String mediaId){
		String localFileFullPath = (String)tokenMap.get(mediaId);
		if(StringUtils.isBlank(localFileFullPath)){
			throw new IllegalArgumentException("不存在的本地媒体文件mediaId=" + mediaId);
		}
		return localFileFullPath;
	}


	/**
	 * 设置QQappid
	 * @return 返回已设置的QQappid
	 * @create Nov 28, 2015 8:28:47 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String setQQAppid(String appid){
		tokenMap.put(QQ_APPID, appid);
		return getQQAppid();
	}


	/**
	 * 获取已设置的QQappid
	 * @create Nov 28, 2015 8:29:06 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getQQAppid(){
		String appid = (String)tokenMap.get(QQ_APPID);
		if(StringUtils.isBlank(appid)){
			throw new IllegalArgumentException("未设置QQappid");
		}
		return appid;
	}


	/**
	 * 设置QQappsecret
	 * @return 返回已设置的QQappsecret
	 * @create Nov 28, 2015 8:29:28 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String setQQAppsecret(String appsecret){
		tokenMap.put(QQ_APPSECRET, appsecret);
		return getQQAppsecret();
	}


	/**
	 * 获取已设置的QQappsecret
	 * @create Nov 28, 2015 8:29:39 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getQQAppsecret(){
		String appsecret = (String)tokenMap.get(QQ_APPSECRET);
		if(StringUtils.isBlank(appsecret)){
			throw new IllegalArgumentException("未设置QQappsecret");
		}
		return appsecret;
	}


	/**
	 * 获取QQaccess_token
	 * @see 这里只缓存7000s,详细介绍见<<QQ公众号API文档.pdf>>-20150907版
	 * @see 7000s到期时,一个请求在更新access_token的过程中,另一个请求进来时,其取到的是旧的access_token(200s内都是有效的)
	 * @create Nov 28, 2015 8:30:12 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getQQAccessToken(){
		Long expireTime = (Long)tokenMap.get(FLAG_QQ_ACCESSTOKEN_EXPIRETIME + getQQAppid());
		if(null!=expireTime && expireTime>=System.currentTimeMillis()){
			return (String)tokenMap.get(FLAG_QQ_ACCESSTOKEN + getQQAppid());
		}
		if(qqAccessTokenRefreshing.compareAndSet(false, true)){
			String accessToken = QQHelper.getQQAccessToken(getQQAppid(), getQQAppsecret());
			tokenMap.put(FLAG_QQ_ACCESSTOKEN + getQQAppid(), accessToken);
			tokenMap.put(FLAG_QQ_ACCESSTOKEN_EXPIRETIME + getQQAppid(), System.currentTimeMillis()+QQ_TOKEN_EXPIRE_TIME_MILLIS);
			qqAccessTokenRefreshing.set(false);
		}
		return (String)tokenMap.get(FLAG_QQ_ACCESSTOKEN + getQQAppid());
	}


	/**
	 * 获取QQjsapi_ticket
	 * @see 这里只缓存7000s,详细介绍见<<QQ公众号API文档.pdf>>-20150907版
	 * @create Nov 28, 2015 8:31:20 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getQQJSApiTicket(){
		Long expireTime = (Long)tokenMap.get(FLAG_QQ_JSAPI_TICKET_EXPIRETIME + getQQAppid());
		if(null!=expireTime && expireTime>=System.currentTimeMillis()){
			return (String)tokenMap.get(FLAG_QQ_JSAPI_TICKET + getQQAppid());
		}
		if(qqJSApiTicketRefreshing.compareAndSet(false, true)){
			String jsapiTicket = QQHelper.getQQJSApiTicket(getQQAccessToken());
			tokenMap.put(FLAG_QQ_JSAPI_TICKET + getQQAppid(), jsapiTicket);
			tokenMap.put(FLAG_QQ_JSAPI_TICKET_EXPIRETIME + getQQAppid(), System.currentTimeMillis()+QQ_TOKEN_EXPIRE_TIME_MILLIS);
			qqJSApiTicketRefreshing.set(false);
		}
		return (String)tokenMap.get(FLAG_QQ_JSAPI_TICKET + getQQAppid());
	}


	/**
	 * 通过code换取网页授权access_token
	 * @see 这里只缓存7000s,详细介绍见<<QQ公众号API文档.pdf>>-20150907版
	 * @param code 换取access_token的有效期为5分钟的票据
	 * @return 返回获取到的网页access_token(获取失败时的应答码也在该返回中)
	 * @create Nov 28, 2015 8:32:05 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static QQOAuthAccessToken getQQOAuthAccessToken(String code){
		Long expireTime = (Long)tokenMap.get(FLAG_QQ_OAUTH_ACCESSTOKEN_EXPIRETIME + getQQAppid());
		if(null!=expireTime && expireTime>=System.currentTimeMillis()){
			return (QQOAuthAccessToken)tokenMap.get(FLAG_QQ_OAUTH_ACCESSTOKEN + getQQAppid());
		}
		if(qqOAuthAccessTokenRefreshing.compareAndSet(false, true)){
			QQOAuthAccessToken qqOauthAccessToken = QQHelper.getQQOAuthAccessToken(getQQAppid(), getQQAppsecret(), code);
			tokenMap.put(FLAG_QQ_OAUTH_ACCESSTOKEN + getQQAppid(), qqOauthAccessToken);
			tokenMap.put(FLAG_QQ_OAUTH_ACCESSTOKEN_EXPIRETIME + getQQAppid(), System.currentTimeMillis()+QQ_TOKEN_EXPIRE_TIME_MILLIS);
			qqOAuthAccessTokenRefreshing.set(false);
		}
		return (QQOAuthAccessToken)tokenMap.get(FLAG_QQ_OAUTH_ACCESSTOKEN + getQQAppid());
	}
}