package com.jadyer.sdk.qq.msg.in;

/**
 * 接收文本消息
 * @create Nov 26, 2015 7:38:16 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQInTextMsg extends QQInMsg {
	/**
	 * 文本消息内容
	 */
	private String content;
	
	/**
	 * 64位整型的消息id
	 */
	private String msgId;

	public QQInTextMsg(String toUserName, String fromUserName, long createTime, String msgType) {
		super(toUserName, fromUserName, createTime, msgType);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}