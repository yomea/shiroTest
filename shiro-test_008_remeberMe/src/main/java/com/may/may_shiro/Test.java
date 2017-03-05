package com.may.may_shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.After;
import org.junit.Before;

public class Test {
	
	private Subject subject = null;
	
	@Before
	public void start() {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		
		SecurityUtils.setSecurityManager(securityManager);
		
		subject = SecurityUtils.getSubject();
		
		
		
	}

	@org.junit.Test
	public void test() {
		
		UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin");
		subject.login(token);
		token.setRememberMe(true);
	}
	
	@After
	public void end() {
		
	}

}
