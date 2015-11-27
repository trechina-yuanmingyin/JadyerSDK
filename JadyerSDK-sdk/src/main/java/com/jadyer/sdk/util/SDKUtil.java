package com.jadyer.sdk.util;

import java.util.Arrays;
import java.util.Map;

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


	/**
	 * 获取Map中的属性
	 * @see 该方法的参数适用于打印Map<String, String[]>的情况
	 * @see 由于Map.toString()打印出来的参数值对,是横着一排的...参数多的时候,不便于查看各参数值
	 * @see 故此仿照commons-lang3.jar中的ReflectionToStringBuilder.toString()编写了本方法
	 * @return String key11=value11 \n key22=value22 \n key33=value33 \n......
	 */
	public static String buildStringFromMapWithStringArray(Map<String, String[]> map){
		StringBuilder sb = new StringBuilder();
		sb.append(map.getClass().getName()).append("@").append(map.hashCode()).append("[");
		for(Map.Entry<String,String[]> entry : map.entrySet()){
			sb.append("\n").append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue()));
		}
		return sb.append("\n]").toString();
	}
}