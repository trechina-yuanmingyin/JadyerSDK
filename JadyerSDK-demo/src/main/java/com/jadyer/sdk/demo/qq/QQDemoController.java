package com.jadyer.sdk.demo.qq;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.qq.helper.QQHelper;
import com.jadyer.sdk.qq.helper.QQTokenHolder;
import com.jadyer.sdk.qq.model.QQErrorInfo;
import com.jadyer.sdk.qq.model.QQFansInfo;
import com.jadyer.sdk.qq.model.custom.QQCustomTextMsg;
import com.jadyer.sdk.qq.model.custom.QQCustomTextMsg.Text;
import com.jadyer.sdk.qq.model.menu.QQButton;
import com.jadyer.sdk.qq.model.menu.QQMenu;
import com.jadyer.sdk.qq.model.menu.QQSubClickButton;
import com.jadyer.sdk.qq.model.menu.QQSubViewButton;
import com.jadyer.sdk.qq.model.menu.QQSuperButton;

@Controller
@RequestMapping(value="/demo")
public class QQDemoController {
	/**
	 * 网页授权静默获取粉丝openid
	 * @create Dec 24, 2015 12:10:25 AM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/getopenid")
	public String getopenid(String openid){
		return "您的openid=" + openid;
	}


	/**
	 * 设置自定义菜单
	 * @see http://127.0.0.1/demo/createQQMenu?appid=123&appsecret=123
	 * @create Nov 28, 2015 9:02:22 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/createQQMenu")
	public QQErrorInfo createQQMenu(String appid, String appsecret){
		QQTokenHolder.setQQAppid(appid);
		QQTokenHolder.setQQAppsecret(appsecret);
		QQSubViewButton btn11 = new QQSubViewButton("我的博客", "http://blog.csdn.net/jadyer");
		QQSubViewButton btn22 = new QQSubViewButton("我的GitHub", "https://github.com/jadyer");
		QQSubClickButton btn33 = new QQSubClickButton("历史上的今天", "123abc");
		QQSubClickButton btn44 = new QQSubClickButton("天气预报", "456");
		QQSubClickButton btn55 = new QQSubClickButton("幽默笑话", "joke");
		QQSuperButton sbtn11 = new QQSuperButton("个人中心", new QQButton[]{btn11, btn22});
		QQSuperButton sbtn22 = new QQSuperButton("休闲驿站", new QQButton[]{btn33, btn44});
		QQMenu menu = new QQMenu(new QQButton[]{sbtn11, btn55, sbtn22});
		return QQHelper.createQQMenu(QQTokenHolder.getQQAccessToken(), menu);
	}


	/**
	 * 拉取粉丝信息
	 * @see http://127.0.0.1/demo/getQQFansInfo?appid=123&appsecret=123&openid=123
	 * @create Nov 28, 2015 9:59:01 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/getQQFansInfo")
	public QQFansInfo getQQFansInfo(String appid, String appsecret, String openid){
		QQTokenHolder.setQQAppid(appid);
		QQTokenHolder.setQQAppsecret(appsecret);
		return QQHelper.getQQFansInfo(QQTokenHolder.getQQAccessToken(), openid);
	}


	/**
	 * 单发主动推消息
	 * @see http://127.0.0.1/demo/pushQQMsgToFans?appid=123&appsecret=123&openid=123
	 * @create Nov 28, 2015 9:54:55 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/pushQQMsgToFans")
	public QQErrorInfo pushQQMsgToFans(String appid, String appsecret, String openid){
		QQTokenHolder.setQQAppid(appid);
		QQTokenHolder.setQQAppsecret(appsecret);
//		//推图文消息
//		QQCustomNewsMsg.MPNews.Article article11 = new Article("", "", "", "欢迎访问玄玉博客", "玄玉博客是一个开放态度的Java生态圈", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
//		QQCustomNewsMsg.MPNews.Article article22 = new Article("", "", "", "玄玉微信SDK", "玄玉微信SDK是一个正在研发中的SDK", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
//		QQCustomNewsMsg customNewsMsg = new QQCustomNewsMsg(openid, new MPNews(new Article[]{article11, article22}));
//		return QQHelper.pushQQMsgToFans(QQTokenHolder.getQQAccessToken(), customNewsMsg);
		//推文本消息
		QQCustomTextMsg customTextMsg = new QQCustomTextMsg(openid, new Text("这是一条主动推给单个粉丝的测试消息"));
		return QQHelper.pushQQMsgToFans(QQTokenHolder.getQQAccessToken(), customTextMsg);
	}
}