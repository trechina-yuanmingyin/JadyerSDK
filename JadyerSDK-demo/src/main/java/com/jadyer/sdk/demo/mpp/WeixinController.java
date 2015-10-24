package com.jadyer.sdk.demo.mpp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.common.util.LogUtil;
import com.jadyer.sdk.demo.mpp.fans.FansInfoDao;
import com.jadyer.sdk.demo.mpp.fans.FansSaveThread;
import com.jadyer.sdk.demo.mpp.reply.ReplyInfoDao;
import com.jadyer.sdk.demo.mpp.reply.model.ReplyInfo;
import com.jadyer.sdk.demo.user.UserService;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.mpp.controller.WeixinMsgControllerCustomServiceAdapter;
import com.jadyer.sdk.mpp.msg.in.InTextMsg;
import com.jadyer.sdk.mpp.msg.in.event.InFollowEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InMenuEventMsg;
import com.jadyer.sdk.mpp.msg.out.OutCustomServiceMsg;
import com.jadyer.sdk.mpp.msg.out.OutMsg;
import com.jadyer.sdk.mpp.msg.out.OutTextMsg;

@Controller
@RequestMapping(value="/weixin")
public class WeixinController extends WeixinMsgControllerCustomServiceAdapter {
	@Resource
	private UserService userService;
	@Resource
	private FansInfoDao fansInfoDao;
	@Resource
	private ReplyInfoDao replyInfoDao;

	@Override
	protected OutMsg processInTextMsg(InTextMsg inTextMsg) {
		UserInfo userInfo = userService.findByWxId(inTextMsg.getToUserName());
		//没绑定就提示绑定
		if("0".equals(userInfo.getBindStatus()) && !Constants.WEIXIN_BIND_TEXT.equals(inTextMsg.getContent())){
			return new OutTextMsg(inTextMsg).setContent("账户未绑定\r请发送\"" + Constants.WEIXIN_BIND_TEXT + "\"绑定");
		}
		//绑定
		if("0".equals(userInfo.getBindStatus()) && Constants.WEIXIN_BIND_TEXT.equals(inTextMsg.getContent())){
			userInfo.setBindStatus("1");
			userService.save(userInfo);
			return new OutTextMsg(inTextMsg).setContent("绑定完毕，升级成功");
		}
		//关键字查找(暂时只支持回复文本或转发到多客服)
		ReplyInfo replyInfo = replyInfoDao.findByKeyword(userInfo.getId(), inTextMsg.getContent());
		if(null!=replyInfo && "0".equals(replyInfo.getType())){
			return new OutTextMsg(inTextMsg).setContent(replyInfo.getContent());
		}
		//查找通用的回复(暂时设定为转发到多客服)
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(userInfo.getId(), "0");
		if(!replyInfoList.isEmpty() && "4".equals(replyInfoList.get(0).getType())){
			return new OutCustomServiceMsg(inTextMsg);
		}
		//否则原样返回
		return new OutTextMsg(inTextMsg).setContent(inTextMsg.getContent());
	}


	@Override
	protected OutMsg processInMenuEventMsg(InMenuEventMsg inMenuEventMsg) {
		return new OutTextMsg(inMenuEventMsg).setContent("自定义菜单事件内容为" + inMenuEventMsg.getEventKey());
	}


