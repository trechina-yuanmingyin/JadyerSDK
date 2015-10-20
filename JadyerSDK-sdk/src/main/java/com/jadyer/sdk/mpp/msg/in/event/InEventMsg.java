package com.jadyer.sdk.mpp.msg.in.event;

import com.jadyer.sdk.mpp.msg.in.InMsg;

/**
 * 接收事件推送的公共类
 * @create Oct 18, 2015 5:25:34 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class InEventMsg extends InMsg {
	/**
	 * 事件类型
	 */
	protected String event;

	public InEventMsg(String toUserName, String fromUserName, long createTime, String msgType, String event) {
		super(toUserName, fromUserName, createTime, msgType);
		this.event = event;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}