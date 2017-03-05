package com.shiro.myrealm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

/**
 * 类似验证器
 * @author may
 *
 */
public class MyRealm implements Realm {

	@Override
	public String getName() {
		//返回这个realm的名字
		return "myrealm_1";
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		//表示这个令牌必须是用户名密码令牌
		return token instanceof UsernamePasswordToken;
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		String username = (String) token.getPrincipal();//获取用户身份，这里就是用户名
		String password = new String((char[])token.getCredentials());//获得验证信息，这里就是密码
		//验证
		if(username == null || !username.equals("admin")) {
			
			throw new AuthenticationException("此用户不存在！");
		} else if(password == null || !password.equals("admin")) {
			throw new AuthenticationException("用户名或者密码错误！");
		}
		
		
		//返回验证信息
		return new SimpleAuthenticationInfo(username, password, getName());
	}

}