	@Override
	protected OutMsg processInFollowEventMsg(InFollowEventMsg inFollowEventMsg) {
		UserInfo userInfo = userService.findByWxId(inFollowEventMsg.getToUserName());
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(userInfo.getId(), "1");
		if(InFollowEventMsg.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			//记录粉丝关注情况
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			threadPool.execute(new FansSaveThread(userInfo, inFollowEventMsg.getFromUserName()));
			threadPool.shutdown();
			//目前设定关注后回复文本
			if(replyInfoList.isEmpty()){
				return new OutTextMsg(inFollowEventMsg).setContent("感谢您的关注");
			}else{
				return new OutTextMsg(inFollowEventMsg).setContent(replyInfoList.get(0).getContent());
			}
		}
		if(InFollowEventMsg.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			fansInfoDao.updateSubscribe("0", userInfo.getId(), inFollowEventMsg.getFromUserName());
			LogUtil.getAppLogger().info("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
		}
		return new OutTextMsg(inFollowEventMsg).setContent("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
	}


//	@ResponseBody
//	@RequestMapping(value="/getWeixinAccessToken")
//	public String getWeixinAccessToken(){
//		String appid = "wx63ae5326e400cca2";
//		String appsecret = "b6a838ea12d6175c00793503500ede64";
//		return MPPUtil.getWeixinAccessToken(appid, appsecret);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/getFansInfo")
//	public FansInfo getFansInfo(){
//		String accesstoken = "axJDAkVIyi5SEYXTnHbWQZypbO0O8p5cDM5CdU0gs4loKMtpB3QCSuK4S6rfPebIArXx33Lmg_U5QoSLflUIHwKQnlGSsTlqhgFk-5RT2y0";
//		String openid = "o3SHot22_IqkUI7DpahNv-KBiFIs";
//		return MPPUtil.getFansInfo(accesstoken, openid);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/createMenu")
//	public ErrorInfo createMenu(){
//		String accesstoken = "axJDAkVIyi5SEYXTnHbWQZypbO0O8p5cDM5CdU0gs4loKMtpB3QCSuK4S6rfPebIArXx33Lmg_U5QoSLflUIHwKQnlGSsTlqhgFk-5RT2y0";
//		SubViewButton btn11 = new SubViewButton("我的博客", "http://blog.csdn.net/jadyer");
//		SubViewButton btn22 = new SubViewButton("我的GitHub", "http://jadyer.tunnel.mobi/weixin/getOpenid?oauth=base&openid=openid");
//		SubClickButton btn33 = new SubClickButton("历史上的今天", "history");
//		SubClickButton btn44 = new SubClickButton("天气预报", "weather");
//		SubClickButton btn55 = new SubClickButton("幽默笑话", "joke");
//		SuperButton sbtn11 = new SuperButton("个人中心", new Button[]{btn11, btn22});
//		SuperButton sbtn22 = new SuperButton("休闲驿站", new Button[]{btn33, btn44});
//		Menu menu = new Menu(new Button[]{sbtn11, btn55, sbtn22});
//		return MPPUtil.createMenu(accesstoken, menu);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/pushMsgToFans")
//	public ErrorInfo pushMsgToFans(){
//		String accesstoken = "axJDAkVIyi5SEYXTnHbWQZypbO0O8p5cDM5CdU0gs4loKMtpB3QCSuK4S6rfPebIArXx33Lmg_U5QoSLflUIHwKQnlGSsTlqhgFk-5RT2y0";
//		String openid = "o3SHot22_IqkUI7DpahNv-KBiFIs";
//		//推文本消息
//		CustomTextMsg customTextMsg = new CustomTextMsg(openid, new Text("[呲牙]SDK已发布，详见<a href=\"https://github.com/jadyer/JadyerSDK\">我的Github</a>[呲牙]"));
//		MPPUtil.pushMsgToFans(accesstoken, customTextMsg);
//		//推图文消息
//		CustomNewsMsg.News.Article article11 = new Article("欢迎访问玄玉博客", "玄玉博客是一个开放态度的Java生态圈", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
//		CustomNewsMsg.News.Article article22 = new Article("玄玉微信SDK", "玄玉微信SDK是一个正在研发中的SDK", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
//		CustomNewsMsg customNewsMsg = new CustomNewsMsg(openid, new News(new Article[]{article11, article22}));
//		return MPPUtil.pushMsgToFans(accesstoken, customNewsMsg);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/getOpenid", method=RequestMethod.GET)
//	public String getOpenid(String openid){
//		return openid;
//	}
//
//
//	@Override
//	protected OutMsg processInTextMsg(InTextMsg inTextMsg) {
//		//回复纯文本消息
//		if("hi".equals(inTextMsg.getContent())){
//			return new OutTextMsg(inTextMsg).setContent("hello");
//		}
//		//回复带链接和表情的文本消息
//		if("blog".equals(inTextMsg.getContent())){
//			return new OutTextMsg(inTextMsg).setContent("[右哼哼]欢迎访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[左哼哼]");
//		}
//		//回复多图文
//		OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
//		outMsg.addNews("第一个大图文标题", "第一个大图文描述", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
//		outMsg.addNews("第二个图文的标题", "第二个图文的描述", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
//		outMsg.addNews("第三个图文的标题", "第三个图文的描述", "http://img.my.csdn.net/uploads/201009/14/7892753_1284475095fyR0.jpg", "http://blog.csdn.net/jadyer/article/details/5859908");
//		return outMsg;
//	}
}