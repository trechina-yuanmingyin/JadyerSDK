package com.jadyer.sdk.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.jadyer.sdk.demo.common.constant.Constants;

/**
 * 管理平台菜单过滤Filter
 * @create Oct 23, 2015 5:34:56 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class MenuFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		//菜单路径暂时固定
		if(request.getServletPath().startsWith("/user")){
			request.getSession().setAttribute(Constants.MENU_VIEW_CURRENT, Constants.MENU_SYS);
		}
		if(request.getServletPath().startsWith("/fans")){
			request.getSession().setAttribute(Constants.MENU_VIEW_CURRENT, Constants.MENU_FANS);
		}
		if(request.getServletPath().startsWith("/reply")){
			request.getSession().setAttribute(Constants.MENU_VIEW_CURRENT, Constants.MENU_REPLY);
		}
		if(request.getServletPath().startsWith("/view")){
			String url = request.getParameter("url");
			if(url.startsWith("user")){
				request.getSession().setAttribute(Constants.MENU_VIEW_CURRENT, Constants.MENU_SYS);
			}
			if(url.startsWith("reply")){
				request.getSession().setAttribute(Constants.MENU_VIEW_CURRENT, Constants.MENU_REPLY);
			}
		}
		chain.doFilter(req, resp);
	}
}