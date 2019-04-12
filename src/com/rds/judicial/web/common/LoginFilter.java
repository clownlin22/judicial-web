package com.rds.judicial.web.common;

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
import javax.servlet.http.HttpSession;

import com.rds.upc.model.RdsUpcUserModel;

public class LoginFilter implements Filter {

	public LoginFilter() {

		// TODO Auto-generated constructor stub
	}

	public void destroy() {

		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		HttpServletResponse res = (HttpServletResponse) response;

		// 获得请求的URL

		String url = req.getRequestURL().toString();

		// 获得session中的对象

		HttpSession session = req.getSession();

		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");

		// 使用endsWith()判断url结尾的字符串

		if (url.endsWith("loginIndex.jsp") || user != null
				|| url.endsWith("success.do") || url.endsWith(".css")
				|| url.endsWith(".js") || url.endsWith(".gif")
				|| url.endsWith(".png") || url.endsWith(".jpg")
				|| url.endsWith("login.do")) {
			// 满足条件就继续执行
			chain.doFilter(request, response);

		} else {
			response.setCharacterEncoding("UTF-8");
			// 不满足条件就跳转到其他页面
			PrintWriter out = res.getWriter();
			String string = "对不起，您的登录状态已失效，请刷新后重新登录！</br><a href="+url.split("judicial-web")[0]+"judicial-web/loginIndex.jsp>"+url.split("judicial-web")[0]+"judicial-web/loginIndex.jsp</a>";
			out.print("{'message':'" + string + "'}");
		}

	}

	/**
	 * 
	 * @see Filter#init(FilterConfig)
	 */

	public void init(FilterConfig fConfig) throws ServletException {

		// TODO Auto-generated method stub

	}

}