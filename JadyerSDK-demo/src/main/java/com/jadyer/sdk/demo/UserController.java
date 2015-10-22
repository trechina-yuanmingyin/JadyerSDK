package com.jadyer.sdk.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.demo.common.base.CommonResult;
import com.jadyer.sdk.demo.common.constant.Constants;

@Controller
@RequestMapping(value="/user")
public class UserController{
	@ResponseBody
	@RequestMapping("/login/{username}/{password}")
	public CommonResult save(@PathVariable String username, @PathVariable String password, HttpServletRequest request){
		request.setAttribute(Constants.USER, "test");
		return new CommonResult();
	}

	@RequestMapping("/welcome")
	public String welcome(){
		return "welcome";
	}
}