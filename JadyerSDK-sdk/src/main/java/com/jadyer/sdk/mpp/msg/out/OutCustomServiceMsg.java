package com.jadyer.sdk.mpp.msg.out;

import com.jadyer.sdk.mpp.msg.in.InMsg;

/**
 * 转发多客服消息
 * @create Oct 19, 2015 10:02:30 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class OutCustomServiceMsg extends OutMsg {
	public OutCustomServiceMsg(InMsg inMsg) {
		super(inMsg);
		this.msgType = "transfer_customer_service";
	}
}