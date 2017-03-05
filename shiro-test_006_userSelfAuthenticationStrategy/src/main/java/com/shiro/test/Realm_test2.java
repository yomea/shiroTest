package com.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Realm_test2 {

	private static Logger logger = LoggerFactory.getLogger(Realm_test2.class);

	public static void main(String[] args) {
		// Using the IniSecurityManagerFactory, which will use the an INI file
		// as the security file.
		// 读取配置文件
		// 数据库中的表明必须是users，字段分别为userName和password
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:setRealmStrategy.ini");
		// Setting up the SecurityManager...
		// 获取SecurityManager
		org.apache.shiro.mgt.SecurityManager secuityManager = factory.getInstance();
		// 把securityManager实例绑定到securityUtils
		SecurityUtils.setSecurityManager(secuityManager);
		// 得到当前可执行的用户
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		// 创建用户令牌，这里验证是数据库中users表中的用户信息
		UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");
		// 验证当前用户是否登录
		logger.info("" + currentUser.isAuthenticated());
		/**
		 * FirstSuccessfulStrategy：只要有一个Realm验证成功即可，只返回第一个Realm身份验证成功的认证信息，其他的忽略；
		 * AtLeastOneSuccessfulStrategy：只要有一个Realm验证成功即可，和FirstSuccessfulStrategy不同，返回所有Realm身份验证成功的认证信息；
		 * AllSuccessfulStrategy：所有Realm验证成功才算成功，且返回所有Realm身份验证成功的认证信息，如果有一个失败就失败了。
		 */
		currentUser.login(token);// 登录验证将使用配置文件中自定义的realm
		System.err.print("身份验证成功！-----------------");
		System.err.print(currentUser.getPrincipals() + "----------------------------");
		System.err.print(currentUser.isAuthenticated());

		System.err.println();
		currentUser.logout();
		System.err.print("注销！");
		System.err.print(currentUser.isAuthenticated());
	}

}
