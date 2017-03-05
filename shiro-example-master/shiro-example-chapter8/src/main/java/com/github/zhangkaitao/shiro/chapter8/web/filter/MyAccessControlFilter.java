package com.github.zhangkaitao.shiro.chapter8.web.filter;

import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-3
 * <p>Version: 1.0
 */
public class MyAccessControlFilter extends AccessControlFilter {
	/**
	 * 即是否允许访问，返回true表示允许；
	 */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("access allowed");
        return true;
    }
    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("访问拒绝也不自己处理，继续拦截器链的执行");
        return true;
    }
}
