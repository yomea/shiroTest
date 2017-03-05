package com.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jdbc_realm {
	
	private static Logger logger = LoggerFactory.getLogger(Jdbc_realm.class);
	
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
		UsernamePasswordToken token = new UsernamePasswordToken("test", "test");
		//验证当前用户是否登录
		logger.info(""+currentUser.isAuthenticated());
		currentUser.login(token);
		System.out.print("身份验证成功！");
		System.out.print(currentUser.isAuthenticated());
		System.out.println();
		currentUser.logout();
		System.out.print("注销！");
		System.out.print(currentUser.isAuthenticated());
	}

}
