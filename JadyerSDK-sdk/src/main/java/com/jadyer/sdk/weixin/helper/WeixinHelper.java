package com.jadyer.sdk.weixin.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jadyer.sdk.util.HttpUtil;
import com.jadyer.sdk.weixin.constant.WeixinCodeEnum;
import com.jadyer.sdk.weixin.constant.WeixinConstants;
import com.jadyer.sdk.weixin.model.WeixinErrorInfo;
import com.jadyer.sdk.weixin.model.WeixinFansInfo;
import com.jadyer.sdk.weixin.model.WeixinOAuthAccessToken;
import com.jadyer.sdk.weixin.model.custom.WeixinCustomMsg;
import com.jadyer.sdk.weixin.model.menu.WeixinMenu;

public final class WeixinHelper {
	private static final Logger logger = LoggerFactory.getLogger(WeixinHelper.class);

	private WeixinHelper(){}

	/**
	 * 获取微信的access_token
	 * @see 默认修饰符default即只有同包中的类才可使用
	 * @see {"access_token":"8DF72J-d_u3XIaq22e_HUY_fe5wfdoj6awnq2wDrk5v05zf1yEuUhUdtfX7yqB5wAJ1edwGrgAyJvinZTXl2RamjsqDOIg4L1humLuj32Oo","expires_in":7200}
	 * @see {"errcode":40125,"errmsg":"invalid appsecret, view more at http:\/\/t.cn\/RAEkdVq hint: [M5_jKa0125vr22]"}
	 * @return 获取失败时将抛出RuntimeException
	 */
	static String getWeixinAccessToken(String appid, String appsecret) throws IllegalAccessException {
		String reqURL = WeixinConstants.URL_WEIXIN_GET_ACCESSTOKEN.replace(WeixinConstants.URL_PLACEHOLDER_APPID, appid).replace(WeixinConstants.URL_PLACEHOLDER_APPSECRET, appsecret);
		String respData = HttpUtil.post(reqURL);
		logger.info("获取微信access_token,微信应答报文为-->{}", respData);
		Map<String, String> map = JSON.parseObject(respData, new TypeReference<Map<String, String>>(){});
		if(respData.contains("access_token")){
			return map.get("access_token");
		}
		String errmsg = WeixinCodeEnum.getMessageByCode(Integer.parseInt((map.get("errcode"))));
		if(StringUtils.isBlank(errmsg)){
			errmsg = map.get("errmsg");
		}
		throw new IllegalAccessException(errmsg);
	}


