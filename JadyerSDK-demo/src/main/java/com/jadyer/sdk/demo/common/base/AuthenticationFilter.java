package com.jadyer.sdk.demo.common.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.common.util.ConfigUtil;

/**
 * 权限验证
 * @see -----------------------------------------------------------------------------------------------------------
 * @see 待解决
 * @see Session超时后,Ajax请求会被"Status Code:302 Found"到超时后的登录页面http://127.0.0.1:8088/engine/login.jsp
 * @see 此时前台JS中收到的Ajax结果是undefined,其实是没有得到应答,所以alert(jsonData.message)时会弹出undefined
 * @see -----------------------------------------------------------------------------------------------------------
 * @create Dec 3, 2014 10:39:11 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class AuthenticationFilter implements Filter {
	private String url = "/error.jsp";
	private String[] anonymousResources = new String[]{};
	
	public void destroy() {}
	
	/**
	 * 获取web.xml中设定的参数url的值
	 * @see 即读取web.xml中的<param-name>url</param-name>
	 */
	public void init(FilterConfig config) throws ServletException {
		this.url = config.getInitParameter("url");
		this.anonymousResources = ConfigUtil.INSTANCE.getProperty("authentication.anonymous").split("`");
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		/**
		 * 增加对[/js/**]模式的资源控制
		 */
		boolean disallowAnonymousVisit = true;
		List<String> anonymousResourceList = Arrays.asList(this.anonymousResources);
		for(String anonymousResource : anonymousResourceList){
			if(anonymousResource.equals(request.getServletPath())){
				disallowAnonymousVisit = false;
				break;
			}
			if(anonymousResource.endsWith("**") && request.getServletPath().startsWith(anonymousResource.replace("**", ""))){
				disallowAnonymousVisit = false;
				break;
			}
		}
		if(disallowAnonymousVisit && null==request.getSession().getAttribute(Constants.USERINFO)){
			response.sendRedirect(request.getContextPath() + this.url);
		}else{
			chain.doFilter(req, resp);
		}
	}
}