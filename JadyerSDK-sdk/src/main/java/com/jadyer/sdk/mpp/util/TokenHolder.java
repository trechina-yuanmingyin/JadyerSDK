package com.jadyer.sdk.mpp.util;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.jadyer.sdk.mpp.model.WeixinOAuthAccessToken;

/**
 * 微信或QQ公众平台Token持有器
 * @see 1.appid和appsecret是与Token息息相关的,故一并缓存于此处
 * @see 2.flag常量均加了appid是考虑到微信管理平台更换绑定的公众号时,获取到access_token是旧的,从而影响自定义菜单发布
 * @create Oct 29, 2015 8:11:50 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class TokenHolder {
	private static final String WEIXIN_APPID = "weixin_appid";
	private static final String WEIXIN_APPSECRET = "weixin_appsecret";
	private static final String FLAG_WEIXIN_ACCESSTOKEN = "weixin_access_token";
	private static final String FLAG_WEIXIN_JSAPI_TICKET = "weixin_jsapi_ticket";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN = "weixin_oauth_access_token";
	private static final String FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_ACCESSTOKEN + "_expire_time";
	private static final String FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME = FLAG_WEIXIN_JSAPI_TICKET + "_expire_time";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_OAUTH_ACCESSTOKEN + "_expire_time";
	private static ConcurrentHashMap<String, Object> tokenMap = new ConcurrentHashMap<String, Object>();

	private TokenHolder(){}

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
	 * @see 这里只缓存110分钟,详细介绍见http://mp.weixin.qq.com/wiki/11/0e4b294685f817b95cbed85ba5e82b8f.html
	 * @create Oct 29, 2015 8:13:24 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinAccessToken(){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME + getWeixinAppid());
		if(null != expireTime){
			expireTime.add(Calendar.MINUTE, 110);
			if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
				return (String)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN + getWeixinAppid());
			}
		}
		String accessToken = MPPUtil.getWeixinAccessToken(getWeixinAppid(), getWeixinAppsecret());
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN + getWeixinAppid(), accessToken);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME + getWeixinAppid(), Calendar.getInstance());
		return accessToken;
	}


	/**
	 * 获取微信jsapi_ticket
	 * @see 这里只缓存110分钟,详细介绍见http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 * @create Oct 29, 2015 9:55:33 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinJSApiTicket(){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME + getWeixinAppid());
		if(null != expireTime){
			expireTime.add(Calendar.MINUTE, 110);
			if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
				return (String)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET + getWeixinAppid());
			}
		}
		String jsapiTicket = MPPUtil.getWeixinJSApiTicket(getWeixinAccessToken());
		tokenMap.put(FLAG_WEIXIN_JSAPI_TICKET + getWeixinAppid(), jsapiTicket);
		tokenMap.put(FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME + getWeixinAppid(), Calendar.getInstance());
		return jsapiTicket;
	}


	/**
	 * 通过code换取网页授权access_token
	 * @see 这里只缓存110分钟,详细介绍见http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
	 * @param code      换取access_token的有效期为5分钟的票据
	 * @return 返回获取到的网页access_token(获取失败时的应答码也在该返回中)
	 * @create Oct 29, 2015 9:32:01 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static WeixinOAuthAccessToken getWeixinOAuthAccessToken(String code){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME + getWeixinAppid());
		if(null != expireTime){
			expireTime.add(Calendar.MINUTE, 110);
			if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
				return (WeixinOAuthAccessToken)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN + getWeixinAppid());
			}
		}
		WeixinOAuthAccessToken weixinOauthAccessToken = MPPUtil.getWeixinOAuthAccessToken(getWeixinAppid(), getWeixinAppsecret(), code);
		tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN + getWeixinAppid(), weixinOauthAccessToken);
		tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME + getWeixinAppid(), Calendar.getInstance());
		return weixinOauthAccessToken;
	}
}