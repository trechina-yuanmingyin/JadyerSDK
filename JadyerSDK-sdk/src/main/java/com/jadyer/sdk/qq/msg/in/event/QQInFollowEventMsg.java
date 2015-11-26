package com.jadyer.sdk.qq.msg.in.event;

/**
 * 接收关注/取消关注事件
 * @see -----------------------------------------------------------------------------------------------------------
 * @see 测试发现,关注和取消关注时,QQ服务器发送到开发者服务器的xml中都比官方文档多一个控制的EventKey元素
 * @see <xml>
 * @see 	<ToUserName><![CDATA[gh_4769d11d72e0]]></ToUserName>
 * @see 	<FromUserName><![CDATA[o3SHot22_IqkUI7DpahNv-KBiFIs]]></FromUserName>
 * @see 	<CreateTime>1445160792</CreateTime>
 * @see 	<MsgType><![CDATA[event]]></MsgType>
 * @see 	<Event><![CDATA[subscribe]]></Event>
 * @see 	<EventKey><![CDATA[]]></EventKey>
 * @see </xml>
 * @see <xml>
 * @see 	<ToUserName><![CDATA[gh_4769d11d72e0]]></ToUserName>
 * @see 	<FromUserName><![CDATA[o3SHot22_IqkUI7DpahNv-KBiFIs]]></FromUserName>
 * @see 	<CreateTime>1445160733</CreateTime>
 * @see 	<MsgType><![CDATA[event]]></MsgType>
 * @see 	<Event><![CDATA[unsubscribe]]></Event>
 * @see 	<EventKey><![CDATA[]]></EventKey>
 * @see </xml>
 * @see -----------------------------------------------------------------------------------------------------------
 * @create Nov 26, 2015 7:39:05 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQInFollowEventMsg extends QQInEventMsg {
	/**
	 * 关注事件
	 */
	public static final String EVENT_INFOLLOW_SUBSCRIBE = "subscribe";

	/**
	 * 取消关注事件
	 */
	public static final String EVENT_INFOLLOW_UNSUBSCRIBE = "unsubscribe";

	public QQInFollowEventMsg(String toUserName, String fromUserName, long createTime, String msgType, String event) {
		super(toUserName, fromUserName, createTime, msgType, event);
	}
}