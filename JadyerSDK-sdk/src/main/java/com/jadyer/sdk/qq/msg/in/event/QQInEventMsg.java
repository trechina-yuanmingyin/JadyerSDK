package com.jadyer.sdk.qq.msg.in.event;

import com.jadyer.sdk.qq.msg.in.QQInMsg;

/**
 * 接收事件推送的公共类
 * @create Nov 26, 2015 7:38:36 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class QQInEventMsg extends QQInMsg {
	/**
	 * 事件类型
	 */
	protected String event;

	public QQInEventMsg(String toUserName, String fromUserName, long createTime, String msgType, String event) {
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