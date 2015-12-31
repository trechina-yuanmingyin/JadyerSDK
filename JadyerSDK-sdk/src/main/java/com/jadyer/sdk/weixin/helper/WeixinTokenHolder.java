package com.jadyer.sdk.weixin.helper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadyer.sdk.weixin.model.WeixinOAuthAccessToken;

/**
 * 微信公众平台Token持有器
 * @see 1.appid和appsecret是与Token息息相关的,故一并缓存于此处
 * @see 2.flag常量均加了appid是考虑到更换绑定的公众号时,获取到access_token是旧的,从而影响自定义菜单发布
 * @create Oct 29, 2015 8:11:50 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class WeixinTokenHolder {
	private static final Logger logger = LoggerFactory.getLogger(WeixinTokenHolder.class);
	private static final String WEIXIN_APPID = "weixin_appid";
	private static final String WEIXIN_APPSECRET = "weixin_appsecret";
	private static final String FLAG_WEIXIN_ACCESSTOKEN = "weixin_access_token";
	private static final String FLAG_WEIXIN_JSAPI_TICKET = "weixin_jsapi_ticket";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN = "weixin_oauth_access_token";
	private static final String FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_ACCESSTOKEN + "_expire_time";
	private static final String FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME = FLAG_WEIXIN_JSAPI_TICKET + "_expire_time";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_OAUTH_ACCESSTOKEN + "_expire_time";
	private static final long WEIXIN_TOKEN_EXPIRE_TIME_MILLIS = 7000 * 1000;
	private static AtomicBoolean weixinAccessTokenRefreshing = new AtomicBoolean(false);
	private static AtomicBoolean weixinJSApiTicketRefreshing = new AtomicBoolean(false);
	private static AtomicBoolean weixinOAuthAccessTokenRefreshing = new AtomicBoolean(false);
	private static ConcurrentHashMap<String, Object> tokenMap = new ConcurrentHashMap<String, Object>();

	private WeixinTokenHolder(){}

	/**
	 * 记录微信媒体文件存储在本地的完整路径
	 * @param mediaId           微信媒体文件ID
	 * @param localFileFullPath 微信媒体文件存储在本地的完整路径
	 * @return 返回已设置的微信媒体文件存储在本地的完整路径
	 * @create Nov 9, 2015 9:21:28 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String setMediaIdFilePath(String mediaId, String localFileFullPath){
		tokenMap.put(mediaId, localFileFullPath);
		return getMediaIdFilePath(mediaId);
	}


	/**
	 * 获取微信媒体文件存储在本地的完整路径
	 * @param mediaId 微信媒体文件ID
	 * @return 返回微信媒体文件存储在本地的完整路径
	 * @create Nov 9, 2015 9:21:52 PM
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
	 * 设置微信appid
	 * @return 返回已设置的微信appid
	 * @create Nov 1, 2015 2:17:33 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String setWeixinAppid(String appid){
		tokenMap.put(WEIXIN_APPID, appid);
		return getWeixinAppid();
	}


	/**
	 * 获取已设置的微信appid
	 * @create Nov 1, 2015 2:20:43 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinAppid(){
		String appid = (String)tokenMap.get(WEIXIN_APPID);
		if(StringUtils.isBlank(appid)){
			throw new IllegalArgumentException("未设置微信appid");
		}
		return appid;
	}


	/**
	 * 设置微信appsecret
	 * @return 返回已设置的微信appsecret
	 * @create Nov 1, 2015 2:18:39 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String setWeixinAppsecret(String appsecret){
		tokenMap.put(WEIXIN_APPSECRET, appsecret);
		return getWeixinAppsecret();
	}


	/**
	 * 获取已设置的微信appsecret
	 * @create Nov 1, 2015 2:21:23 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinAppsecret(){
		String appsecret = (String)tokenMap.get(WEIXIN_APPSECRET);
		if(StringUtils.isBlank(appsecret)){
			throw new IllegalArgumentException("未设置微信appsecret");
		}
		return appsecret;
	}


	/**
	 * 获取微信access_token
	 * @see 这里只缓存7000s,详细介绍见http://mp.weixin.qq.com/wiki/11/0e4b294685f817b95cbed85ba5e82b8f.html
	 * @see 7000s到期时,一个请求在更新access_token的过程中,另一个请求进来时,其取到的是旧的access_token(200s内都是有效的)
	 * @create Oct 29, 2015 8:13:24 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinAccessToken(){
		Long expireTime = (Long)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME + getWeixinAppid());
		if(null!=expireTime && expireTime>=System.currentTimeMillis()){
			return (String)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN + getWeixinAppid());
		}
		if(weixinAccessTokenRefreshing.compareAndSet(false, true)){
			String accessToken = null;
			try {
				accessToken = WeixinHelper.getWeixinAccessToken(getWeixinAppid(), getWeixinAppsecret());
				tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN + getWeixinAppid(), accessToken);
				tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME + getWeixinAppid(), System.currentTimeMillis()+WEIXIN_TOKEN_EXPIRE_TIME_MILLIS);
			} catch (IllegalAccessException e) {
				logger.error("获取微信access_token失败-->"+e.getMessage(), e);
			}
			weixinAccessTokenRefreshing.set(false);
		}
		return (String)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN + getWeixinAppid());
	}


	/**
	 * 获取微信jsapi_ticket
	 * @see 这里只缓存7000s,详细介绍见http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 * @create Oct 29, 2015 9:55:33 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinJSApiTicket(){
		Long expireTime = (Long)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME + getWeixinAppid());
		if(null!=expireTime && expireTime>=System.currentTimeMillis()){
			return (String)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET + getWeixinAppid());
		}
		if(weixinJSApiTicketRefreshing.compareAndSet(false, true)){
			String jsapiTicket = null;
			try {
				jsapiTicket = WeixinHelper.getWeixinJSApiTicket(getWeixinAccessToken());
				tokenMap.put(FLAG_WEIXIN_JSAPI_TICKET + getWeixinAppid(), jsapiTicket);
				tokenMap.put(FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME + getWeixinAppid(), System.currentTimeMillis()+WEIXIN_TOKEN_EXPIRE_TIME_MILLIS);
			} catch (IllegalAccessException e) {
				logger.error("获取微信jsapi_ticket失败-->"+e.getMessage(), e);
			}
			weixinJSApiTicketRefreshing.set(false);
		}
		return (String)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET + getWeixinAppid());
	}


	/**
	 * 通过code换取网页授权access_token
	 * @see 这里只缓存7000s,详细介绍见http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
	 * @param code 换取access_token的有效期为5分钟的票据
	 * @return 返回获取到的网页access_token(获取失败时的应答码也在该返回中)
	 * @create Oct 29, 2015 9:32:01 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static WeixinOAuthAccessToken getWeixinOAuthAccessToken(String code){
		Long expireTime = (Long)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME + getWeixinAppid());
		if(null!=expireTime && expireTime>=System.currentTimeMillis()){
			return (WeixinOAuthAccessToken)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN + getWeixinAppid());
		}
		if(weixinOAuthAccessTokenRefreshing.compareAndSet(false, true)){
			WeixinOAuthAccessToken weixinOauthAccessToken = WeixinHelper.getWeixinOAuthAccessToken(getWeixinAppid(), getWeixinAppsecret(), code);
			tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN + getWeixinAppid(), weixinOauthAccessToken);
			tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME + getWeixinAppid(), System.currentTimeMillis()+WEIXIN_TOKEN_EXPIRE_TIME_MILLIS);
			weixinOAuthAccessTokenRefreshing.set(false);
		}
		return (WeixinOAuthAccessToken)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN + getWeixinAppid());
	}
}