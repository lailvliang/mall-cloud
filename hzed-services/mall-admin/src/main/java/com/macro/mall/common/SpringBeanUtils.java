package com.macro.mall.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author yyq
 */
@Configuration
public  class SpringBeanUtils implements ApplicationContextAware {
	
    private static ApplicationContext applicationContext = null;  
  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeanUtils.applicationContext == null) {
            SpringBeanUtils.applicationContext = applicationContext;
        }  
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;  
    }

    public static <T> T getBean(String name,Class<T> tClass) {

        return getApplicationContext().getBean(name,tClass);
    }  
}  
