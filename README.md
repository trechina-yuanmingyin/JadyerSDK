# JadyerSDK[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/jadyer/JadyerSDK/blob/master/LICENSE)
个人研发的含微信SDK的SDK集<br/>

1.大家有问题可以提在这里https://github.com/jadyer/JadyerSDK/issues<br/>
2.SDK中mpp用于存放微信或QQ公众平台SDK，你可以理解为Media and Public Platform<br/>
3.使用时extends WeixinMsgController或者extends WeixinMsgControllerAdapter等适配器类<br/>
4.并保证Spring能够扫描到SDK包&lt;context:component-scan base-package="com.jadyer.sdk"/&gt;<br/>

# 更新日志
[v1.0.2] 2015.10.24<br/>
1.demo中增加后台管理模块，用于管理和发布微信功能<br/>

[v1.0.1] 2015.10.20<br/>
1.增加名为demo的Moudle用于演示微信SDK的便捷用法<br/>
2.下一个版本准备在demo中增加一个后台管理模块<br/>
3.再下一个版本准备增加QQ公众平台SDK<br/>
4.再再下一个版本准备支持多用户<br/>

[v1.0.0] 2015.10.19<br/>
1.花了一个礼拜天和一个周一共2天时间开发完微信SDK，含以下功能：<br/>
2.处理接收到的文本消息、图片消息、地址位置消息、链接消息、关注&nbsp;<b>/</b>&nbsp;取消关注事件<br/>
3.处理自定义菜单拉取消息/跳转链接事件、多客服接入会话&nbsp;<b>/</b>&nbsp;关闭会话&nbsp;<b>/</b>&nbsp;转接会话事件<br/>
4.可自定义回复图片、多图文、纯文本、带表情的文本，或将消息转发给多客服客户端<br/>
5.可主动拉去指定的粉丝信息、推消息给粉丝（48小时内有过交互）、创建自定义菜单<br/>
6.可通过网页授权静默获取粉丝openid（web.xml配置一个Filter即可，不需要其它代码）