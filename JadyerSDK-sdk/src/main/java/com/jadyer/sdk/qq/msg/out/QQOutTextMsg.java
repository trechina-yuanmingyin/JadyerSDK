package com.jadyer.sdk.qq.msg.out;

import com.jadyer.sdk.qq.msg.in.QQInMsg;

/**
 * 回复文本消息
 * @create Nov 26, 2015 7:36:52 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQOutTextMsg extends QQOutMsg {
	private String content;

	public QQOutTextMsg(QQInMsg inMsg) {
		super(inMsg);
		this.msgType = "text";
	}

	public String getContent() {
		return content;
	}

	public QQOutTextMsg setContent(String content) {
		this.content = content;
		return this;
	}
}