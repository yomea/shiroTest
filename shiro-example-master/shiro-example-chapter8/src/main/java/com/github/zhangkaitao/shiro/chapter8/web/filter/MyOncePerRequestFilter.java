package com.github.zhangkaitao.shiro.chapter8.web.filter;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-3
 * <p>Version: 1.0
 */
//保证一次请求只调用一次doFilterInternal
public class MyOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("=========once per request filter");
        chain.doFilter(request, response);
    }
}
