package com.jadyer.sdk.util;

import javax.servlet.http.HttpServletRequest;

public final class SDKUtil {
	private SDKUtil(){}

	/**
	 * 判断是否为Ajax请求
	 * @create Nov 1, 2015 1:30:55 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String requestType = request.getHeader("X-Requested-With");
		if(null!=requestType && "XMLHttpRequest".equals(requestType)){
			return true;
		}
		requestType = request.getHeader("x-requested-with");
		if(null!=requestType && "XMLHttpRequest".equals(requestType)){
			return true;
		}else{
			return false;
		}
	}
}