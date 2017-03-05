package com.github.zhangkaitao.shiro.chapter3.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.text.IniRealm;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
public class BitAndWildPermissionResolver implements PermissionResolver {

    @Override
    public Permission resolvePermission(String permissionString) {
    	//检查是否是自定义的权限表达式，如果是，将它解析成自定义的权限对象
        if(permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        //否则使用shiro自带的权限对象进行包装
        return new WildcardPermission(permissionString);
    }
}
