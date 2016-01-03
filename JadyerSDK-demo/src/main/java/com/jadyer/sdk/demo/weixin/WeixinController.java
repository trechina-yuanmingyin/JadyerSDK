package com.jadyer.sdk.demo.weixin;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.common.util.LogUtil;
import com.jadyer.sdk.demo.user.UserService;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.demo.weixin.fans.FansInfoDao;
import com.jadyer.sdk.demo.weixin.fans.FansSaveThread;
import com.jadyer.sdk.demo.weixin.reply.ReplyInfoDao;
import com.jadyer.sdk.demo.weixin.reply.model.ReplyInfo;
import com.jadyer.sdk.qq.helper.QQTokenHolder;
import com.jadyer.sdk.weixin.controller.WeixinMsgControllerCustomServiceAdapter;
import com.jadyer.sdk.weixin.helper.WeixinTokenHolder;
import com.jadyer.sdk.weixin.msg.in.WeixinInTextMsg;
import com.jadyer.sdk.weixin.msg.in.event.WeixinInFollowEventMsg;
import com.jadyer.sdk.weixin.msg.in.event.WeixinInMenuEventMsg;
import com.jadyer.sdk.weixin.msg.out.WeixinOutCustomServiceMsg;
import com.jadyer.sdk.weixin.msg.out.WeixinOutMsg;
import com.jadyer.sdk.weixin.msg.out.WeixinOutTextMsg;

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
	protected WeixinOutMsg processInTextMsg(WeixinInTextMsg inTextMsg) {
//		//回复带链接和表情的文本消息
//		if("blog".equals(inTextMsg.getContent())){
//			return new WeixinOutTextMsg(inTextMsg).setContent("[右哼哼]欢迎访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[左哼哼]");
//		}
//		//回复多图文
//		WeixinOutNewsMsg outMsg = new WeixinOutNewsMsg(inTextMsg);
//		outMsg.addNews("第一个大图文标题", "第一个大图文描述", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
//		outMsg.addNews("第二个图文的标题", "第二个图文的描述", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
//		outMsg.addNews("第三个图文的标题", "第三个图文的描述", "http://img.my.csdn.net/uploads/201009/14/7892753_1284475095fyR0.jpg", "http://blog.csdn.net/jadyer/article/details/5859908");
//		return outMsg;
		//防伪
		UserInfo userInfo = userService.findByWxid(inTextMsg.getToUserName());
		if(null == userInfo){
			return new WeixinOutTextMsg(inTextMsg).setContent("该公众号未绑定");
		}
		//没绑定就提示绑定
		if("0".equals(userInfo.getBindStatus()) && !Constants.MPP_BIND_TEXT.equals(inTextMsg.getContent())){
			return new WeixinOutTextMsg(inTextMsg).setContent("账户未绑定\r请发送\"" + Constants.MPP_BIND_TEXT + "\"绑定");
		}
		//绑定
		if("0".equals(userInfo.getBindStatus()) && Constants.MPP_BIND_TEXT.equals(inTextMsg.getContent())){
			userInfo.setBindStatus("1");
			userInfo.setBindTime(new Date());
			userService.save(userInfo);
			return new WeixinOutTextMsg(inTextMsg).setContent("绑定完毕，升级成功");
		}
		//关键字查找(暂时只支持回复文本或转发到多客服)
		ReplyInfo replyInfo = replyInfoDao.findByKeyword(userInfo.getId(), inTextMsg.getContent());
		if(null!=replyInfo && "0".equals(replyInfo.getType())){
			return new WeixinOutTextMsg(inTextMsg).setContent(replyInfo.getContent());
		}
		//查找通用的回复(暂时设定为转发到多客服)
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(userInfo.getId(), "0");
		if(!replyInfoList.isEmpty() && "4".equals(replyInfoList.get(0).getType())){
			return new WeixinOutCustomServiceMsg(inTextMsg);
		}
		//否则原样返回
		return new WeixinOutTextMsg(inTextMsg).setContent(inTextMsg.getContent());
	}


	@Override
	protected WeixinOutMsg processInMenuEventMsg(WeixinInMenuEventMsg inMenuEventMsg) {
		//防伪
		UserInfo userInfo = userService.findByWxid(inMenuEventMsg.getToUserName());
		if(null == userInfo){
			return new WeixinOutTextMsg(inMenuEventMsg).setContent("该公众号未绑定");
		}
		//VIEW类的直接跳转过去了,CLICK类的暂定根据关键字回复(找不到关键字就转发到多客服)
		if(WeixinInMenuEventMsg.EVENT_INMENU_CLICK.equals(inMenuEventMsg.getEvent())){
			ReplyInfo replyInfo = replyInfoDao.findByKeyword(userInfo.getId(), inMenuEventMsg.getEventKey());
			if(null == replyInfo){
				return new WeixinOutCustomServiceMsg(inMenuEventMsg);
			}else{
				return new WeixinOutTextMsg(inMenuEventMsg).setContent(replyInfo.getContent());
			}
		}
		//跳到URL时,这里也不会真的推送消息给用户
		return new WeixinOutTextMsg(inMenuEventMsg).setContent("您正在访问<a href=\""+inMenuEventMsg.getEventKey()+"\">"+inMenuEventMsg.getEventKey()+"</a>");
	}


	@Override
	protected WeixinOutMsg processInFollowEventMsg(WeixinInFollowEventMsg inFollowEventMsg) {
		//防伪
		UserInfo userInfo = userService.findByWxid(inFollowEventMsg.getToUserName());
		if(null == userInfo){
			return new WeixinOutTextMsg(inFollowEventMsg).setContent("该公众号未绑定");
		}
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(userInfo.getId(), "1");
		if(WeixinInFollowEventMsg.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			//记录粉丝关注情况
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			threadPool.execute(new FansSaveThread(userInfo, inFollowEventMsg.getFromUserName()));
			threadPool.shutdown();
			//目前设定关注后回复文本
			if(replyInfoList.isEmpty()){
				return new WeixinOutTextMsg(inFollowEventMsg).setContent("感谢您的关注");
			}else{
				return new WeixinOutTextMsg(inFollowEventMsg).setContent(replyInfoList.get(0).getContent());
			}
		}
		if(WeixinInFollowEventMsg.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			fansInfoDao.updateSubscribe("0", userInfo.getId(), inFollowEventMsg.getFromUserName());
			LogUtil.getAppLogger().info("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
		}
		return new WeixinOutTextMsg(inFollowEventMsg).setContent("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
	}


	@ResponseBody
	@RequestMapping(value="/getOpenid", method=RequestMethod.GET)
	public String getOpenid(String openid){
		return openid;
	}


//	@ResponseBody
//	@RequestMapping(value="/getWeixinAccessToken")
//	public String getWeixinAccessToken(){
//		String appid = "wx63ae5326e400cca2";
//		String appsecret = "b6a838ea12d6175c00793503500ede64";
//		return WeixinHelper.getWeixinAccessToken(appid, appsecret);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/getWeixinFansInfo")
//	public FansInfo getWeixinFansInfo(){
//		String accesstoken = "axJDAkVIyi5SEYXTnHbWQZypbO0O8p5cDM5CdU0gs4loKMtpB3QCSuK4S6rfPebIArXx33Lmg_U5QoSLflUIHwKQnlGSsTlqhgFk-5RT2y0";
//		String openid = "o3SHot22_IqkUI7DpahNv-KBiFIs";
//		return WeixinHelper.getWeixinFansInfo(accesstoken, openid);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/createWeixinMenu")
//	public ErrorInfo createWeixinMenu(){
//		String accesstoken = "nHVQXjVPWlyvdglrU6EgGnH_MzvdltddS4HOzUJocjX-wb_NVOi-6rJjumZJayRqwHT7xx80ziBaDCXc6dqddVHheP7g6aJAxv71Lwj3Cxg";
//		WeixinSubViewButton btn11 = new WeixinSubViewButton("我的博客", "http://blog.csdn.net/jadyer");
//		WeixinSubViewButton btn22 = new WeixinSubViewButton("我的GitHub", "http://jadyer.tunnel.mobi/weixin/getOpenid?oauth=base&openid=openid");
//		WeixinSubClickButton btn33 = new WeixinSubClickButton("历史上的今天", "123abc");
//		WeixinSubClickButton btn44 = new WeixinSubClickButton("天气预报", "456");
//		WeixinSubClickButton btn55 = new WeixinSubClickButton("幽默笑话", "joke");
//		WeixinSuperButton sbtn11 = new WeixinSuperButton("个人中心", new WeixinButton[]{btn11, btn22});
//		WeixinSuperButton sbtn22 = new WeixinSuperButton("休闲驿站", new WeixinButton[]{btn33, btn44});
//		WeixinMenu menu = new WeixinMenu(new WeixinButton[]{sbtn11, btn55, sbtn22});
//		return WeixinHelper.createWeixinMenu(accesstoken, menu);
//	}
//
//
//	@ResponseBody
//	@RequestMapping(value="/pushWeixinMsgToFans")
//	public ErrorInfo pushWeixinMsgToFans(){
//		String accesstoken = "axJDAkVIyi5SEYXTnHbWQZypbO0O8p5cDM5CdU0gs4loKMtpB3QCSuK4S6rfPebIArXx33Lmg_U5QoSLflUIHwKQnlGSsTlqhgFk-5RT2y0";
//		String openid = "o3SHot22_IqkUI7DpahNv-KBiFIs";
//		//推文本消息
//		WeixinCustomTextMsg customTextMsg = new WeixinCustomTextMsg(openid, new Text("[呲牙]SDK已发布，详见<a href=\"https://github.com/jadyer/JadyerSDK\">我的Github</a>[呲牙]"));
//		WeixinHelper.pushWeixinMsgToFans(accesstoken, customTextMsg);
//		//推图文消息
//		WeixinCustomNewsMsg.News.Article article11 = new Article("欢迎访问玄玉博客", "玄玉博客是一个开放态度的Java生态圈", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
//		WeixinCustomNewsMsg.News.Article article22 = new Article("玄玉微信SDK", "玄玉微信SDK是一个正在研发中的SDK", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
//		WeixinCustomNewsMsg customNewsMsg = new WeixinCustomNewsMsg(openid, new News(new Article[]{article11, article22}));
//		return WeixinHelper.pushWeixinMsgToFans(accesstoken, customNewsMsg);
//	}


	@PostConstruct
	public void scheduleReport(){
		Executors.newScheduledThreadPool(1).schedule(new Runnable(){
			@Override
			public void run() {
				try {
					List<UserInfo> userinfoList = userService.findAll();
					for(UserInfo obj : userinfoList){
						if("1".equals(obj.getBindStatus())){
							if("1".equals(obj.getMptype())){
								WeixinTokenHolder.setWeixinAppidAppsecret(obj.getAppid(), obj.getAppsecret());
							}
							if("2".equals(obj.getMptype())){
								QQTokenHolder.setQQAppidAppsecret(obj.getAppid(), obj.getAppsecret());
							}
						}
					}
				} catch (Exception e) {
					LogUtil.getAppLogger().info("登记微信appid和appsecret任务异常, 堆栈轨迹如下", e);
				}
			}
		}, 2, TimeUnit.MINUTES);
	}
}