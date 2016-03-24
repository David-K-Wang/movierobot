package com.wk.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class ClassBeanFactory implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory f) throws BeansException {
        ClassBeanFactory.beanFactory = f;
    }

    public static Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return (T) beanFactory.getBean(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean containBean(String name) {
        return beanFactory.containsBean(name);
    }
}
