package com.rds.upc.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.rds.upc.model.RdsUpcUserModel;

public class SessionOperation {
	/**
	 * 用户登录设置session
	 * @param request
	 * @param user
	 */
	public void setSesion(HttpServletRequest request,RdsUpcUserModel user)
	{
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		session.setMaxInactiveInterval(36000);
	}
	/**
	 * 用户退出清除session 
	 * @param request
	 * @param user
	 */
	public void clearSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		session.invalidate();
	}
}
