package com.jadyer.sdk.weixin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadyer.sdk.weixin.msg.in.WeixinInImageMsg;
import com.jadyer.sdk.weixin.msg.in.WeixinInLinkMsg;
import com.jadyer.sdk.weixin.msg.in.WeixinInLocationMsg;
import com.jadyer.sdk.weixin.msg.in.WeixinInTextMsg;
import com.jadyer.sdk.weixin.msg.in.event.WeixinInCustomServiceEventMsg;
import com.jadyer.sdk.weixin.msg.in.event.WeixinInFollowEventMsg;
import com.jadyer.sdk.weixin.msg.in.event.WeixinInMenuEventMsg;
import com.jadyer.sdk.weixin.msg.out.WeixinOutImageMsg;
import com.jadyer.sdk.weixin.msg.out.WeixinOutMsg;
import com.jadyer.sdk.weixin.msg.out.WeixinOutTextMsg;

/**
 * 通用的微信消息Adapter
 * @see 对WeixinMsgController部分方法提供默认实现,以便开发者可以只关注需要处理的抽象方法
 * @create Oct 19, 2015 11:33:07 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class WeixinMsgControllerAdapter extends WeixinMsgController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected abstract WeixinOutMsg processInMenuEventMsg(WeixinInMenuEventMsg inMenuEventMsg);

	/**
	 * 处理收到的文本消息
	 * @see 默认原样返回
	 */
	@Override
	protected WeixinOutMsg processInTextMsg(WeixinInTextMsg inTextMsg) {
		return new WeixinOutTextMsg(inTextMsg).setContent(inTextMsg.getContent());
	}

	/**
	 * 处理收到的图片消息
	 * @see 默认原样返回
	 */
	@Override
	protected WeixinOutMsg processInImageMsg(WeixinInImageMsg inImageMsg) {
		return new WeixinOutImageMsg(inImageMsg).setMediaId(inImageMsg.getMediaId());
	}

	/**
	 * 处理收到的地址位置消息
	 * @see 默认返回粉丝地理位置的明文地址
	 */
	@Override
	protected WeixinOutMsg processInLocationMsg(WeixinInLocationMsg inLocationMsg) {
		return new WeixinOutTextMsg(inLocationMsg).setContent(inLocationMsg.getLabel());
	}

	/**
	 * 处理收到的链接消息
	 * @see 默认返回用户输入的链接
	 */
	@Override
	protected WeixinOutMsg processInLinkMsg(WeixinInLinkMsg inLinkMsg) {
		return new WeixinOutTextMsg(inLinkMsg).setContent("您的链接为<a href=\""+inLinkMsg.getUrl()+"\">"+inLinkMsg.getTitle()+"</a>");
	}

	/**
	 * 处理收到的关注/取消关注事件
	 * @see 关注时默认返回欢迎语"感谢您的关注"
	 * @see 取消专注时默认会打印取消关注的信息
	 */
	@Override
	protected WeixinOutMsg processInFollowEventMsg(WeixinInFollowEventMsg inFollowEventMsg){
		if(WeixinInFollowEventMsg.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			return new WeixinOutTextMsg(inFollowEventMsg).setContent("感谢您的关注");
		}
		if(WeixinInFollowEventMsg.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			logger.info("您的粉丝{}取消关注了您", inFollowEventMsg.getFromUserName());
		}
		return null;
	}

	/**
	 * 处理多客服接入会话/关闭会话/转接会话的事件
	 * @see 默认返回事件详情描述,无法识别事件时返回欢迎语
	 */
	@Override
	protected WeixinOutMsg processInCustomServiceEventMsg(WeixinInCustomServiceEventMsg inCustomServiceEventMsg){
		if(WeixinInCustomServiceEventMsg.EVENT_INCUSTOMSERVICE_KF_CREATE_SESSION.equals(inCustomServiceEventMsg.getEvent())){
			logger.info("客服{}接入了会话", inCustomServiceEventMsg.getKfAccount());
			return new WeixinOutTextMsg(inCustomServiceEventMsg).setContent("客服" + inCustomServiceEventMsg.getKfAccount() + "接入了会话");
		}
		if(WeixinInCustomServiceEventMsg.EVENT_INCUSTOMSERVICE_KF_CLOSE_SESSION.equals(inCustomServiceEventMsg.getEvent())){
			logger.info("客服{}关闭了会话", inCustomServiceEventMsg.getKfAccount());
			return new WeixinOutTextMsg(inCustomServiceEventMsg).setContent("客服" + inCustomServiceEventMsg.getKfAccount() + "关闭了会话");
		}
		if(WeixinInCustomServiceEventMsg.EVENT_INCUSTOMSERVICE_KF_SWITCH_SESSION.equals(inCustomServiceEventMsg.getEvent())){
			logger.info("客服{}将会话转接给了客服{}", inCustomServiceEventMsg.getKfAccount(), inCustomServiceEventMsg.getToKfAccount());
			return new WeixinOutTextMsg(inCustomServiceEventMsg).setContent("客服" + inCustomServiceEventMsg.getKfAccount() + "将会话转接给了客服" + inCustomServiceEventMsg.getToKfAccount());
		}
		return new WeixinOutTextMsg(inCustomServiceEventMsg).setContent("[右哼哼]欢迎访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[左哼哼]");
	}
}