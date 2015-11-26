package com.jadyer.sdk.demo.qq;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.demo.common.util.LogUtil;
import com.jadyer.sdk.qq.controller.QQMsgController;
import com.jadyer.sdk.qq.msg.in.QQInTextMsg;
import com.jadyer.sdk.qq.msg.in.event.QQInFollowEventMsg;
import com.jadyer.sdk.qq.msg.in.event.QQInMenuEventMsg;
import com.jadyer.sdk.qq.msg.out.QQOutMsg;
import com.jadyer.sdk.qq.msg.out.QQOutTextMsg;

@Controller
@RequestMapping(value="/qq")
public class QQController extends QQMsgController {
	@Override
	protected QQOutMsg processInTextMsg(QQInTextMsg inTextMsg) {
		//回复纯文本消息
		if("有一事请教".equals(inTextMsg.getContent())){
			return new QQOutTextMsg(inTextMsg).setContent("但说无妨");
		}
		if("兄台的趟泥步如此精纯，未知师从何处".equals(inTextMsg.getContent())){
			return new QQOutTextMsg(inTextMsg).setContent("家师不喜人闻，幸勿见怪");
		}
		if("小弟另有一套凌波微步".equals(inTextMsg.getContent())){
			return new QQOutTextMsg(inTextMsg).setContent("好，我等便在这步法上，证个高下");
		}
		return new QQOutTextMsg(inTextMsg).setContent("言毕，二人竟瞬息不见，步法之神令人叹绝。欲知后事如何，请访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[阴险]");
	}

	@Override
	protected QQOutMsg processInFollowEventMsg(QQInFollowEventMsg inFollowEventMsg) {
		if(QQInFollowEventMsg.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			return new QQOutTextMsg(inFollowEventMsg).setContent("欢迎关注武林百晓生，民国武术，尽在此间。");
		}
		if(QQInFollowEventMsg.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			LogUtil.getAppLogger().info("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
		}
		return new QQOutTextMsg(inFollowEventMsg).setContent("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
	}

	@Override
	protected QQOutMsg processInMenuEventMsg(QQInMenuEventMsg inMenuEventMsg) {
		//VIEW类的直接跳转过去了,CLICK类的暂定根据关键字回复
		if(QQInMenuEventMsg.EVENT_INMENU_CLICK.equals(inMenuEventMsg.getEvent())){
			return new QQOutTextMsg(inMenuEventMsg).setContent("您刚才点击了菜单：" + inMenuEventMsg.getEventKey());
		}
		//跳到URL时,这里也不会真的推送消息给用户
		return new QQOutTextMsg(inMenuEventMsg).setContent("您正在访问<a href=\""+inMenuEventMsg.getEventKey()+"\">"+inMenuEventMsg.getEventKey()+"</a>");
	}
}