package com.writeraven.jupiter.mvc.server.bean;

import com.writeraven.jupiter.mvc.base.bean.BeanFactory;
import com.writeraven.jupiter.mvc.server.exception.GlobalHandlerException;
import com.writeraven.jupiter.mvc.server.reflect.ClassScanner;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Function:
 *      初始化beans。
 *      beanFactory 管理，
 *      获取bean,
 * @author  cyj
 * Date: 2018/11/14 01:41
 * @since JDK 1.8
 */

@Slf4j
public final class BeanManager {
    
    private static volatile BeanManager beanManager;

    // bean container
    private static BeanFactory beanFactory;

    private GlobalHandlerException handlerException;

    // 单例模式
    private BeanManager() {
    }

    public static BeanManager getInstance() {
        if (beanManager == null) {
            synchronized (BeanManager.class) {
                if (beanManager == null) {
                    beanManager = new BeanManager();
                }
            }
        }
        return beanManager;
    }

    /**
     * initBean route beans and factory
     *
     * @param packageName
     * @throws Exception
     */
    public void initBeans(String packageName) throws Exception {
        Map<String, Class<?>> beans = ClassScanner.getBeans(packageName);

        beanFactory = ClassScanner.getBeanFactory();

        for (Map.Entry<String, Class<?>> classEntry : beans.entrySet()) {
            Object instance = classEntry.getValue().getConstructor().newInstance();
            // 注册到容器中
            beanFactory.register(instance);

            // todo 不能把interface注册到容器中
            //set exception handler
            if (ClassScanner.isInterface(classEntry.getValue(), GlobalHandlerException.class)) {
                GlobalHandlerException exception = (GlobalHandlerException) instance;
                BeanManager.getInstance().exceptionHandle(exception);
            }
        }

    }


    /**
     * get route bean
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) {
        try {
            return beanFactory.getBean(name);
        } catch (Exception e) {
            log.error("get bean error", e);
        }
        return null;
    }


    /**
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getBean(Class<T> clazz) {
        try {
            return beanFactory.getBean(clazz);
        } catch (Exception e) {
            log.error("get bean error", e);
        }
        return null;
    }

    /**
     * release all beans
     */
    public void releaseBean() {
        beanFactory.releaseBean();
    }

    public void exceptionHandle(GlobalHandlerException ex) {
        handlerException = ex;
    }

    public GlobalHandlerException exceptionHandle() {
        return handlerException;
    }
}