	/**
	 * 获取微信jsapi_ticket
	 * @see http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 * @see {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest hint: [3sEnya0653vr23]"}
	 * @see {"errcode":0,"errmsg":"ok","ticket":"sM4AOVdWfPE4DxkXGEs8VDqmMJ5Cg8sos8UXyJqPG4FpcrJtLcmFoV69dhqNmiQdoF1HjamNrYH9c8S9r4B_MA","expires_in":7200}
	 * @return 获取失败时将抛出RuntimeException
	 * @create Oct 29, 2015 9:45:20 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	static String getWeixinJSApiTicket(String accesstoken) throws IllegalAccessException {
		String reqURL = WeixinConstants.URL_WEIXIN_GET_JSAPI_TICKET.replace(WeixinConstants.URL_PLACEHOLDER_ACCESSTOKEN, accesstoken);
		String respData = HttpUtil.post(reqURL);
		logger.info("获取微信jsapi_ticket,微信应答报文为-->{}", respData);
		Map<String, String> map = JSON.parseObject(respData, new TypeReference<Map<String, String>>(){});
		if("0".equals(map.get("errcode"))){
			return map.get("ticket");
		}
		String errmsg = WeixinCodeEnum.getMessageByCode(Integer.parseInt((map.get("errcode"))));
		if(StringUtils.isBlank(errmsg)){
			errmsg = map.get("errmsg");
		}
		throw new IllegalAccessException(errmsg);
	}


	/**
	 * 通过code换取微信网页授权access_token
	 * @param appid     微信公众号AppID
	 * @param appsecret 微信公众号AppSecret
	 * @param code      换取access_token的有效期为5分钟的票据
	 * @return 返回获取到的网页access_token(获取失败时的应答码也在该返回中)
	 */
	static WeixinOAuthAccessToken getWeixinOAuthAccessToken(String appid, String appsecret, String code){
		String reqURL = WeixinConstants.URL_WEIXIN_OAUTH2_GET_ACCESSTOKEN.replace(WeixinConstants.URL_PLACEHOLDER_APPID, appid)
																	  .replace(WeixinConstants.URL_PLACEHOLDER_APPSECRET, appsecret)
																	  .replace(WeixinConstants.URL_PLACEHOLDER_CODE, code);
		String respData = HttpUtil.post(reqURL);
		logger.info("获取微信网页access_token,微信应答报文为-->{}", respData);
		WeixinOAuthAccessToken weixinOauthAccessToken = JSON.parseObject(respData, WeixinOAuthAccessToken.class);
		if(weixinOauthAccessToken.getErrcode() != 0){
			String errmsg = WeixinCodeEnum.getMessageByCode(weixinOauthAccessToken.getErrcode());
			if(StringUtils.isNotBlank(errmsg)){
				weixinOauthAccessToken.setErrmsg(errmsg);
			}
		}
		return weixinOauthAccessToken;
	}


