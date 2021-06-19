package com.writeraven.jupiter.mvc.base.ioc;

import com.writeraven.jupiter.mvc.base.bean.BeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @class: Jupiter
 * @description:
 * @author: cyj
 * @create: 2021-04-25 15:28
 **/
@Slf4j
public class JupiterIOC implements BeanFactory {

    private static Map<String, Object> beans = new ConcurrentHashMap<>(16);

    @Override
    public void register(Object object) {
        beans.put(object.getClass().getName(), object);
    }

    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) getBean(clazz.getName());
    }

    @Override
    public void releaseBean() {
        beans = null;
//        log.info("Successfully release all bean.");
    }
}
