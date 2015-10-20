package com.jadyer.sdk.mpp.controller;

import com.jadyer.sdk.mpp.msg.in.InImageMsg;
import com.jadyer.sdk.mpp.msg.in.InLinkMsg;
import com.jadyer.sdk.mpp.msg.in.InLocationMsg;
import com.jadyer.sdk.mpp.msg.in.InTextMsg;
import com.jadyer.sdk.mpp.msg.in.event.InMenuEventMsg;
import com.jadyer.sdk.mpp.msg.out.OutCustomServiceMsg;
import com.jadyer.sdk.mpp.msg.out.OutMsg;

/**
 * 用于将消息转发到多客服的Adapter
 * @see 对WeixinMsgController部分方法提供默认实现,以便开发者可以只关注需要处理的抽象方法
 * @create Oct 19, 2015 10:56:56 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class WeixinMsgControllerCustomServiceAdapter extends WeixinMsgControllerAdapter {
	@Override
	protected abstract OutMsg processInMenuEventMsg(InMenuEventMsg inMenuEventMsg);

	/**
	 * 处理收到的文本消息
	 * @see 默认将消息转发到多客服
	 */
	@Override
	protected OutMsg processInTextMsg(InTextMsg inTextMsg) {
		return new OutCustomServiceMsg(inTextMsg);
	}

	/**
	 * 处理收到的图片消息
	 * @see 默认将消息转发到多客服
	 */
	@Override
	protected OutMsg processInImageMsg(InImageMsg inImageMsg) {
		return new OutCustomServiceMsg(inImageMsg);
	}

	/**
	 * 处理收到的地址位置消息
	 * @see 默认将消息转发到多客服
	 */
	@Override
	protected OutMsg processInLocationMsg(InLocationMsg inLocationMsg) {
		return new OutCustomServiceMsg(inLocationMsg);
	}

	/**
	 * 处理收到的链接消息
	 * @see 默认将消息转发到多客服
	 */
	@Override
	protected OutMsg processInLinkMsg(InLinkMsg inLinkMsg) {
		return new OutCustomServiceMsg(inLinkMsg);
	}
}