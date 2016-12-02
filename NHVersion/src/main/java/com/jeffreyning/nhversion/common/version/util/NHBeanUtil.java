package com.jeffreyning.nhversion.common.version.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class NHBeanUtil implements ApplicationContextAware{
	public static ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context=arg0;
	}
	public static Object getNHBean(String beanId){
		return context.getBean(beanId);
	}

}
