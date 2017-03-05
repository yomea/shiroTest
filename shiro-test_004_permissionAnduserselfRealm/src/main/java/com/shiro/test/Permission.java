package com.shiro.test;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Permission {
	private static Logger logger = LoggerFactory.getLogger(Permission.class);
	
	public static void main(String[] args) {
		
		// Using the IniSecurityManagerFactory, which will use the an INI file
        // as the security file.
		//读取配置文件
		//数据库中的表明必须是users，字段分别为userName和password
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		// Setting up the SecurityManager...
		//获取SecurityManager
		org.apache.shiro.mgt.SecurityManager secuityManager = factory.getInstance();
		//把securityManager实例绑定到securityUtils
		SecurityUtils.setSecurityManager(secuityManager);
		//得到当前可执行的用户
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		//创建用户令牌，这里验证是数据库中users表中的用户信息
		UsernamePasswordToken token = new UsernamePasswordToken("jack", "123");
		//验证当前用户是否登录
		logger.info(""+currentUser.isAuthenticated());
		currentUser.login(token);//登录验证将使用配置文件中自定义的realm
		
		currentUser.checkPermission("user:select");
		//Subject does not have role [user:select]
		currentUser.checkRoles(Arrays.asList("user:select","user:update"));
		
		
	}

}
