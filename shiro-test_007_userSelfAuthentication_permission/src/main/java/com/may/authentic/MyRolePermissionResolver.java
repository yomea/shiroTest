package com.may.authentic;

import java.util.Arrays;
import java.util.Collection;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * 角色通过角色来授权
 * @author Administrator
 *
 */
public class MyRolePermissionResolver implements RolePermissionResolver {
	/**
	 * RolePermissionResolver用于根据角色字符串来解析得到权限集合。
	 * @param roleString 判断用户是否拥有这个角色时传递进来的
	 */
	@Override
	public Collection<Permission> resolvePermissionsInRole(String roleString) {
		if ("role1".equals(roleString)) {
			return Arrays.asList((Permission) new WildcardPermission("menu:*"));
		}
		return null;
	}
}