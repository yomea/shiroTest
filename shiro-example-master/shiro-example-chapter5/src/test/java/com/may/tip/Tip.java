package com.may.tip;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

public class Tip {

	/**
	 * 将明文转换成密文
	 * @author may
	 *
	 */
	public interface PasswordService {  
	    //输入明文密码得到密文密码  
	    String encryptPassword(Object plaintextPassword) throws IllegalArgumentException;  
	}  
	
	/**
	 * 对比传入的值和系统中的密码是否一致
	 * @author may
	 *
	 */
	public interface CredentialsMatcher {  
	    //匹配用户输入的token的凭证（未加密）与系统提供的凭证（已加密）  
	    boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info);  
	} 
	
}