	/**
	 * 构建网页授权获取用户信息的获取Code地址
	 * @param appid       微信公众号AppID
	 * @param scope       应用授权作用域(snsapi_base或snsapi_userinfo)
	 * @param state       重定向后会带上state参数(开发者可以填写a-zA-Z0-9的参数值,最多128字节)
	 * @param redirectURI 授权后重定向的回调链接地址(请使用urlencode对链接进行处理)
	 */
	public static String buildWeixinOAuthCodeURL(String appid, String scope, String state, String redirectURI){
		try {
			return WeixinConstants.URL_WEIXIN_OAUTH2_GET_CODE.replace(WeixinConstants.URL_PLACEHOLDER_APPID, appid)
														  .replace(WeixinConstants.URL_PLACEHOLDER_SCOPE, scope)
														  .replace(WeixinConstants.URL_PLACEHOLDER_STATE, state)
														  .replace(WeixinConstants.URL_PLACEHOLDER_REDIRECT_URI, URLEncoder.encode(redirectURI, HttpUtil.DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}


	/**
	 * 创建自定义菜单
	 * @see -----------------------------------------------------------------------------------------------------------
	 * @see 1.自定义菜单最多包括3个一级菜单,每个一级菜单最多包含5个二级菜单
	 * @see 2.一级菜单最多4个汉字,二级菜单最多7个汉字,多出来的部分将会以"..."代替
	 * @see 3.由于微信客户端缓存,创建的菜单需24小时微信客户端才会展现,测试时可尝试取消关注公众账号后再次关注后查看效果
	 * @see 4.修改菜单时(修改内容或菜单数量等)不需要删除菜单,直接调用创建接口即可,微信会自动覆盖以前创建的菜单
	 * @see -----------------------------------------------------------------------------------------------------------
	 */
	public static WeixinErrorInfo createWeixinMenu(String accesstoken, WeixinMenu menu){
		String reqURL = WeixinConstants.URL_WEIXIN_GET_CREATE_MENU.replace(WeixinConstants.URL_PLACEHOLDER_ACCESSTOKEN, accesstoken);
		String reqData = JSON.toJSONString(menu);
		logger.info("自定义菜单创建-->待发送的JSON为{}", reqData);
		String respData = HttpUtil.post(reqURL, reqData, null);
		logger.info("自定义菜单创建-->微信应答JSON为{}", respData);
		WeixinErrorInfo errinfo = JSON.parseObject(respData, WeixinErrorInfo.class);
		if(errinfo.getErrcode() != 0){
			String errmsg = WeixinCodeEnum.getMessageByCode(errinfo.getErrcode());
			if(StringUtils.isNotBlank(errmsg)){
				errinfo.setErrmsg(errmsg);
			}
		}
		return errinfo;
	}


	/**
	 * 创建自定义菜单
	 * @see String menuJson = "{\"button\":[{\"type\":\"view\", \"name\":\"我的博客\", \"url\":\"http://blog.csdn.net/jadyer\"}, {\"type\":\"click\", \"name\":\"今日歌曲\", \"key\":\"V1001_TODAY_MUSIC\"}, {\"name\":\"个人中心\", \"sub_button\": [{\"type\":\"view\", \"name\":\"搜索\", \"url\":\"http://www.soso.com/\"}, {\"type\":\"view\", \"name\":\"视频\", \"url\":\"http://v.qq.com/\"}, {\"type\":\"click\", \"name\":\"赞一下我们\", \"key\":\"V1001_GOOD\"}]}]}";
	 * @see String menuJson = "{\"button\":[{\"type\":\"view\", \"name\":\"我的博客\", \"url\":\"http://blog.csdn.net/jadyer\"}, {\"name\":\"个人中心\", \"sub_button\": [{\"type\":\"view\", \"name\":\"搜索\", \"url\":\"http://www.soso.com/\"}, {\"type\":\"view\", \"name\":\"视频\", \"url\":\"http://v.qq.com/\"}, {\"type\":\"click\", \"name\":\"赞一下我们\", \"key\":\"V1001_GOOD\"}]}]}";
	 */
	public static WeixinErrorInfo createWeixinMenu(String accesstoken, String menuJson){
		String reqURL = WeixinConstants.URL_WEIXIN_GET_CREATE_MENU.replace(WeixinConstants.URL_PLACEHOLDER_ACCESSTOKEN, accesstoken);
		logger.info("自定义菜单创建-->待发送的JSON为{}", menuJson);
		String respData = HttpUtil.post(reqURL, menuJson, null);
		logger.info("自定义菜单创建-->微信应答JSON为{}", respData);
		WeixinErrorInfo errinfo = JSON.parseObject(respData, WeixinErrorInfo.class);
		if(errinfo.getErrcode() != 0){
			String errmsg = WeixinCodeEnum.getMessageByCode(errinfo.getErrcode());
			if(StringUtils.isNotBlank(errmsg)){
				errinfo.setErrmsg(errmsg);
			}
		}
		return errinfo;
	}


	/**
	 * 获取用户基本信息
	 * @see 微信服务器的应答报文是下面这样的,一般Content-Type里面编码都用charset,它竟然用encoding
	 * @see HTTP/1.1 200 OK
	 * @see Server: nginx/1.8.0
	 * @see Date: Wed, 21 Oct 2015 03:56:53 GMT
	 * @see Content-Type: application/json; encoding=utf-8
	 * @see Content-Length: 357
	 * @see Connection: keep-alive
	 * @see 
	 * @see {"subscribe":1,"openid":"o3SHot22_IqkUI7DpahNv-KBiFIs","nickname":"玄玉","sex":1,"language":"en","city":"江北","province":"重庆","country":"中国","headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/Sa1DhFzJREXnSqZKc2Y2AficBdiaaiauFNBbiakfO7fJkf8Cp3oLgJQhbgkwmlN3co2aJr9iabEKJq5jsZYup3gibaVCHD5W13XRmR\/0","subscribe_time":1445398219,"remark":"","groupid":0}
	 */
	public static WeixinFansInfo getWeixinFansInfo(String accesstoken, String openid){
		String reqURL = WeixinConstants.URL_WEIXIN_GET_FANSINFO.replace(WeixinConstants.URL_PLACEHOLDER_ACCESSTOKEN, accesstoken).replace(WeixinConstants.URL_PLACEHOLDER_OPENID, openid);
		String respData = HttpUtil.post(reqURL);
		return JSON.parseObject(respData, WeixinFansInfo.class);
	}


	/**
	 * 客服接口主动推消息
	 * @see http://mp.weixin.qq.com/wiki/1/70a29afed17f56d537c833f89be979c9.html
	 * @see 目前只要粉丝在48小时内与公众号发生过互动,那么均可通过该接口主动推消息给粉丝
	 * @see 注意:如果需要以某个客服帐号来发消息,需要在请求JSON中加入customservice参数,这里暂未指定customservice
	 */
	public static WeixinErrorInfo pushWeixinMsgToFans(String accesstoken, WeixinCustomMsg customMsg){
		String reqURL = WeixinConstants.URL_WEIXIN_CUSTOM_PUSH_MESSAGE.replace(WeixinConstants.URL_PLACEHOLDER_ACCESSTOKEN, accesstoken);
		String reqData = JSON.toJSONString(customMsg);
		logger.info("客服接口主动推消息-->待发送的JSON为{}", reqData);
		String respData = HttpUtil.post(reqURL, reqData, null);
		logger.info("客服接口主动推消息-->微信应答JSON为{}", respData);
		WeixinErrorInfo errinfo = JSON.parseObject(respData, WeixinErrorInfo.class);
		if(errinfo.getErrcode() != 0){
			String errmsg = WeixinCodeEnum.getMessageByCode(errinfo.getErrcode());
			if(StringUtils.isNotBlank(errmsg)){
				errinfo.setErrmsg(errmsg);
			}
		}
		return errinfo;
	}


	/**
	 * JS-SDK权限验证的签名
	 * @see 注意这里使用的是noncestr,不是nonceStr
	 * @see http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 * @param noncestr  随机字符串
	 * @param timestamp 时间戳
	 * @param url       当前网页的URL,不包含#及其后面部分
	 * @create Oct 29, 2015 10:11:29 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String signWeixinJSSDK(String noncestr, String timestamp, String url){
		StringBuilder sb = new StringBuilder();
		sb.append("jsapi_ticket=").append(WeixinTokenHolder.getWeixinJSApiTicket()).append("&")
		  .append("noncestr=").append(noncestr).append("&")
		  .append("timestamp=").append(timestamp).append("&")
		  .append("url=").append(url);
		return DigestUtils.sha1Hex(sb.toString());
	}


	/**
	 * 获取临时素材
	 * @see http://mp.weixin.qq.com/wiki/11/07b6b76a6b6e8848e855a435d5e34a5f.html
	 * @return 获取成功时返回文件保存在本地的路径,获取失败时将抛出RuntimeException
	 * @create Oct 30, 2015 4:02:43 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String downloadWeixinTempMediaFile(String accesstoken, String mediaId){
		String reqURL = WeixinConstants.URL_WEIXIN_GET_TEMP_MEDIA_FILE.replace(WeixinConstants.URL_PLACEHOLDER_ACCESSTOKEN, accesstoken).replace(WeixinConstants.URL_PLACEHOLDER_MEDIAID, mediaId);
		Map<String, String> resultMap = HttpUtil.postWithDownload(reqURL, null);
		if("no".equals(resultMap.get("isSuccess"))){
			Map<String, String> errmap = JSON.parseObject((String)resultMap.get("failReason"), new TypeReference<Map<String, String>>(){});
			String errmsg = WeixinCodeEnum.getMessageByCode(Integer.parseInt((errmap.get("errcode"))));
			if(StringUtils.isBlank(errmsg)){
				errmsg = errmap.get("errmsg");
			}
			throw new RuntimeException("下载微信临时素材" + mediaId + "失败-->" + errmsg);
		}
		return resultMap.get("fullPath");
	}
}