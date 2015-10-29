package com.jadyer.sdk.mpp.util;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadyer.sdk.mpp.model.OAuthAccessToken;

/**
 * 微信或QQ公众平台Token持有器
 * @create Oct 29, 2015 8:11:50 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class TokenHolder {
	private static final Logger logger = LoggerFactory.getLogger(TokenHolder.class);
	private static final String FLAG_WEIXIN_ACCESSTOKEN = "weixin_access_token";
	private static final String FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_ACCESSTOKEN + "expire_time";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN = "weixin_oauth_access_token";
	private static final String FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME = FLAG_WEIXIN_OAUTH_ACCESSTOKEN + "expire_time";
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
			logger.info("get {} access_token from memory", appid);
			return (String)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN);
		}
		String accessToken = MPPUtil.getWeixinAccessToken(appid, appsecret);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN, accessToken);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME, Calendar.getInstance());
		return accessToken;
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
	public static OAuthAccessToken getWeixinOAuthAccessToken(String appid, String appsecret, String code){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME);
		expireTime.add(Calendar.MINUTE, 110);
		if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
			logger.info("get {} oauth_access_token from memory", appid);
			return (OAuthAccessToken)tokenMap.get(FLAG_WEIXIN_OAUTH_ACCESSTOKEN);
		}
		OAuthAccessToken oauthAccessToken = MPPUtil.getWeixinOAuthAccessToken(appid, appsecret, code);
		tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN, oauthAccessToken);
		tokenMap.put(FLAG_WEIXIN_OAUTH_ACCESSTOKEN_EXPIRETIME, Calendar.getInstance());
		return oauthAccessToken;
	}
}