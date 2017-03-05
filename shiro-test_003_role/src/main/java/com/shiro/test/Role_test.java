package com.shiro.test;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Role_test {
	
	private static Logger logger = LoggerFactory.getLogger(Role_test.class);
	
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
		UsernamePasswordToken token = new UsernamePasswordToken("bjangles", "dance");
		//验证当前用户是否登录
		logger.info(""+currentUser.isAuthenticated());
		currentUser.login(token);
		System.out.print("身份验证成功！");
		System.out.print(currentUser.isAuthenticated());
		
		logger.info(currentUser.hasRole("role1")?"当前用户有用户角色role1":"当前用户没有用户角色role1");
		logger.info(currentUser.hasRoles(Arrays.asList("role1", "role2"))[0]?"当前用户有用户角色role1":"当前用户没有用户角色role1");
		logger.info(currentUser.hasRoles(Arrays.asList("role1", "role2"))[1]?"当前用户有用户角色role2":"当前用户没有用户角色role2");
		logger.info(currentUser.hasAllRoles(Arrays.asList("role1", "role2"))?"当前用户有用户角色role1,role2":"当前用户没有用户角色role1或者role2或者role1,role2");
		//如果存在就不报错，如果不存在就会报错
		currentUser.checkRole("role1");
		
		System.out.println();
		currentUser.logout();
		System.out.print("注销！");
		System.out.print(currentUser.isAuthenticated());
	}

}
