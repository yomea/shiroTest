package com.github.zhangkaitao.shiro.chapter2.authenticator.strategy;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-25
 * <p>Version: 1.0
 */
public class AtLeastTwoAuthenticatorStrategy extends AbstractAuthenticationStrategy {

    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();//返回一个权限的认证信息，用来装所有验证后的合并信息
    }

    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;//返回上一个realm与aggregate合并后的aggregate
    }

    /**
     * realm 每个调用后的realm
     * token 令牌
     * singleRealmInfo 单个realm调用后返回的AuthenticationInfo
     * aggregateInfo 一个聚合的AuthenticationInfo
     * t 调用realm验证失败时返回的异常
     */
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else {
            if (aggregateInfo == null) {
                info = singleRealmInfo;
            } else {
            	//合并两个AuthenticationInfo
                info = merge(singleRealmInfo, aggregateInfo);
            }
        }

        return info;
    }

    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
      //如果验证成功的realm小于2则表示失败，如果从realm返回的aggregate为空，或者身份小于2，或为空则抛出异常
    	if (aggregate == null || CollectionUtils.isEmpty(aggregate.getPrincipals()) || aggregate.getPrincipals().getRealmNames().size() < 2) {
            throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                    "could not be authenticated by any configured realms.  Please ensure that at least two realm can " +
                    "authenticate these tokens.");
        }

        return aggregate;
    }
}
