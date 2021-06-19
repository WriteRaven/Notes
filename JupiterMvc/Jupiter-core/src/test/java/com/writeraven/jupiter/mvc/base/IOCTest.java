package com.writeraven.jupiter.mvc.base;

import com.writeraven.jupiter.mvc.base.bean.BeanFactory;
import com.writeraven.jupiter.mvc.base.ioc.JupiterIOC;
import com.writeraven.jupiter.mvc.server.reflect.ClassScanner;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

/**
 * @class: Jupiter
 * @description:
 * @author: cyj
 * @create: 2021-05-21 18:21
 **/
public class IOCTest {

    /**
     * spi机制获取IOCtest
     */
    @Test
    public void getBeanFacoty(){
        BeanFactory beanFactory =  ClassScanner.getBeanFactory();
        System.out.println(beanFactory.getClass());
    }

    @Test
    public void beanLifeCycle(){
        JupiterIOC jupiterIOC = new JupiterIOC();

        TestBean bean = new TestBean("bean name", 10001);

        // register
        jupiterIOC.register(bean);
        System.out.println("registered");

        // get bean
        System.out.println(jupiterIOC.getBean(bean.getClass()));

        //release bean
        jupiterIOC.releaseBean();
        System.out.println("released");
    }


    @Data
    @AllArgsConstructor
    class TestBean{
        private String name;
        private int age;
    }
}
