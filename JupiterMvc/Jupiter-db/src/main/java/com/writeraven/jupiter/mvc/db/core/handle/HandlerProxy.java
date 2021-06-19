package com.writeraven.jupiter.mvc.db.core.handle;

import com.writeraven.jupiter.mvc.db.listener.DataChangeListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Function:
 *      jdk proxy
 * @author  cyj
 * Date: 2019-12-04 00:00
 * @since JDK 1.8
 */
@Slf4j
public class HandlerProxy<T> {

    private Class<T> clazz;

    private DataChangeListener listener;


    public HandlerProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getInstance(DataChangeListener listener) {
        this.listener = listener;
        return getInstance();
    }

    public T getInstance() {
        Object obj =  Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                            new Class[]{clazz},
                            new ProxyInvocation(DBHandler.class));
        return (T) obj;
    }


    /**
     * jdk proxy.newProxyInstance - 事件处理
     *     执行目标对象的方法时,会触发事件处理器的方法,
     *     会把当前执行目标对象的方法作为参数传入
     */
    private class ProxyInvocation implements InvocationHandler {

        private Object target;

        public ProxyInvocation(Class clazz) {
            try {
                this.target = clazz.getConstructor().newInstance();
            } catch (Exception e) {
                log.error("exception={}", e);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // 执行目标对象方法
            Object invoke = method.invoke(target, args);

            if (null != listener) {
                listener.listener(args[0]);
            }
            return invoke;
        }
    }
}
