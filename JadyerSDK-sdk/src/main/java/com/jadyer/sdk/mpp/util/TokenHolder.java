package com.jadyer.sdk.mpp.util;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import com.jadyer.sdk.mpp.model.WeixinOAuthAccessToken;

/**
 * 微信或QQ公众平台Token持有器
 * @create Oct 29, 2015 8:11:50 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class TokenHolder {
	private static final String FLAG_WEIXIN_ACCESSTOKEN = "weixin_access_token";
	private static final String FLAG_WEIXIN_JSAPI_TICKET = "weixin_jsapi_ticket";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN = "weixin_oauth_access_token";
	private static final String FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_ACCESSTOKEN + "_expire_time";
	private static final String FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME = FLAG_WEIXIN_JSAPI_TICKET + "_expire_time";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_OAUTH_ACCESSTOKEN + "_expire_time";
	private static ConcurrentHashMap<String, Object> tokenMap = new ConcurrentHashMap<String, Object>();

	private TokenHolder(){}

	/**
	 * 获取微信access_token
	 * @see 这里只缓存110分钟,详细介绍见http://mp.weixin.qq.com/wiki/11/0e4b294685f817b95cbed85ba5e82b8f.html
	 * @param appid     微信公众号AppID
	 * @param appsecret 微信公众号AppSecret
	 * @create Oct 29, 2015 8:13:24 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinAccessToken(String appid, String appsecret){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME);
		expireTime.add(Calendar.MINUTE, 110);
		if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
			return (String)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN);
		}
		String accessToken = MPPUtil.getWeixinAccessToken(appid, appsecret);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN, accessToken);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME, Calendar.getInstance());
		return accessToken;
	}


	/**
	 * 获取微信jsapi_ticket
	 * @see 这里只缓存110分钟,详细介绍见http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 * @create Oct 29, 2015 9:55:33 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinJSApiTicket(String accesstoken){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME);
		expireTime.add(Calendar.MINUTE, 110);
		if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
			return (String)tokenMap.get(FLAG_WEIXIN_JSAPI_TICKET);
		}
		String jsapiTicket = MPPUtil.getWeixinJSApiTicket(accesstoken);
		tokenMap.put(FLAG_WEIXIN_JSAPI_TICKET, jsapiTicket);
		tokenMap.put(FLAG_WEIXIN_JSAPI_TICKET_EXPIRETIME, Calendar.getInstance());
		return jsapiTicket;
	}


	/**
	 * 通过code换取网页授权access_token
	 * @see 这里只缓存110分钟,详细介绍见http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
	 * @param appid     微信公众号AppID
	 * @param appsecret 微信公众号AppSecret
	 * @param code      换取access_token的有效期为5分钟的票据
	 * @return 返回获取到的网页access_token(获取失败时的应答码也在该返回中)
	 * @create Oct 29, 2015 9:32:01 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static WeixinOAuthAccessToken getWeixinOAuthAccessToken(String appid, String appsecret, String code){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME);
		expireTime.add(Calendar.MINUTE, 110);
		if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
			return (WeixinOAuthAccessToken)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN);
		}
		WeixinOAuthAccessToken weixinOauthAccessToken = MPPUtil.getWeixinOAuthAccessToken(appid, appsecret, code);
		tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN, weixinOauthAccessToken);
		tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME, Calendar.getInstance());
		return weixinOauthAccessToken;
	}
}