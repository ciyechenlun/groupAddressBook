package com.cmcc.zysoft.groupaddressbook.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private static final String key = "0002000200020002";

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
		throws BeansException {
			try {
				String username = props.getProperty("druid.pool.username");
				if (username != null) {
					props.setProperty("druid.pool.username", Des.Decrypt(username, Des.hex2byte(key)));
				}
				
				String password = props.getProperty("druid.pool.password");
				if (password != null) {
					props.setProperty("druid.pool.password", Des.Decrypt(password, Des.hex2byte(key)));
				}
				
				String url = props.getProperty("druid.pool.url");
				if (url != null) {
					props.setProperty("druid.pool.url", Des.Decrypt(url, Des.hex2byte(key)));
				}
				
				String driverClassName = props.getProperty("druid.pool.driverClassName");
				if(driverClassName != null){
					props.setProperty("druid.pool.driverClassName", Des.Decrypt(driverClassName, Des.hex2byte(key)));
				}
				
				super.processProperties(beanFactory, props);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BeanInitializationException(e.getMessage());
			}
		}
	}
