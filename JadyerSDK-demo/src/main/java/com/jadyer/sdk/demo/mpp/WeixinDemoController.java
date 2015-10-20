package com.jadyer.sdk.demo.mpp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.mpp.controller.WeixinMsgControllerAdapter;
import com.jadyer.sdk.mpp.model.ErrorInfo;
import com.jadyer.sdk.mpp.model.FansInfo;
import com.jadyer.sdk.mpp.model.custom.CustomNewsMsg;
import com.jadyer.sdk.mpp.model.custom.CustomNewsMsg.News;
import com.jadyer.sdk.mpp.model.custom.CustomNewsMsg.News.Article;
import com.jadyer.sdk.mpp.model.custom.CustomTextMsg;
import com.jadyer.sdk.mpp.model.custom.CustomTextMsg.Text;
import com.jadyer.sdk.mpp.model.menu.Button;
import com.jadyer.sdk.mpp.model.menu.Menu;
import com.jadyer.sdk.mpp.model.menu.SubClickButton;
import com.jadyer.sdk.mpp.model.menu.SubViewButton;
import com.jadyer.sdk.mpp.model.menu.SuperButton;
import com.jadyer.sdk.mpp.msg.in.InTextMsg;
import com.jadyer.sdk.mpp.msg.in.event.InMenuEventMsg;
import com.jadyer.sdk.mpp.msg.out.OutMsg;
import com.jadyer.sdk.mpp.msg.out.OutNewsMsg;
import com.jadyer.sdk.mpp.msg.out.OutTextMsg;
import com.jadyer.sdk.mpp.util.MPPUtil;

@Controller
@RequestMapping(value="/weixin")
public class WeixinDemoController extends WeixinMsgControllerAdapter {
	@ResponseBody
	@RequestMapping(value="/getWeixinAccessToken")
	public String getWeixinAccessToken(){
		String appid = "wx63ae5326e400cca2";
		String appsecret = "b6a838ea12d6175c00793503500ede64";
		return MPPUtil.getWeixinAccessToken(appid, appsecret);
	}


	@ResponseBody
	@RequestMapping(value="/getFansInfo")
	public FansInfo getFansInfo(){
		String accesstoken = "nHVQXjVPWlyvdglrU6EgGr7spBq2QW6IoW5a7UNDC5zye0Jzw9OeQgeM76FTc9D7ELvgm2s2v1UZ_cbtx6tGOLFd9WGFbS_zTI0Jp5D5GS4";
		String openid = "o3SHot22_IqkUI7DpahNv-KBiFIs";
		return MPPUtil.getFansInfo(accesstoken, openid);
	}


	@ResponseBody
	@RequestMapping(value="/createMenu")
	public ErrorInfo createMenu(){
		String accesstoken = "5fRyRK6LF0JAyU3kGKc17WZKjr3pRjjR3t_zvQlbD_aZzkoL5fF3ah5Q4wrzF2oDPMir-_j9f2GEHXtIZ-xqIT7kceai0E9ORvtkarFf3E8";
		SubViewButton btn11 = new SubViewButton("我的博客", "http://blog.csdn.net/jadyer");
		SubViewButton btn22 = new SubViewButton("我的GitHub", "http://jadyer.tunnel.mobi/weixin/getOpenid?oauth=base&openid=openid");
		SubClickButton btn33 = new SubClickButton("历史上的今天", "history");
		SubClickButton btn44 = new SubClickButton("天气预报", "weather");
		SubClickButton btn55 = new SubClickButton("幽默笑话", "joke");
		SuperButton sbtn11 = new SuperButton("个人中心", new Button[]{btn11, btn22});
		SuperButton sbtn22 = new SuperButton("休闲驿站", new Button[]{btn33, btn44});
		Menu menu = new Menu(new Button[]{sbtn11, btn55, sbtn22});
		return MPPUtil.createMenu(accesstoken, menu);
	}


	@ResponseBody
	@RequestMapping(value="/pushMsgToFans")
	public ErrorInfo pushMsgToFans(){
		String accesstoken = "nHVQXjVPWlyvdglrU6EgGr7spBq2QW6IoW5a7UNDC5zye0Jzw9OeQgeM76FTc9D7ELvgm2s2v1UZ_cbtx6tGOLFd9WGFbS_zTI0Jp5D5GS4";
		String openid = "o3SHot22_IqkUI7DpahNv-KBiFIs";
		//推文本消息
		CustomTextMsg customTextMsg = new CustomTextMsg(openid, new Text("明天发布微信SDK"));
		MPPUtil.pushMsgToFans(accesstoken, customTextMsg);
		//推图文消息
		CustomNewsMsg.News.Article article11 = new Article("欢迎访问玄玉博客", "玄玉博客是一个开放态度的Java生态圈", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
		CustomNewsMsg.News.Article article22 = new Article("玄玉微信SDK", "玄玉微信SDK是一个正在研发中的SDK", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
		CustomNewsMsg customNewsMsg = new CustomNewsMsg(openid, new News(new Article[]{article11, article22}));
		return MPPUtil.pushMsgToFans(accesstoken, customNewsMsg);
	}


	@ResponseBody
	@RequestMapping(value="/getOpenid", method=RequestMethod.GET)
	public String getOpenid(String openid){
		return openid;
	}


	@Override
	protected OutMsg processInTextMsg(InTextMsg inTextMsg) {
		//回复纯文本消息
		if("hi".equals(inTextMsg.getContent())){
			return new OutTextMsg(inTextMsg).setContent("hello");
		}
		//回复带链接和表情的文本消息
		if("blog".equals(inTextMsg.getContent())){
			return new OutTextMsg(inTextMsg).setContent("[右哼哼]欢迎访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[左哼哼]");
		}
		//回复多图文
		OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
		outMsg.addNews("第一个大图文标题", "第一个大图文描述", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
		outMsg.addNews("第二个图文的标题", "第二个图文的描述", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
		outMsg.addNews("第三个图文的标题", "第三个图文的描述", "http://img.my.csdn.net/uploads/201009/14/7892753_1284475095fyR0.jpg", "http://blog.csdn.net/jadyer/article/details/5859908");
		return outMsg;
	}


	@Override
	protected OutMsg processInMenuEventMsg(InMenuEventMsg inMenuEventMsg) {
		return new OutTextMsg(inMenuEventMsg).setContent("自定义菜单事件内容为" + inMenuEventMsg.getEventKey());
	}
}