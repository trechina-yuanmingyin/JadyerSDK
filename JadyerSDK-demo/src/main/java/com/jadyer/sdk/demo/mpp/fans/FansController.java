package com.jadyer.sdk.demo.mpp.fans;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/fans")
public class FansController{
	@Resource
	private FansInfoDao fansInfoDao;

	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		request.setAttribute("fansList", fansInfoDao.findAll());
		return "fansList";
	}
}