package com.jadyer.sdk.demo.user;

import java.util.List;

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
import com.jadyer.sdk.demo.user.model.MenuInfo;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.mpp.model.ErrorInfo;
import com.jadyer.sdk.mpp.util.MPPUtil;

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
		return "user/userInfo";
	}

	@RequestMapping("/password")
	public String password(){
		return "user/password";
	}

	@ResponseBody
	@RequestMapping("/password/update")
	public CommonResult passwordUpdate(String oldPassword, String newPassword, HttpServletRequest request){
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		UserInfo respUserInfo = userService.passwordUpdate(userInfo, oldPassword, newPassword);
		if(null == respUserInfo){
			return new CommonResult(CodeEnum.SYSTEM_BUSY.getCode(), "原密码不正确");
		}
		//刷新HttpSession中的用户信息
		request.getSession().setAttribute(Constants.USERINFO, respUserInfo);
		return new CommonResult();
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
		return "user/userBind";
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
		return "user/userBindConfirm";
	}

	/**
	 * 前往微信菜单页面
	 */
	@RequestMapping("/tomenu/weixin")
	public String tomenuWeixin(HttpServletRequest request){
		int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		List<MenuInfo> menuList = userService.findMenuList(uid);
		request.setAttribute("menuList", menuList);
		return "user/menu_weixin";
	}

	/**
	 * 前往微信JSON菜单页面
	 */
	@RequestMapping("/tomenu/weixin/json")
	public String tomenuWeixinJson(){
		return "user/menu_weixin_json";
	}


	/**
	 * 通过JSON的方式发布自定义微信菜单
	 */
	@ResponseBody
	@RequestMapping("/menu/weixin/create/json")
	public CommonResult tomenuWeixinJsonDeploy(String menuJson, HttpServletRequest request){
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(Constants.USERINFO);
		String accesstoken = MPPUtil.getWeixinAccessToken(userInfo.getAppId(), userInfo.getAppSecret());
		ErrorInfo errorInfo = MPPUtil.createMenu(accesstoken, menuJson);
		if(0 == errorInfo.getErrcode()){
			return new CommonResult();
		}else{
			return new CommonResult(errorInfo.getErrcode(), errorInfo.getErrmsg());
		}
	}
}