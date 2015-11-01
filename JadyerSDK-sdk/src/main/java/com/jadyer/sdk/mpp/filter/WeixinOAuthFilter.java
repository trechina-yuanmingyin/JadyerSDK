package com.jadyer.sdk.mpp.filter;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jadyer.sdk.mpp.constant.MPPConstants;
import com.jadyer.sdk.mpp.util.HttpUtil;
import com.jadyer.sdk.mpp.util.MPPUtil;

/**
 * 微信网页授权获取用户信息
 * @see -----------------------------------------------------------------------------------------------------------
 * @see <filter>
 * @see 	<filter-name>WeixinOAuthFilter</filter-name>
 * @see 	<filter-class>com.jadyer.sdk.mpp.filter.WeixinOAuthFilter</filter-class>
 * @see 	<init-param>
 * @see 		<param-name>appid</param-name>
 * @see 		<param-value>wx63ae5326e400cca2</param-value>
 * @see 	</init-param>
 * @see 	<init-param>
 * @see 		<param-name>appsecret</param-name>
 * @see 		<param-value>b6a838ea12d6175c00793503500ede64</param-value>
 * @see 	</init-param>
 * @see 	<init-param>
 * @see 		<param-name>redirecturl</param-name>
 * @see 		<param-value>http://jadyer.tunnel.mobi/weixin/helper/oauth/getAccessToken/33</param-value>
 * @see 	</init-param>
 * @see </filter>
 * @see <filter-mapping>
 * @see 	<filter-name>WeixinOAuthFilter</filter-name>
 * @see 	<url-pattern>/*</url-pattern>
 * @see </filter-mapping>
 * @see -----------------------------------------------------------------------------------------------------------
 * @create Oct 19, 2015 4:45:35 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class WeixinOAuthFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(WeixinOAuthFilter.class);
	private String redirecturl = null;
	public static String appid = null;
	public static String appsecret = null;

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		appid = filterConfig.getInitParameter("appid");
		appsecret = filterConfig.getInitParameter("appsecret");
		this.redirecturl = filterConfig.getInitParameter("redirecturl");
	}

	/**
	 * 判断是否需要通过网页授权获取用户信息
	 * @see 1.请求参数需包含oauth=base&openid=openid两个参数,无论GET还是POST请求
	 * @see 2.该Filter常用于自定义菜单跳转URL时获取粉丝的openid,故验证条件较为苛刻
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		if("base".equals(request.getParameter("oauth")) && "openid".equals(request.getParameter("openid"))){
			/**
			 * 不同浏览来源的User-Agent头信息不同
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
			if(!userAgent.contains("MicroMessenger") || (!userAgent.contains("iPhone") && !userAgent.contains("Android"))){
				response.setCharacterEncoding(HttpUtil.DEFAULT_CHARSET);
				response.setContentType("text/plain; charset=" + HttpUtil.DEFAULT_CHARSET);
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				PrintWriter out = response.getWriter();
				out.print("请于iPhone或Android手机微信端访问");
				out.flush();
				out.close();
				return;
			}
			/**
			 * 计算state
			 * @see 注意得到的URL的第一个字符是斜线,如下所示
			 * @see state=/JadyerSDK/user/get/2/uname=玄玉/openid=openid或者state=/user/get/2/uname=玄玉/openid=openid
			 */
			String fullURI = request.getRequestURI() + (null==request.getQueryString()?"":"?"+request.getQueryString());
			String state = fullURI.replace("?", "/").replace("&", "/");
			state = state.replace("/oauth=base", "");
			logger.info("计算粉丝请求的资源得到state={}", state);
			response.sendRedirect(MPPUtil.buildWeixinOAuthCodeURL(appid, MPPConstants.OAUTH_SCOPE_SNSAPI_BASE, state, this.redirecturl));
		}else{
			chain.doFilter(req, resp);
		}
	}
}
///**
// * 自定义ServletRequest对象
// * @see 由于HttpServletRequest.getParameterMap()得到的Map是immutable的,不可更改的
// * @see 而且HttpServletRequest.setAttribute()方法也是不能修改请求参数的,故扩展此类
// * @create Oct 19, 2015 5:08:24 PM
// * @author 玄玉<http://blog.csdn.net/jadyer>
// */
//class MPPHttpServletRequestWrapper extends HttpServletRequestWrapper{
//	Map<String, String[]> paramMap = null;
//
//	public MPPHttpServletRequestWrapper(HttpServletRequest request) {
//		super(request);
//		this.paramMap = new HashMap<String, String[]>(request.getParameterMap());
//	}
//
//	/**
//	 * 新增的setParameter()方法
//	 */
//	public void setParameter(String key, String[] values){
//		paramMap.put(key, values);
//	}
//
//	/**
//	 * 新增的setParameter()方法
//	 */
//	public void setParameter(String key, String value){
//		this.setParameter(key, new String[]{value});
//	}
//
//	@Override
//	public String getParameter(String name) {
//		Object obj = paramMap.get(name);
//		if(null == obj){
//			return null;
//		}
//		if(obj instanceof String[]){
//			String[] objArr = (String[])obj;
//			if(objArr.length > 0){
//				return objArr[0];
//			}else{
//				return null;
//			}
//		}
//		return obj.toString();
//	}
//
//	@Override
//	public Map<String, String[]> getParameterMap() {
//		return paramMap;
//	}
//
//	@Override
//	public Enumeration<String> getParameterNames() {
//		Vector<String> vector = new Vector<String>(paramMap.keySet());
//		return vector.elements();
//	}
//
//	@Override
//	public String[] getParameterValues(String name) {
//		return paramMap.get(name);
//	}
//}