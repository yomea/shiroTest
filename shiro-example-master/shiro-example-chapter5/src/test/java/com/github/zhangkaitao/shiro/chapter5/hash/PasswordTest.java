package com.github.zhangkaitao.shiro.chapter5.hash;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.junit.Test;

/**
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-1-27
 * <p>
 * Version: 1.0
 */
public class PasswordTest extends BaseTest {

	@Test
	public void testPasswordServiceWithMyRealm() {
		login("classpath:shiro-passwordservice.ini", "wu", "123");
	}

	// 如果没有配置passWordservice之类的服务，则不会进行编码操作
	@Test
	public void test() {
		login("classpath:shiro.ini", "zhang", "123");
	}

	@Test
	public void testPasswordServiceWithJdbcRealm() {
		login("classpath:shiro-jdbc-passwordservice.ini", "wu", "123");

	}

	@Test
	public void testGeneratePassword() {
		String algorithmName = "md5";
		String username = "liu";
		String password = "123";
		String salt1 = username;
		String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
		int hashIterations = 2;

		SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
		String encodedPassword = hash.toHex();
		System.out.println(salt2);// 这个是随机生成的0072273a5d87322163795118fdd7c45e
		System.out.println(encodedPassword);// be320beca57748ab9632c4121ccac0db
	}

	@Test
	public void testHashedCredentialsMatcherWithMyRealm2() {
		// 使用testGeneratePassword生成的散列密码
		login("classpath:shiro-hashedCredentialsMatcher.ini", "liu", "123");

	}

	@Test
	public void testHashedCredentialsMatcherWithJdbcRealm() {
		// 此处还要注意Shiro默认使用了apache commons
		// BeanUtils，默认是不进行Enum类型转型的，此时需要自己注册一个Enum转换器“BeanUtilsBean.getInstance().getConvertUtils().register(new
		// EnumConverter(), JdbcRealm.SaltStyle.class);”
		BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);

		// 使用testGeneratePassword生成的散列密码
		login("classpath:shiro-jdbc-hashedCredentialsMatcher.ini", "liu", "123");
	}

	private class EnumConverter extends AbstractConverter {
		@Override
		protected String convertToString(final Object value) throws Throwable {
			return ((Enum) value).name();
		}

		@Override
		protected Object convertToType(final Class type, final Object value) throws Throwable {
			return Enum.valueOf(type, value.toString());
		}

		@Override
		protected Class getDefaultType() {
			return null;
		}

	}

	@Test(expected = ExcessiveAttemptsException.class)
	public void testRetryLimitHashedCredentialsMatcherWithMyRealm() {
		for (int i = 1; i <= 5; i++) {
			try {
				login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
			} catch (Exception e) {
				/* ignore */}
		}
		login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
	}
}
