package com.jadyer.sdk.demo.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.jadyer.sdk.demo.common.base.CommonResult;
import com.jadyer.sdk.demo.common.constant.CodeEnum;
import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.user.model.UserInfo;

@Controller
@RequestMapping(value="/user")
public class UserController{
	@Resource
	private UserService userService;

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

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request){
		request.getSession().removeAttribute(Constants.USERINFO);
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/login.jsp";
	}

	@RequestMapping("/info")
	public String info(){
		return "userInfo";
	}

	/**
	 * 前往微信绑定页面
	 */
	@RequestMapping("/tobind")
	public String tobind(HttpServletRequest request){
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme()).append("://").append(request.getServerName());
		if(80!=request.getServerPort() && 443!=request.getServerPort()){
			sb.append(":").append(request.getServerPort());
		}
		sb.append(request.getContextPath()).append("/");
		sb.append("weixin/").append(userInfo.getUuid());
		request.setAttribute("token", DigestUtils.md5Hex(userInfo.getUuid() + "http://blog.csdn.net/jadyer" + userInfo.getUuid()));
		request.setAttribute("weixinURL", sb.toString());
		return "userBind";
	}

	/**
	 * 绑定(即录库)
	 */
	@RequestMapping("/bind")
	public String bind(UserInfo userInfo, HttpServletRequest request){
		UserInfo _userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		userInfo.setPassword(_userInfo.getPassword());
		userInfo.setUsername(_userInfo.getUsername());
		userInfo.setParentId(_userInfo.getParentId());
		userInfo.setUuid(_userInfo.getUuid());
		request.getSession().setAttribute(Constants.USERINFO, userService.save(userInfo));
		return "userBindConfirm";
	}
}