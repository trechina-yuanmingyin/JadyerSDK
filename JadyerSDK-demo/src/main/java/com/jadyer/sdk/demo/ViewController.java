package com.jadyer.sdk.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/view")
public class ViewController {
	@RequestMapping(method=RequestMethod.GET)
	public String url(String url){
		return url;
	}
}