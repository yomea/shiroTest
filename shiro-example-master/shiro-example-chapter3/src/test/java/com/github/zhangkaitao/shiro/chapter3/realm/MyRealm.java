package com.github.zhangkaitao.shiro.chapter3.realm;

import com.github.zhangkaitao.shiro.chapter3.permission.BitPermission;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
public class MyRealm extends AuthorizingRealm {

	//在验证权限和角色时调用
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //给验证信息加上角色，和权限，如果是shiro自带的，将是从ini中取得
        //在shiro校验是否存在这个角色是会从这个集合中寻找
        authorizationInfo.addRole("role1");
        authorizationInfo.addRole("role2");
        //权限实例，当需要进行校验是否有这个权限时会在这个列表中进行循环，调用他们自身的implies方法（有点类似compareTo方法）
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
        //加入字符串权限，验证方法有重载，有传入权限对象的，有直接传权限字符串的
        authorizationInfo.addStringPermission("+user2+10");
        authorizationInfo.addStringPermission("user2:*");
        return authorizationInfo;
    }

    //这个是在登录时进行验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();  //得到用户名
        String password = new String((char[])token.getCredentials()); //得到密码
        if(!"zhang".equals(username)) {
            throw new UnknownAccountException(); //如果用户名错误
        }
        if(!"123".equals(password)) {
            throw new IncorrectCredentialsException(); //如果密码错误
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
