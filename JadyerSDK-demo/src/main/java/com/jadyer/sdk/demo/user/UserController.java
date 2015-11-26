package com.jadyer.sdk.demo.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.jadyer.sdk.demo.common.base.CommonResult;
import com.jadyer.sdk.demo.common.constant.CodeEnum;
import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.weixin.helper.WeixinTokenHolder;
import com.jadyer.sdk.weixin.helper.WeixinHelper;
import com.jadyer.sdk.weixin.model.WeixinErrorInfo;

@Controller
@RequestMapping(value="/user")
public class UserController{
	@Resource
	private UserService userService;

	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping("/login/{username}/{password}")
	public CommonResult login(@PathVariable String username, @PathVariable String password, HttpServletRequest request){
		UserInfo userInfo = userService.findByUsernameAndPassword(username, password);
		if(null == userInfo){
			return new CommonResult(CodeEnum.SYSTEM_BUSY.getCode(), "无效的用户名或密码");
		}
		request.getSession().setAttribute(Constants.UID, userInfo.getId());
		request.getSession().setAttribute(Constants.USERINFO, userInfo);
		return new CommonResult();
	}


	/**
	 * 登出
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request){
		request.getSession().removeAttribute(Constants.USERINFO);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/login.jsp";
	}


	/**
	 * 平台用户信息
	 */
	@RequestMapping("/info")
	public String info(HttpServletRequest request){
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		//每次都取最新的
		userInfo = userService.findOne(userInfo.getId());
		//再更新HttpSession
		request.getSession().setAttribute(Constants.USERINFO, userInfo);
		//拼造开发者服务器的URL和Token
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme()).append("://").append(request.getServerName());
		if(80!=request.getServerPort() && 443!=request.getServerPort()){
			sb.append(":").append(request.getServerPort());
		}
		sb.append(request.getContextPath()).append("/");
		sb.append("weixin/").append(userInfo.getUuid());
		request.setAttribute("token", DigestUtils.md5Hex(userInfo.getUuid() + "http://blog.csdn.net/jadyer" + userInfo.getUuid()));
		request.setAttribute("weixinURL", sb.toString());
		/**
		 * 微信管理平台不需要配置WeixinFilter,这是因为它可以更换绑定的公众号,这样即便配置WeixinFilter也是无意义的
		 * 但也不能不初始化微信appid和appsecret,否则无法发布自定义菜单或主动推消息给粉丝等等
		 */
		if(StringUtils.isNotBlank(userInfo.getAppId())){
			WeixinTokenHolder.setWeixinAppid(userInfo.getAppId());
		}
		if(StringUtils.isNotBlank(userInfo.getAppSecret())){
			WeixinTokenHolder.setWeixinAppsecret(userInfo.getAppSecret());
		}
		return "user/userInfo";
	}


	/**
	 * 绑定公众号
	 * @see 即录库
	 */
	@ResponseBody
	@RequestMapping("/bind")
	public CommonResult bind(UserInfo userInfo, HttpServletRequest request){
		UserInfo _userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		userInfo.setPassword(_userInfo.getPassword());
		userInfo.setUsername(_userInfo.getUsername());
		userInfo.setParentId(_userInfo.getParentId());
		userInfo.setUuid(_userInfo.getUuid());
		userInfo.setAccessToken(_userInfo.getAccessToken());
		userInfo.setAccessTokenTime(_userInfo.getAccessTokenTime());
		request.getSession().setAttribute(Constants.USERINFO, userService.save(userInfo));
		//更换绑定的公众号,也要同步更新微信appid和appsecret
		WeixinTokenHolder.setWeixinAppid(userInfo.getAppId());
		WeixinTokenHolder.setWeixinAppsecret(userInfo.getAppSecret());
		return new CommonResult();
	}


	/**
	 * 修改密码
	 * @see 修改成功后要刷新HttpSession中的用户信息
	 */
	@ResponseBody
	@RequestMapping("/password/update")
	public CommonResult passwordUpdate(String oldPassword, String newPassword, HttpServletRequest request){
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		UserInfo respUserInfo = userService.passwordUpdate(userInfo, oldPassword, newPassword);
		if(null == respUserInfo){
			return new CommonResult(CodeEnum.SYSTEM_BUSY.getCode(), "原密码不正确");
		}
		request.getSession().setAttribute(Constants.USERINFO, respUserInfo);
		return new CommonResult();
	}


	/**
	 * 通过JSON的方式发布微信自定义菜单
	 * @param menuJson 微信自定义菜单数据的JSON串
	 */
	@ResponseBody
	@RequestMapping("/menu/weixin/create")
	public CommonResult menuWeixinCreate(String menuJson){
		WeixinErrorInfo errorInfo = WeixinHelper.createWeixinMenu(WeixinTokenHolder.getWeixinAccessToken(), menuJson);
		if(0 == errorInfo.getErrcode()){
			return new CommonResult();
		}else{
			return new CommonResult(errorInfo.getErrcode(), errorInfo.getErrmsg());
		}
	}
}