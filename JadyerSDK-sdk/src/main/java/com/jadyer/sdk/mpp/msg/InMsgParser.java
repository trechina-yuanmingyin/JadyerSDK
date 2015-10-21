package com.jadyer.sdk.mpp.msg;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jadyer.sdk.mpp.msg.in.InImageMsg;
import com.jadyer.sdk.mpp.msg.in.InLinkMsg;
import com.jadyer.sdk.mpp.msg.in.InLocationMsg;
import com.jadyer.sdk.mpp.msg.in.InMsg;
import com.jadyer.sdk.mpp.msg.in.InTextMsg;
import com.jadyer.sdk.mpp.msg.in.event.InCustomServiceEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InFollowEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InMenuEventMsg;

/**
 * 微信服务器请求消息解析器
 * @create Oct 18, 2015 12:53:31 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class InMsgParser{
	private InMsgParser(){}

	/**
	 * 解析微信服务器请求的xml报文体为com.jadyer.sdk.mpp.msg.in.InMsg对象
	 * @see 若无法识别请求的MsgType或解析xml出错,会抛出RuntimeException
	 * @create Oct 18, 2015 12:59:48 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static InMsg parse(String xml) {
		try {
			return doParse(xml);
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}


	private static InMsg doParse(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		String toUserName = root.elementText("ToUserName");
		String fromUserName = root.elementText("FromUserName");
		long createTime = Long.parseLong(root.elementText("CreateTime"));
		String msgType = root.elementText("MsgType");
		if("text".equals(msgType)){
			return parseInTextMsg(root, toUserName, fromUserName, createTime, msgType);
		}
		if("image".equals(msgType)){
			return parseInImageMsg(root, toUserName, fromUserName, createTime, msgType);
		}
		if("location".equals(msgType)){
			return parseInLocationMsg(root, toUserName, fromUserName, createTime, msgType);
		}
		if("link".equals(msgType)){
			return parseInLinkMsg(root, toUserName, fromUserName, createTime, msgType);
		}
		if("event".equals(msgType)){
			return parseInEventMsg(root, toUserName, fromUserName, createTime, msgType);
		}
		throw new RuntimeException("未知的消息类型" + msgType + ", 请查阅微信公众平台开发者文档http://mp.weixin.qq.com/wiki/home/index.html.");
	}


	private static InMsg parseInTextMsg(Element root, String toUserName, String fromUserName, long createTime, String msgType) {
		InTextMsg msg = new InTextMsg(toUserName, fromUserName, createTime, msgType);
		msg.setContent(root.elementText("Content"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}


	private static InMsg parseInImageMsg(Element root, String toUserName, String fromUserName, long createTime, String msgType) {
		InImageMsg msg = new InImageMsg(toUserName, fromUserName, createTime, msgType);
		msg.setPicUrl(root.elementText("PicUrl"));
		msg.setMediaId(root.elementText("MediaId"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}


	private static InMsg parseInLocationMsg(Element root, String toUserName, String fromUserName, long createTime, String msgType) {
		InLocationMsg msg = new InLocationMsg(toUserName, fromUserName, createTime, msgType);
		msg.setLocation_X(root.elementText("Location_X"));
		msg.setLocation_Y(root.elementText("Location_Y"));
		msg.setScale(root.elementText("Scale"));
		msg.setLabel(root.elementText("Label"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}


	private static InMsg parseInLinkMsg(Element root, String toUserName, String fromUserName, long createTime, String msgType) {
		InLinkMsg msg = new InLinkMsg(toUserName, fromUserName, createTime, msgType);
		msg.setTitle(root.elementText("Title"));
		msg.setDescription(root.elementText("Description"));
		msg.setUrl(root.elementText("Url"));
		msg.setMsgId(root.elementText("MsgId"));
		return msg;
	}


	private static InMsg parseInEventMsg(Element root, String toUserName, String fromUserName, long createTime, String msgType) {
		String event = root.elementText("Event");
		String eventKey = root.elementText("EventKey");
		//包括二维码扫描关注在内的关注/取消关注事件(二维码扫描关注事件与扫描带参数二维码事件是不一样的)
		if(("subscribe".equals(event) || "unsubscribe".equals(event)) && StringUtils.isBlank(eventKey)){
			return new InFollowEventMsg(toUserName, fromUserName, createTime, msgType, event);
		}
		//自定义菜单事件之点击菜单拉取消息时的事件推送
		if("CLICK".equals(event)){
			InMenuEventMsg e = new InMenuEventMsg(toUserName, fromUserName, createTime, msgType, event);
			e.setEventKey(eventKey);
			return e;
		}
		//自定义菜单事件之点击菜单跳转链接时的事件推送
		if("VIEW".equals(event)){
			InMenuEventMsg e = new InMenuEventMsg(toUserName, fromUserName, createTime, msgType, event);
			e.setEventKey(eventKey);
			return e;
		}
		//多客服接入会话事件
		if("kf_create_session".equals(event)){
			InCustomServiceEventMsg e = new InCustomServiceEventMsg(toUserName, fromUserName, createTime, msgType, event);
			e.setKfAccount(root.elementText("KfAccount"));
			return e;
		}
		//多客服关闭会话事件
		if("kf_close_session".equals(event)){
			InCustomServiceEventMsg e = new InCustomServiceEventMsg(toUserName, fromUserName, createTime, msgType, event);
			e.setKfAccount(root.elementText("KfAccount"));
			return e;
		}
		//多客服转接会话事件
		if("kf_switch_session".equals(event)){
			InCustomServiceEventMsg e = new InCustomServiceEventMsg(toUserName, fromUserName, createTime, msgType, event);
			e.setKfAccount(root.elementText("KfAccount"));
			e.setToKfAccount(root.elementText("ToKfAccount"));
			return e;
		}
		throw new RuntimeException("未知的事件类型" + event + ", 请查阅微信公众平台开发者文档http://mp.weixin.qq.com/wiki/home/index.html.");
	}
}