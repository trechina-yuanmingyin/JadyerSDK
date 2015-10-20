package com.jadyer.sdk.mpp.msg.out;

import com.jadyer.sdk.mpp.msg.in.InMsg;

/**
 * 被动回复消息的公共类
 * @see 禁止SDK接入方使用此类
 * @create Oct 18, 2015 11:52:55 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class OutMsg {
	/**
	 * 接收方帐号(收到的OpenID)
	 */
	protected String toUserName;

	/**
	 * 开发者微信号
	 */
	protected String fromUserName;
	
	/**
	 * 消息创建时间
	 */
	protected long createTime;

	/**
	 * 被动回复消息的消息类型
	 */
	protected String msgType;

	public OutMsg() {}
	
	public OutMsg(InMsg inMsg) {
		this.toUserName = inMsg.getFromUserName();
		this.fromUserName = inMsg.getToUserName();
		this.createTime = (long)(System.currentTimeMillis()/1000);
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}