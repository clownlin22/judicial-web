package com.rds.code.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {
	public static final String ORIGINAL_SERVLET_PATH = "ORIGINAL_SERVLET_PATH";
	public static String SAVED_URI = "SavedURI";

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String currentURL = httpRequest.getRequestURI();

		// 取得根目录所对应的绝对路径:
		String targetURL = currentURL.substring(currentURL.lastIndexOf("/"),
				currentURL.length());
		// 截取到当前文件名用于比较
		HttpSession session = httpRequest.getSession(false);
		if (!targetURL.contains("login.do")
				&& !"/loginIndex.jsp".equals(targetURL)
				&& !"/success.do".equals(targetURL)
				&& !"/taskDetail.do".equals(targetURL)
				&& !"/insertYXB.do".equals(targetURL)) {
			// 判断当前页是否是重定向以后的登录页面页面，如果是就不做session的判断，防止出现死循环
			if (session == null || session.getAttribute("user") == null) {
				// *用户登录以后需手动添加session
				if (httpRequest.getHeader("X-Requested-With") != null) {
					httpResponse.sendError(999);
				} else {
					httpResponse.sendRedirect(httpRequest.getContextPath()
							+ "/loginIndex.jsp");
				}
				return;
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
