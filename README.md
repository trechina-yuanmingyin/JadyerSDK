# JadyerSDK[![License](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/jadyer/JadyerSDK/blob/master/LICENSE)
个人研发的含微信SDK的SDK集<br/>

1.大家有问题可以提在这里https://github.com/jadyer/JadyerSDK/issues<br/>
2.SDK中mpp用于存放微信或QQ公众平台SDK，你可以理解为Media and Public Platform<br/>
3.使用时extends WeixinMsgController或者extends WeixinMsgControllerAdapter等适配器类<br/>
4.并保证Spring能够扫描到SDK包&lt;context:component-scan base-package="com.jadyer.sdk"/&gt;<br/>

# 集成方式
1.若微信H5客户端和后台管理的服务端是一个应用，那就不需要在web.xml中配置WeixinFilter<br/>
2.若二者分离（即目前流行的前后端分离），则前端需要在web.xml中配置WeixinFilter，后端不需要<br/>
3.上面提到的后端，对应的就是JadyerSDK-demo的Module<br/>

# 更新日志
[v1.1.1] 2015.12.23<br/>
1.优化WeixinFilter静默方式网页授权获取用户信息的流程，使得前端只需一个Filter不需引入SDK<br/>

[v1.1.0] 2015.11.26<br/>
1.增加QQ公众平台SDK，实现基本的收发文本消息、自定义菜单监听、关注与取消关注等功能<br/>

[v1.0.6] 2015.11.26<br/>
1.为下个版本增加的QQSDK而重命名并区分微信SDK，因为QQ公众平台最后一定会与微信的不同<br/>

[v1.0.5] 2015.11.19<br/>
1.修复1.0.4中微信媒体文件下载接口采用ResponseEntity<byte[]>返回流导致图片无法打开的问题<br/>

[v1.0.4] 2015.11.09<br/>
1.修复1.0.3版中走了弯路导致的若干BUG<br/>
2.TokenHolder引入java.util.concurrent.atomic.AtomicBoolean，保证更新Token过程中的旧Token可用<br/>
3.微信后台增加防伪标记，只有绑定到平台的公众号才提供服务，避免开发者URL被破译后，盗用服务<br/>

[v1.0.3] 2015.11.03<br/>
1.增加集成方式的简单描述<br/>
2.引入jssdk并编写了一个调用手机相机而不调用相册的例子<br/>
3.WeixinFilter增加dataurl参数，用于指定H5前端引入的SDK中TokenHolder数据的获取源<br/>
4.缓存微信access_token和网页授权access_token等生命周期较长的数据信息到TokenHolder中<br/>
5.合并微信相关Filter到一个WeixinFilter中，用于初始化appid和网页授权时回调的URL等预置数据<br/>

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