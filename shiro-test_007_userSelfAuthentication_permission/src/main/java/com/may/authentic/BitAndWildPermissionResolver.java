package com.may.authentic;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

public class BitAndWildPermissionResolver implements PermissionResolver {  
    @Override  
    public Permission resolvePermission(String permissionString) {  
    	//判断是否为自定义格式的权限表达式
        if(permissionString.startsWith("+")) {  
            return new BitPermission(permissionString);  
        }  
        //如果是按照shiro自身的权限表达字符串，那么使用shiro自带的permission包装类
        return new WildcardPermission(permissionString);  
    }  
}   