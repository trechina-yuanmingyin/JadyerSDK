package com.jadyer.sdk.mpp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.jadyer.sdk.mpp.filter.WeixinOAuthFilter;
import com.jadyer.sdk.mpp.model.WeixinOAuthAccessToken;
import com.jadyer.sdk.mpp.util.HttpUtil;
import com.jadyer.sdk.mpp.util.MPPUtil;
import com.jadyer.sdk.mpp.util.TokenHolder;

/**
 * 接收微信服务器回调
 * @create Oct 19, 2015 8:30:44 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Controller
@RequestMapping(value="/weixin/helper")
public class WeixinHelperController {
	private static final Logger logger = LoggerFactory.getLogger(WeixinHelperController.class);

	/**
	 * 获取网页access_token
	 * @param uid   预留字段,以便扩展为多用户支持
	 * @param code  微信服务器发放的有效期为5分钟的用于换取换取网页access_token的code
	 * @param state 重定向到微信服务器时,由开发者服务器携带过去的参数,这里会原样带回来
	 * @return 获取失败则返回一个友好的HTML页面,获取成功后直接跳转到用户原本请求的资源
	 */
	@RequestMapping(value="/oauth/getAccessToken/{uid}")
	public String getAccessToken(@PathVariable String uid, String code, String state, HttpServletResponse response) throws IOException{
		if(StringUtils.isNotBlank(code)){
			WeixinOAuthAccessToken oauthAccessToken = TokenHolder.getWeixinOAuthAccessToken(WeixinOAuthFilter.appid, WeixinOAuthFilter.appsecret, code);
			if(0==oauthAccessToken.getErrcode() && StringUtils.isNotBlank(oauthAccessToken.getOpenid())){
				/**
				 * 还原state携带过来的粉丝请求的原URL
				 * @see state=/JadyerSDK/user/get/2/uname=玄玉/openid=openid或者state=/user/get/2/uname=玄玉/openid=openid
				 */
				//1.获取到URL中的非参数部分
				String uri = state.substring(0, state.indexOf("="));
				uri = uri.substring(0, uri.lastIndexOf("/"));
				//2.获取到URL中的参数部分(得到openid的方式为截取掉占位的,再追加真正的值)
				String params = state.substring(uri.length()+1);
				params = params.replaceAll("/", "&").substring(0, params.length()-6) + oauthAccessToken.getOpenid();
				//3.拼接粉丝请求的原URL并跳转过去
				String fullURI = uri + "?" + params;
				logger.info("还原粉丝请求的资源得到state={}", fullURI);
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + fullURI;
			}
		}
		response.setCharacterEncoding(HttpUtil.DEFAULT_CHARSET);
		response.setContentType("text/plain; charset=" + HttpUtil.DEFAULT_CHARSET);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = response.getWriter();
		out.print("系统繁忙Unauthorized\r\n请联系您关注的微信公众号");
		out.flush();
		out.close();
		return null;
	}


	/**
	 * JS-SDK权限验证的签名
	 * @param appid     微信公众号AppID
	 * @param appsecret 微信公众号AppSecret
	 * @param url       当前网页的URL,不包含#及其后面部分
	 */
	@ResponseBody
	@RequestMapping(value="/sign/jssdk")
	public Map<String, String> signWeixinJSSDK(String appid, String appsecret, String url){
		Map<String, String> resultMap = new HashMap<String, String>();
		String noncestr = RandomStringUtils.randomNumeric(16);
		long timestamp = (long)(System.currentTimeMillis()/1000);
		resultMap.put("appid", appid);
		resultMap.put("timestamp", String.valueOf(timestamp));
		resultMap.put("noncestr", noncestr);
		resultMap.put("signature", MPPUtil.signWeixinJSSDK(appid, appsecret, noncestr, String.valueOf(timestamp), url));
		resultMap.put("url", url);
		return resultMap;
	}
}