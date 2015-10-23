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
			return new OutTextMsg(inTextMsg).setContent("账户未绑定<br/>请发送：\"" + Constants.WEIXIN_BIND_TEXT + "\"完成绑定");
		}
		//绑定(由于当前登录的用户未必时本次绑定的用户,所以没有考虑更新Session中的用户信息)
		if("0".equals(userInfo.getBindStatus()) && Constants.WEIXIN_BIND_TEXT.equals(inTextMsg.getContent())){
			userInfo.setBindStatus("1");
			userService.save(userInfo);
			return new OutTextMsg(inTextMsg).setContent("升级成功");
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
		return null;
	}
}