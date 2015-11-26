package com.jadyer.sdk.qq.msg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.jadyer.sdk.qq.msg.out.QQOutMsg;
import com.jadyer.sdk.qq.msg.out.QQOutTextMsg;

/**
 * 回复QQ公众号消息的构建器
 * @create Nov 26, 2015 7:33:28 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQOutMsgXmlBuilder {
	private QQOutMsgXmlBuilder(){}
	
	public static void write(QQOutMsg outMsg, HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			out.write(build(outMsg));
			out.flush();
			out.close();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}


	public static String build(QQOutMsg outMsg){
		if(null == outMsg){
			throw new IllegalArgumentException("空的回复消息");
		}
		if("text".equals(outMsg.getMsgType())){
			return buildOutTextMsg((QQOutTextMsg)outMsg);
		}
		throw new RuntimeException("未知的消息类型" + outMsg.getMsgType() + ", 请查阅QQ公众平台开发者文档http://mp.qq.com/");
	}


	/**
	 * 回复文本消息
	 * @see -----------------------------------------------------------------------------------------------------------
	 * @see <xml>
	 * @see 	<ToUserName><![CDATA[toUser]]></ToUserName>
	 * @see 	<FromUserName><![CDATA[fromUser]]></FromUserName>
	 * @see 	<CreateTime>12345678</CreateTime>
	 * @see 	<MsgType><![CDATA[text]]></MsgType>
	 * @see 	<Content><![CDATA[你好]]></Content>
	 * @see </xml>
	 * @see -----------------------------------------------------------------------------------------------------------
	 */
	private static String buildOutTextMsg(QQOutTextMsg outTexgMsg){
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>")
		  .append("<ToUserName><![CDATA[").append(outTexgMsg.getToUserName()).append("]]></ToUserName>")
		  .append("<FromUserName><![CDATA[").append(outTexgMsg.getFromUserName()).append("]]></FromUserName>")
		  .append("<CreateTime>").append(outTexgMsg.getCreateTime()).append("</CreateTime>")
		  .append("<MsgType><![CDATA[").append(outTexgMsg.getMsgType()).append("]]></MsgType>")
		  .append("<Content><![CDATA[").append(outTexgMsg.getContent()).append("]]></Content>")
		  .append("</xml>");
		return sb.toString();
	}
}