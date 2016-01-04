package com.jadyer.sdk.weixin.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadyer.sdk.util.HttpUtil;
import com.jadyer.sdk.util.SDKUtil;
import com.jadyer.sdk.weixin.constant.WeixinConstants;
import com.jadyer.sdk.weixin.helper.WeixinHelper;

/**
 * 用于处理微信相关的Filter
 * @create Oct 19, 2015 4:45:35 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class WeixinFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(WeixinFilter.class);

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	/**
	 * 判断是否需要通过网页授权获取粉丝信息
	 * @see 1.请求参数需包含appid=wx63ae5326e400cca2&oauth=base&openid=openid
	 * @see 2.该Filter常用于自定义菜单跳转URL时获取粉丝的openid,故验证条件较为苛刻
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String appid = request.getParameter("appid");
		if(StringUtils.isNotBlank(appid) && "base".equals(request.getParameter("oauth")) && "openid".equals(request.getParameter("openid"))){
			if(SDKUtil.isAjaxRequest(request)){
				throw new RuntimeException("请不要通过Ajax获取粉丝信息");
			}
			/**
			 * @see 1.IE-11.0.9600.17843
			 * @see   User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko
			 * @see 2.Chrome-46.0.2490.71 m (64-bit)
			 * @see   User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36
			 * @see 3.Windows-1.5.0.22
			 * @see   User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat
			 * @see 4.IOS-WeChat-6.3.1
			 * @see   User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Mobile/11D201 MicroMessenger/6.3.1 NetType/WIFI Language/en
			 * @see 5.Android-WeChat-6.2.6
			 * @see   User-Agent: Mozilla/5.0 (Linux; U; Android 4.4.2; zh-cn; H60-L01 Build/HDH60-L01) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025469 Mobile Safari/533.1 MicroMessenger/6.2.5.54_re87237d.622 NetType/WIFI Language/zh_CN
			 */
			String userAgent = request.getHeader("User-Agent");
			logger.info("网页授权获取粉丝信息时请求的User-Agent=[{}]", userAgent);
			if(!userAgent.contains("MicroMessenger") || (!userAgent.contains("iPhone") && !userAgent.contains("Android"))){
				response.setCharacterEncoding(HttpUtil.DEFAULT_CHARSET);
				response.setContentType("text/plain; charset=" + HttpUtil.DEFAULT_CHARSET);
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				PrintWriter out = response.getWriter();
				out.print("请于iPhone或Android微信端访问");
				out.flush();
				out.close();
				return;
			}
			/**
			 * state=http://www.jadyer.com/JadyerSDK/user/get/2/uname=玄玉/openid=openid
			 */
			String fullURL = request.getRequestURL().toString() + (null==request.getQueryString()?"":"?"+request.getQueryString());
			String state = fullURL.replace("?", "/").replaceAll("&", "/").replace("/oauth=base", "");
			logger.info("计算粉丝请求的资源得到state=[{}]", state);
			//String appurl = http://www.jadyer.com/mpp
			StringBuilder appurl = new StringBuilder();
			appurl.append(request.getScheme()).append("://").append(request.getServerName());
			if(80!=request.getServerPort() && 443!=request.getServerPort()){
				appurl.append(":").append(request.getServerPort());
			}
			appurl.append(request.getContextPath());
			String redirectURL = WeixinHelper.buildWeixinOAuthCodeURL(appid, WeixinConstants.WEIXIN_OAUTH_SCOPE_SNSAPI_BASE, state, appurl.toString()+"/weixin/helper/oauth/"+appid);
			logger.info("计算请求到微信服务器的redirectURL=[{}]", redirectURL);
			response.sendRedirect(redirectURL);
		}else{
			chain.doFilter(req, resp);
		}
	}


