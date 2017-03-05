package com.github.zhangkaitao.shiro.chapter3.permission;

import com.alibaba.druid.util.StringUtils;
import junit.framework.Assert;
import org.apache.shiro.authz.Permission;

/**
 *  规则
 *    +资源字符串+权限位+实例ID
 *
 *  以+开头 中间通过+分割
 *
 *  权限：
 *     0 表示所有权限
 *     1 新增 0001
 *     2 修改 0010
 *     4 删除 0100
 *     8 查看 1000
 *
 *  如 +user+10 表示对资源user拥有修改/查看权限
 *
 *  不考虑一些异常情况
 *
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
//自定义的权限对象
public class BitPermission implements Permission {

    private String resourceIdentify;
    private int permissionBit;
    private String instanceId;

    public BitPermission(String permissionString) {
    	//如+user+10
        String[] array = permissionString.split("\\+");
        //array[0] = ""; array[1] = "user";array[2] = "10";
        if(array.length > 1) {
            resourceIdentify = array[1];//resourceIdentify = user;
        }

        if(StringUtils.isEmpty(resourceIdentify)) {
            resourceIdentify = "*";
        }

        if(array.length > 2) {
            permissionBit = Integer.valueOf(array[2]);
        }

        if(array.length > 3) {
            instanceId = array[3];
        }

        if(StringUtils.isEmpty(instanceId)) {
            instanceId = "*";
        }

    }
    
    
    /**
     * shiro将ini配置文件中的权限存在了AuthenticInfo中的一个集合中，当需要
     * 比较的时候循环和参数p（即用户传进来需要验证的权限）
     */
    @Override
    public boolean implies(Permission p) {
        if(!(p instanceof BitPermission)) {
            return false;
        }
        BitPermission other = (BitPermission) p;
        //如果资源可以不是任意的，或者不和这个资源相等，则失败
        if(!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))) {
            return false;
        }

        if(!(this.permissionBit ==0 || (this.permissionBit & other.permissionBit) != 0)) {
            return false;
        }

        if(!("*".equals(this.instanceId) || this.instanceId.equals(other.instanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BitPermission{" +
                "resourceIdentify='" + resourceIdentify + '\'' +
                ", permissionBit=" + permissionBit +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }
}
