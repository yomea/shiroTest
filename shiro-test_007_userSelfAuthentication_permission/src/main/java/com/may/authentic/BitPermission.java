package com.may.authentic;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.Permission;

/**
 * 配置这个权限类后，shiro会将配置中的权限new
 * BitPermission(..)出来存在一个容器中，最后需要验证的时候，就会调用implies进行合逐个验证
 * 
 * @author Administrator
 *
 */
public class BitPermission implements Permission {
	private String resourceIdentify;
	private int permissionBit;
	private String instanceId;

	public BitPermission(String permissionString) {
		String[] array = permissionString.split("\\+");
		if (array.length > 1) {
			resourceIdentify = array[1];
		}
		if (StringUtils.isEmpty(resourceIdentify)) {
			resourceIdentify = "*";
		}
		if (array.length > 2) {
			permissionBit = Integer.valueOf(array[2]);
		}
		if (array.length > 3) {
			instanceId = array[3];
		}
		if (StringUtils.isEmpty(instanceId)) {
			instanceId = "*";
		}
	}
	/**
	 * 将用户需要的权限与本地的权限列表进行比较
	 */
	@Override
	public boolean implies(Permission p) {
		if (!(p instanceof BitPermission)) {
			return false;
		}
		BitPermission other = (BitPermission) p;
		if (!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))) {
			return false;
		}
		if (!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) != 0)) {
			return false;
		}
		if (!("*".equals(this.instanceId) || this.instanceId.equals(other.instanceId))) {
			return false;
		}
		return true;
	}
}