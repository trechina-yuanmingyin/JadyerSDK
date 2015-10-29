package com.jadyer.sdk.mpp.util;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信或QQ公众平台Token持有器
 * @create Oct 29, 2015 8:11:50 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class TokenHolder {
	private static final Logger logger = LoggerFactory.getLogger(TokenHolder.class);
	private static final String FLAG_WEIXIN_ACCESSTOKEN = "weixin_access_token";
	private static final String FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME = "expire_time";
	private static ConcurrentHashMap<String, Object> tokenMap = new ConcurrentHashMap<String, Object>();

	private TokenHolder(){}

	/**
	 * 获取微信access_token
	 * @see 只缓存110分钟
	 * @create Oct 29, 2015 8:13:24 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String getWeixinAccessToken(String appid, String appsecret){
		Calendar expireTime = (Calendar)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN + FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME);
		expireTime.add(Calendar.MINUTE, 110);
		if((expireTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) >= 0){
			logger.info("已于内存中取到{}的access_token", appid);
			return (String)tokenMap.get(FLAG_WEIXIN_ACCESSTOKEN);
		}
		String accessToken = MPPUtil.getWeixinAccessToken(appid, appsecret);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN, accessToken);
		tokenMap.put(FLAG_WEIXIN_ACCESSTOKEN_EXPIRETIME, Calendar.getInstance());
		return accessToken;
	}
}