package com.jadyer.sdk.demo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/view")
public class ViewController {
	/**
	 * 直接访问JSP
	 * @see 可以在URL上传参
	 * @see 比如http://127.0.0.1/view?url=reply/keyword&id=3,id=3会被放到request中
	 * @create Nov 3, 2015 5:50:54 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String url(String url, HttpServletRequest request){
		Map<String, String[]> paramMap = request.getParameterMap();
		for(Map.Entry<String,String[]> entry : paramMap.entrySet()){
			if(!"url".equals(entry.getKey())){
				request.setAttribute(entry.getKey(), entry.getValue()[0]);
			}
		}
		return url;
	}
}