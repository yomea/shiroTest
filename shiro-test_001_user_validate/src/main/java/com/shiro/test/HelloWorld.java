package com.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
	
	private static Logger logger = LoggerFactory.getLogger(HelloWorld.class);
	
	public static void main(String[] args) {
		// Using the IniSecurityManagerFactory, which will use the an INI file
        // as the security file.
		//读取配置文件
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		// Setting up the SecurityManager...
		//获取SecurityManager
		org.apache.shiro.mgt.SecurityManager secuityManager = factory.getInstance();
		//把securityManager实例绑定到securityUtils
		SecurityUtils.setSecurityManager(secuityManager);
		//得到当前可执行的用户
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		//创建用户令牌
		UsernamePasswordToken token = new UsernamePasswordToken("bjangles", "dance");
		//验证当前用户是否登录
		logger.info(""+currentUser.isAuthenticated());
		//当前用户使用当前创建的用户令牌去登录
		currentUser.login(token);
		System.out.print("身份验证成功！");
		System.out.print(currentUser.isAuthenticated());
		System.out.println();
		currentUser.logout();
		System.out.print("注销！");
		System.out.print(currentUser.isAuthenticated());
	}

}
