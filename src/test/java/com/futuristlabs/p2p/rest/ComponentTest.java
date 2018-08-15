package com.futuristlabs.p2p.rest;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ComponentTest {

    private static ApplicationContext context;

    @BeforeClass
    public static void initComponent() {
        context = new ClassPathXmlApplicationContext("spring-config.xml");
    }

    @SuppressWarnings("unchecked")
    protected static <T> T init(T bean) {
        return (T) context.getBean(bean.getClass());
    }

    protected static <T> T instance(Class<T> clazz) {
        return context.getBean(clazz);
    }

}
