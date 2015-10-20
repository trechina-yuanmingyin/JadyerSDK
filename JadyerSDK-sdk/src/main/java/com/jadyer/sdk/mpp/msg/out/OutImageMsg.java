package com.jadyer.sdk.mpp.msg.out;

import com.jadyer.sdk.mpp.msg.in.InMsg;

/**
 * 回复图片消息
 * @create Oct 18, 2015 2:21:40 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class OutImageMsg extends OutMsg {
	private String mediaId;

	public OutImageMsg(InMsg inMsg) {
		super(inMsg);
		this.msgType = "image";
	}

	public String getMediaId() {
		return mediaId;
	}

	public OutImageMsg setMediaId(String mediaId) {
		this.mediaId = mediaId;
		return this;
	}
}