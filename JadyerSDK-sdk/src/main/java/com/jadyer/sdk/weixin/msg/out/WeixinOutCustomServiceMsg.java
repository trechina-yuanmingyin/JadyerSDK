package com.jadyer.sdk.weixin.msg.out;

import com.jadyer.sdk.weixin.msg.in.WeixinInMsg;

/**
 * 转发多客服消息
 * @create Oct 19, 2015 10:02:30 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class WeixinOutCustomServiceMsg extends WeixinOutMsg {
	public WeixinOutCustomServiceMsg(WeixinInMsg inMsg) {
		super(inMsg);
		this.msgType = "transfer_customer_service";
	}
}