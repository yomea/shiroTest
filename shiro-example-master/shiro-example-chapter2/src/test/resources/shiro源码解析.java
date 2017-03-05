


org.apache.shiro.authc.pam.ModularRealmAuthenticator类

protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
		//获取验证策略
        AuthenticationStrategy strategy = getAuthenticationStrategy();
		
		//在调用所有的realms之前调用，在源码中的实现为返回一个org.apache.shiro.authc.SimpleAuthenticationInfo.SimpleAuthenticationInfo的实例
		//可以按自己的需求自定义一个AuthenticationStrategy
	    AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        if (log.isTraceEnabled()) {
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        for (Realm realm : realms) {
			//在调用每个realm前调用策略的beforeAttempt方法，这里的实现很简单，只是new了一个空的AuthenticationInfo
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
			//检验realm是否支持当前令牌的验证
            if (realm.supports(token)) {

                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

                AuthenticationInfo info = null;
                Throwable t = null;
                try {
					//这里的方法可以进行令牌的验证
                    info = realm.getAuthenticationInfo(token);
                } catch (Throwable throwable) {
                    t = throwable;
                    if (log.isDebugEnabled()) {
                        String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
                        log.debug(msg, t);
                    }
                }
				//调用每个realm后调用
				/*
				 *realm 执行完后的realm实例
				 *token 用户输入的令牌
				 *info 执行realm.getAuthenticationInfo(token)后返回的AuthenticationInfo
				 *aggregate 用来合并的AuthenticationInfo，info的所有返回的AuthenticationInfo，到时候都会合并到这个实例中，aggregate中有集合分别
				 *储存身份和验证
				 *t 程序执行中出现的验证或者其他异常信息
				 */
                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);

            } else {
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }
		//执行完所有的realm之后调用策略的afterAllAttempts()方法
        aggregate = strategy.afterAllAttempts(token, aggregate);

        return aggregate;
    }



org.apache.shiro.authc.pam.AbstractAuthenticationStrategy类，进行合并操作


protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
        if( aggregate instanceof MergableAuthenticationInfo ) {
            ((MergableAuthenticationInfo)aggregate).merge(info);
            return aggregate;
        } else {
            throw new IllegalArgumentException( "Attempt to merge authentication info from multiple realms, but aggregate " +
                      "AuthenticationInfo is not of type MergableAuthenticationInfo." );
        }
    }

    /**
     * Simply returns the <code>aggregate</code> argument without modification.  Can be overridden for custom behavior.
     */
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}


 public void merge(AuthenticationInfo info) {
        if (info == null || info.getPrincipals() == null || info.getPrincipals().isEmpty()) {
            return;
        }

        if (this.principals == null) {
            this.principals = info.getPrincipals();
        } else {
            if (!(this.principals instanceof MutablePrincipalCollection)) {
                this.principals = new SimplePrincipalCollection(this.principals);
            }
			//聚合之前的所有身份
            ((MutablePrincipalCollection) this.principals).addAll(info.getPrincipals());
        }

        //only mess with a salt value if we don't have one yet.  It doesn't make sense
        //to merge salt values from different realms because a salt is used only within
        //the realm's credential matching process.  But if the current instance's salt
        //is null, then it can't hurt to pull in a non-null value if one exists.
        //
        //since 1.1:
        if (this.credentialsSalt == null && info instanceof SaltedAuthenticationInfo) {
            this.credentialsSalt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
        }

        Object thisCredentials = getCredentials();
        Object otherCredentials = info.getCredentials();

        if (otherCredentials == null) {
            return;
        }

        if (thisCredentials == null) {
            this.credentials = otherCredentials;
            return;
        }
		//聚合之前的验证
        if (!(thisCredentials instanceof Collection)) {
            Set newSet = new HashSet();
            newSet.add(thisCredentials);
            setCredentials(newSet);
        }

        // At this point, the credentials should be a collection
        Collection credentialCollection = (Collection) getCredentials();
        if (otherCredentials instanceof Collection) {
            credentialCollection.addAll((Collection) otherCredentials);
        } else {
            credentialCollection.add(otherCredentials);
        }
    }



自定义的AuthenticatorStrategy策略

public class OnlyOneAuthenticatorStrategy extends AbstractAuthenticationStrategy {

	//在所有Realm验证之前调用
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();//返回一个权限的认证信息
    }

    //在每个realm验证之前调用
    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;//返回上一个realm与aggregate合并后的aggregate
    }
    //在每个Realm验证之后调用
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
		//当realm验证出错时，singleRealmInfo为空
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else {
            if (aggregateInfo == null) {
                info = singleRealmInfo;
            } else {
                info = merge(singleRealmInfo, aggregateInfo);
				//如果超过一个realm验证成功了，那么抛出异常
                if(info.getPrincipals().getRealmNames().size() > 1) {
                    System.out.println(info.getPrincipals().getRealmNames());
                    throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                            "could not be authenticated by any configured realms.  Please ensure that only one realm can " +
                            "authenticate these tokens.");
                }
            }
        }


        return info;
    }

    //在所有Realm验证之后调用
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}
