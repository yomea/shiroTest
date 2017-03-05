package com.may.may_shiro;

import java.util.concurrent.SynchronousQueue;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class PermissionOption_2 {
	
	

	@Test
	public void test() {
		
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken("admin", "admin");
		currentUser.login(token);
		System.out.println(currentUser.isAuthenticated());
		
		if(!currentUser.isPermitted("user.delete")) {
			System.out.println("You do not have delete user permission!!!");
		}
		
	}

}
