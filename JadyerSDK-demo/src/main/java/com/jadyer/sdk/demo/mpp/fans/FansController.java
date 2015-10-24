package com.jadyer.sdk.demo.mpp.fans;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.demo.common.constant.Constants;

@Controller
@RequestMapping(value="/fans")
public class FansController{
	@Resource
	private FansInfoDao fansInfoDao;

	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		request.setAttribute("fansList", fansInfoDao.findByUid(uid));
		return "fansList";
	}
}