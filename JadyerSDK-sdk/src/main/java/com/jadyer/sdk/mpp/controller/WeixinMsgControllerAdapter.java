package com.jadyer.sdk.mpp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadyer.sdk.mpp.msg.in.InImageMsg;
import com.jadyer.sdk.mpp.msg.in.InLinkMsg;
import com.jadyer.sdk.mpp.msg.in.InLocationMsg;
import com.jadyer.sdk.mpp.msg.in.InTextMsg;
import com.jadyer.sdk.mpp.msg.in.event.InCustomServiceEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InFollowEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InMenuEventMsg;
import com.jadyer.sdk.mpp.msg.out.OutImageMsg;
import com.jadyer.sdk.mpp.msg.out.OutMsg;
import com.jadyer.sdk.mpp.msg.out.OutTextMsg;

/**
 * 通用的微信消息Adapter
 * @see 对WeixinMsgController部分方法提供默认实现,以便开发者可以只关注需要处理的抽象方法
 * @create Oct 19, 2015 11:33:07 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class WeixinMsgControllerAdapter extends WeixinMsgController {
	private static final Logger logger = LoggerFactory.getLogger(WeixinMsgControllerAdapter.class);

	@Override
	protected abstract OutMsg processInMenuEventMsg(InMenuEventMsg inMenuEventMsg);

	/**
	 * 处理收到的文本消息
	 * @see 默认原样返回
	 */
	@Override
	protected OutMsg processInTextMsg(InTextMsg inTextMsg) {
		return new OutTextMsg(inTextMsg).setContent(inTextMsg.getContent());
	}

	/**
	 * 处理收到的图片消息
	 * @see 默认原样返回
	 */
	@Override
	protected OutMsg processInImageMsg(InImageMsg inImageMsg) {
		return new OutImageMsg(inImageMsg).setMediaId(inImageMsg.getMediaId());
	}

	/**
	 * 处理收到的地址位置消息
	 * @see 默认返回粉丝地理位置的明文地址
	 */
	@Override
	protected OutMsg processInLocationMsg(InLocationMsg inLocationMsg) {
		return new OutTextMsg(inLocationMsg).setContent(inLocationMsg.getLabel());
	}

	/**
	 * 处理收到的链接消息
	 * @see 默认返回用户输入的链接
	 */
	@Override
	protected OutMsg processInLinkMsg(InLinkMsg inLinkMsg) {
		return new OutTextMsg(inLinkMsg).setContent("您的链接为<a href=\""+inLinkMsg.getUrl()+"\">"+inLinkMsg.getTitle()+"</a>");
	}

	/**
	 * 处理收到的关注/取消关注事件
	 * @see 关注时默认返回欢迎语"感谢您的关注"
	 * @see 取消专注时默认会打印取消关注的信息
	 */
	@Override
	protected OutMsg processInFollowEventMsg(InFollowEventMsg inFollowEventMsg){
		if(InFollowEventMsg.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			return new OutTextMsg(inFollowEventMsg).setContent("感谢您的关注");
		}
		if(InFollowEventMsg.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			logger.info("您的粉丝{}取消关注了您", inFollowEventMsg.getFromUserName());
		}
		return null;
	}

	/**
	 * 处理多客服接入会话/关闭会话/转接会话的事件
	 * @see 默认返回事件详情描述,无法识别事件时返回欢迎语
	 */
	@Override
	protected OutMsg processInCustomServiceEventMsg(InCustomServiceEventMsg inCustomServiceEventMsg){
		if(InCustomServiceEventMsg.EVENT_INCUSTOMSERVICE_KF_CREATE_SESSION.equals(inCustomServiceEventMsg.getEvent())){
			return new OutTextMsg(inCustomServiceEventMsg).setContent("客服" + inCustomServiceEventMsg.getKfAccount() + "接入了会话");
		}
		if(InCustomServiceEventMsg.EVENT_INCUSTOMSERVICE_KF_CLOSE_SESSION.equals(inCustomServiceEventMsg.getEvent())){
			return new OutTextMsg(inCustomServiceEventMsg).setContent("客服" + inCustomServiceEventMsg.getKfAccount() + "关闭了会话");
		}
		if(InCustomServiceEventMsg.EVENT_INCUSTOMSERVICE_KF_SWITCH_SESSION.equals(inCustomServiceEventMsg.getEvent())){
			return new OutTextMsg(inCustomServiceEventMsg).setContent("客服" + inCustomServiceEventMsg.getKfAccount() + "将会话转接给了客服" + inCustomServiceEventMsg.getToKfAccount());
		}
		return new OutTextMsg(inCustomServiceEventMsg).setContent("[右哼哼]欢迎访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[左哼哼]");
	}
}