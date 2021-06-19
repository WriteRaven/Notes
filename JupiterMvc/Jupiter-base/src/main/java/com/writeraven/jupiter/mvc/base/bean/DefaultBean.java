package com.writeraven.jupiter.mvc.base.bean;


/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/11/14 01:26
 * @since JDK 1.8
 */
public class DefaultBean implements BeanFactory {

    @Override
    public void register(Object object) {

    }

    @Override
    public Object getBean(String name) throws Exception {
        Class<?> aClass = Class.forName(name);
        return aClass.getConstructor().newInstance();
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        return clazz.getConstructor().newInstance();
    }

    @Override
    public void releaseBean() {
    }
}
