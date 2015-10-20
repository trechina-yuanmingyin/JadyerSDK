package com.jadyer.sdk.mpp.msg.out;

import com.jadyer.sdk.mpp.msg.in.InMsg;

/**
 * 回复文本消息
 * @create Oct 18, 2015 2:09:02 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class OutTextMsg extends OutMsg {
	private String content;

	public OutTextMsg(InMsg inMsg) {
		super(inMsg);
		this.msgType = "text";
	}

	public String getContent() {
		return content;
	}
	
	public OutTextMsg setContent(String content) {
		this.content = content;
		return this;
	}
}