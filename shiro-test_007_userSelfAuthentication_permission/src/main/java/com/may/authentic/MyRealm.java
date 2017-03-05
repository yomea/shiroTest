package com.may.authentic;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class MyRealm extends AuthorizingRealm {  
	
	
	//表示根据用户身份获取授权信息
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {  
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();  
        authorizationInfo.addRole("role1");  
        authorizationInfo.addRole("role2");  
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));  
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));  
        authorizationInfo.addStringPermission("+user2+10");  
        authorizationInfo.addStringPermission("user2:*");  
        return authorizationInfo;  
    }  
    
    //表示获取身份验证信息
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
    	String username = (String) token.getPrincipal();
    	String password = new String((char[])token.getCredentials());
    	
    	if(username == null || !username.equals("admin")) {
    		throw new AuthenticationException("不存在的用户");
    		
    	} else if(password == null || !password.equals("admin")) {
    		
    		throw new AuthenticationException("用户名或者密码错误");
    	}
    	
    	return new SimpleAuthenticationInfo();
}  
}   
