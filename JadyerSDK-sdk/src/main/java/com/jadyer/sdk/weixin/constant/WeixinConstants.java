package com.jadyer.sdk.weixin.constant;

public interface WeixinConstants {
	/**
	 * 网页授权获取用户信息的方式
	 */
	String WEIXIN_OAUTH_SCOPE_SNSAPI_BASE     = "snsapi_base";
	String WEIXIN_OAUTH_SCOPE_SNSAPI_USERINFO = "snsapi_userinfo";

	/**
	 * URL属性中的占位符
	 */
	String URL_PLACEHOLDER_APPID        = "{appid}";
	String URL_PLACEHOLDER_APPSECRET    = "{appsecret}";
	String URL_PLACEHOLDER_ACCESSTOKEN  = "{accesstoken}";
	String URL_PLACEHOLDER_OPENID       = "{openid}";
	String URL_PLACEHOLDER_REDIRECT_URI = "{redirecturi}";
	String URL_PLACEHOLDER_SCOPE        = "{scope}";
	String URL_PLACEHOLDER_STATE        = "{state}";
	String URL_PLACEHOLDER_CODE         = "{code}";
	String URL_PLACEHOLDER_MEDIAID      = "{mediaid}";

	/**
	 * 微信URL
	 */
	//获取access token
	String URL_WEIXIN_GET_ACCESSTOKEN        = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + URL_PLACEHOLDER_APPID + "&secret=" + URL_PLACEHOLDER_APPSECRET;
	//获取微信服务器IP地址
	String URL_WEIXIN_GET_WEIXINIP_LIST      = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + URL_PLACEHOLDER_ACCESSTOKEN;
	//向微信服务器新增永久图片(image)素材
	String URL_WEIXIN_GET_ADD_FOREVER_IMAGE  = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + URL_PLACEHOLDER_ACCESSTOKEN;
	//获取用户基本信息
	String URL_WEIXIN_GET_FANSINFO           = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&openid=" + URL_PLACEHOLDER_OPENID + "&access_token=" + URL_PLACEHOLDER_ACCESSTOKEN;
	//自定义菜单之创建
	String URL_WEIXIN_GET_CREATE_MENU        = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + URL_PLACEHOLDER_ACCESSTOKEN;
	//客服接口主动推消息
	String URL_WEIXIN_CUSTOM_PUSH_MESSAGE    = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + URL_PLACEHOLDER_ACCESSTOKEN;
	//网页授权获取用户信息的Code地址
	String URL_WEIXIN_OAUTH2_GET_CODE        = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + URL_PLACEHOLDER_APPID + "&redirect_uri=" + URL_PLACEHOLDER_REDIRECT_URI + "&response_type=code&scope=" + URL_PLACEHOLDER_SCOPE + "&state=" + URL_PLACEHOLDER_STATE + "#wechat_redirect";
	//通过code换取网页授权access_token
	String URL_WEIXIN_OAUTH2_GET_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + URL_PLACEHOLDER_APPID + "&secret=" + URL_PLACEHOLDER_APPSECRET +"&code=" + URL_PLACEHOLDER_CODE + "&grant_type=authorization_code";
	//获取微信jsapi_ticket
	String URL_WEIXIN_GET_JSAPI_TICKET       = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + URL_PLACEHOLDER_ACCESSTOKEN + "&type=jsapi";
	//获取微信临时素材
	String URL_WEIXIN_GET_TEMP_MEDIA_FILE    = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + URL_PLACEHOLDER_ACCESSTOKEN + "&media_id=" + URL_PLACEHOLDER_MEDIAID;
}