/*
	/**
	 * 可手工设置HttpServletRequest入参的Wrapper
	 * @see ---------------------------------------------------------------------------------------
	 * @see 由于HttpServletRequest.getParameterMap()得到的Map是immutable的,不可更改的
	 * @see 而且HttpServletRequest.setAttribute()方法也是不能修改请求参数的,故扩展此类
	 * @see ---------------------------------------------------------------------------------------
	 * @see RequestParameterWrapper requestWrapper = new RequestParameterWrapper(request);
	 * @see requestWrapper.addAllParameters(Map<String, Object> allParams);
	 * @see filterChain.doFilter(requestWrapper, response);
	 * @see ---------------------------------------------------------------------------------------
	 * @create Dec 19, 2015 9:06:04 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 *
	class RequestParameterWrapper extends HttpServletRequestWrapper{
		private Map<String, String[]> paramMap = new HashMap<String, String[]>();
		public RequestParameterWrapper(HttpServletRequest request) {
			super(request);
			this.paramMap.putAll(request.getParameterMap());
		}
		@Override
		public String getParameter(String name) {
			String[] values = this.paramMap.get(name);
			if(null==values || values.length==0){
				return null;
			}
			return values[0];
		}
		@Override
		public Map<String, String[]> getParameterMap() {
			return this.paramMap;
		}
		@Override
		public Enumeration<String> getParameterNames() {
			return new Vector<String>(this.paramMap.keySet()).elements();
		}
		@Override
		public String[] getParameterValues(String name) {
			return this.paramMap.get(name);
		}
		public void addParameter(String name, Object value){
			if(null != value){
				if(value instanceof String[]){
					this.paramMap.put(name, (String[])value);
				}else if(value instanceof String){
					this.paramMap.put(name, new String[]{(String)value});
				}else{
					this.paramMap.put(name, new String[]{String.valueOf(value)});
				}
			}
		}
		public void addAllParameters(Map<String,Object> allParams){
			for(Map.Entry<String,Object> entry : allParams.entrySet()){
				this.addParameter(entry.getKey(), entry.getValue());
			}
		}
	}


	/**
	 * 可手工设置HttpServletResponse出参的Wrapper
	 * @see ---------------------------------------------------------------------------------------
	 * @see ResponseContentWrapper wrapperResponse = new ResponseContentWrapper(response);
	 * @see filterChain.doFilter(request, wrapperResponse);
	 * @see String content = wrapperResponse.getContent();
	 * @see response.getOutputStream().write(content.getBytes("UTF-8"));
	 * @see return;
	 * @see ---------------------------------------------------------------------------------------
	 * @see response.setHeader("Content-Type", "application/json; charset=UTF-8");
	 * @see //response.getWriter().write("abcdefg");
	 * @see response.getOutputStream().write(("{\"code\":\"102\", \"message\":\"重复请求\"}").getBytes("UTF-8"));
	 * @see return;
	 * @see ---------------------------------------------------------------------------------------
	 * @create Dec 19, 2015 9:07:09 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 *
	class ResponseContentWrapper extends HttpServletResponseWrapper {
		private ResponsePrintWriter writer;
		private OutputStreamWrapper outputWrapper;
		private ByteArrayOutputStream output;
		public ResponseContentWrapper(HttpServletResponse httpServletResponse) {
			super(httpServletResponse);
			output = new ByteArrayOutputStream();
			outputWrapper = new OutputStreamWrapper(output);
			writer = new ResponsePrintWriter(output);
		}
		public void finalize() throws Throwable {
			super.finalize();
			output.close();
			writer.close();
		}
		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return outputWrapper;
		}
		public String getContent() {
			try {
				writer.flush();
				return writer.getByteArrayOutputStream().toString("UTF-8");
			} catch (UnsupportedEncodingException e) {
				return "UnsupportedEncoding";
			}
		}
		public void close() throws IOException {
			writer.close();
		}
		public PrintWriter getWriter() throws IOException {
			return writer;
		}
		private class ResponsePrintWriter extends PrintWriter {
			ByteArrayOutputStream output;
			public ResponsePrintWriter(ByteArrayOutputStream output) {
				super(output);
				this.output = output;
			}
			public ByteArrayOutputStream getByteArrayOutputStream() {
				return output;
			}
		}
		private class OutputStreamWrapper extends ServletOutputStream {
			ByteArrayOutputStream output;
			public OutputStreamWrapper(ByteArrayOutputStream output) {
				this.output = output;
			}
			@Override
			public boolean isReady() {
				return true;
			}
			@Override
			public void setWriteListener(WriteListener listener) {
				throw new UnsupportedOperationException("UnsupportedMethod setWriteListener.");
			}
			@Override
			public void write(int b) throws IOException {
				output.write(b);
			}
		}
	}
*/
}