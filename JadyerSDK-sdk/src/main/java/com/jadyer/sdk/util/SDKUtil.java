package com.jadyer.sdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

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


	/**
	 * 提取收到的HttpServletRequest请求报文头消息
	 * @see 该方法默认使用了UTF-8解析请求消息
	 * @see 解析过程中发生异常时会抛出RuntimeException
	 * @create Nov 28, 2015 4:12:19 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String extractHttpServletRequestHeaderMessage(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		sb.append(request.getMethod()).append(" ").append(request.getRequestURI() + (null==request.getQueryString()?"":"?"+request.getQueryString())).append(" ").append(request.getProtocol()).append("\n");
		String headerName = null;
		for(Enumeration<String> obj = request.getHeaderNames(); obj.hasMoreElements();){
			headerName = obj.nextElement();
			sb.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
		}
		return sb.toString();
	}


	/**
	 * 提取收到的HttpServletRequest请求报文体消息
	 * @see 该方法默认使用了UTF-8解析请求消息
	 * @see 解析过程中发生异常时会抛出RuntimeException
	 * @create Nov 28, 2015 4:12:19 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public static String extractHttpServletRequestBodyMessage(HttpServletRequest request){
		try{
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e1){
			//ignore
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try{
			br = request.getReader();
			for(String line=null; (line=br.readLine())!=null;){
				sb.append(line).append("\n");
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}finally {
			if(null != br){
				IOUtils.closeQuietly(br);
			}
		}
		return sb.toString();
	}
}