package com.businessextractor.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Singleton-like class containing and providing a reference to the Spring application context.
 * 
 * It also contains a service locator bean ( getBean(String beanName)) which facilitates easy access to
 * spring managed beans.
 * @author user
 *
 */
public class ApplicationContextHolder {

	/**
	 *  The application context reference used in the unit tests
	 */
	private static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
		"applicationContext.xml");

	/**
	 * 
	 * Retrieves the application context.
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getContext() {
		return APPLICATION_CONTEXT;
	}
	
}
