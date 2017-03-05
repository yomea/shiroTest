package com.github.zhangkaitao.shiro.chapter8.web.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-2-4
 * <p>
 * Version: 1.0
 */
public class FormLoginFilter extends PathMatchingFilter {

	private String loginUrl = "/login.jsp";
	private String successUrl = "/";

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		if (SecurityUtils.getSubject().isAuthenticated()) {
			return true;// 已经登录过
		}
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (isLoginRequest(req)) {
			//判断是否为post请求，如果不是，则继续后面的拦截器（放行）
			if ("post".equalsIgnoreCase(req.getMethod())) {// form表单提交
				boolean loginSuccess = login(req); // 登录
				if (loginSuccess) {
					return redirectToSuccessUrl(req, resp);
				}
			}
			return true;// 继续过滤器链
		} else {// 保存当前地址并重定向到登录界面
			saveRequestAndRedirectToLogin(req, resp);
			return false;
		}
	}

	private boolean redirectToSuccessUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/**
		 * 将重定向到先前保存的请求的请求url，或者如果没有保存的请求，将最终用户重定向到指定的fallbackUrl。如果没有保存的请求或后备网址，此方法会抛出IllegalStateException。
		 * 
		 * 此方法主要用于支持常见的登录方案
		 * 如果未经身份验证的用户访问需要身份验证的页面，则应首先保存请求，然后重定向到登录页面。然后，在成功登录后，可以调用此方法将它们重定向到他们最初请求的URL，一个很好的可用性功能。
		 * 
		 * 
		 * 
		 * 
		 */
		//重定向到保存的请求redirectToSavedRequest(ServletRequest request, ServletResponse response, String fallbackUrl)
		WebUtils.redirectToSavedRequest(req, resp, successUrl);
		//直接重定向到上次保存的地址，并且不在继续执行后面的FilterChain
		return false;
	}

	private void saveRequestAndRedirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//保存请求，在下次登录验证成功后，调用redirectToSavedRequest进行跳转到保存的request
		WebUtils.saveRequest(req);
		//根据给定的参数和未指定参数的默认值将当前请求重定向到新的URL。
		WebUtils.issueRedirect(req, resp, loginUrl);
	}

	private boolean login(HttpServletRequest req) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		try {
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
		} catch (Exception e) {
			req.setAttribute("shiroLoginFailure", e.getClass());
			return false;
		}
		return true;
	}

	private boolean isLoginRequest(HttpServletRequest req) {
		/**
		 * WebUtils.getPathWithinApplication(req)源码 String contextPath =
		 * getContextPath(request); String requestUri = getRequestUri(request);
		 * if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) { //
		 * Normal case: URI contains context path. String path =
		 * requestUri.substring(contextPath.length()); return
		 * (StringUtils.hasText(path) ? path : "/"); } else { // Special case:
		 * rather unusual. return requestUri; }
		 */
		// 检测loginURL是否和request请求的一致，如果一致就返回TRUE
		return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(req));
	}
}
