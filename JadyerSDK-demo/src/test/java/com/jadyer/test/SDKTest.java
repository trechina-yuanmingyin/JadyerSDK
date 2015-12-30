package com.jadyer.test;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import com.jadyer.sdk.qq.helper.QQHelper;
import com.jadyer.sdk.util.HttpUtil;

public class SDKTest {
	/**
	 * 模拟粉丝发消息给QQ公众号后，QQ服务器推消息给开发者服务器
	 * @create Nov 28, 2015 6:25:05 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@Test
	public void sendMsgToQQ(){
		String reqURL = "http://127.0.0.1/qq/e9293c3886c411e5bc85000c292d56c5?openId=E12D231CFC30438FB6970B0C7669C101&puin=2878591677";
		String reqData = "<xml><ToUserName><![CDATA[2878591677]]></ToUserName><FromUserName><![CDATA[E12D231CFC30438FB6970B0C7669C101]]></FromUserName><CreateTime>1448703573</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[你好]]></Content><MsgId>875639142</MsgId></xml>";
		System.out.println("开发者服务器返回消息-->" + HttpUtil.post(reqURL, reqData, null));
	}

	/**
	 * 设置QQ自定义菜单
	 * @create Nov 28, 2015 9:28:06 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@Test
	public void createQQMenu(){
		String accesstoken = "c33c7270918c211d4fd29eb42ed7296c";
		String menu = "{\"button\":[{\"name\":\"个人中心\",\"sub_button\":[{\"name\":\"我的博客\",\"type\":\"view\",\"url\":\"http://blog.csdn.net/jadyer\"},{\"name\":\"我的GitHub\",\"type\":\"view\",\"url\":\"https://github.com/jadyer\"}]},{\"key\":\"joke\",\"name\":\"幽默笑话\",\"type\":\"click\"},{\"name\":\"休闲驿站\",\"sub_button\":[{\"key\":\"123abc\",\"name\":\"历史上的今天\",\"type\":\"click\"},{\"key\":\"456\",\"name\":\"天气预报\",\"type\":\"click\"}]}]}";
		System.out.println(ReflectionToStringBuilder.toString(QQHelper.createQQMenu(accesstoken, menu), ToStringStyle.MULTI_LINE_STYLE));;
	}
}