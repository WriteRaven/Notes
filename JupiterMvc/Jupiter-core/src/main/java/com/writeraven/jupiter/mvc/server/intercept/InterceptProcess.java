package com.writeraven.jupiter.mvc.server.intercept;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.config.AppConfig;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.reflect.ClassScanner;

import java.util.*;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2021/3/21 18:42
 * @since JDK 1.8
 */
public class InterceptProcess {

    private InterceptProcess() {
    }

    private volatile static InterceptProcess process;

    private static List<AbstractInterceptor> interceptors;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     *
     * @return
     */
    public static InterceptProcess getInstance() {
        if (process == null) {
            synchronized (InterceptProcess.class) {
                if (process == null) {
                    process = new InterceptProcess();
                }
            }
        }
        return process;
    }


    /**
     * 加载执行责任链
     * @throws Exception
     */
    public void loadInterceptors() throws Exception {

        if (interceptors != null) {
            return;
        } else {
            interceptors = new ArrayList<>(10);
            Map<Class<?>, Integer> JupiterInterceptor = ClassScanner.getJupiterInterceptor(appConfig.getRootPackageName());
            for (Map.Entry<Class<?>, Integer> classEntry : JupiterInterceptor.entrySet()) {
                Class<?> interceptorClass = classEntry.getKey();
                AbstractInterceptor interceptor = (AbstractInterceptor) interceptorClass.getConstructor().newInstance();
                interceptor.setOrder(classEntry.getValue());
                interceptors.add(interceptor);
            }
            Collections.sort(interceptors, new Comparator() {
                // inscrement
                @Override
                public int compare(Object o1, Object o2) {
                    if (((AbstractInterceptor) o1).getOrder() <= ((AbstractInterceptor) o2).getOrder()) {
                        return 1;
                    }
                    return 0;
                }
            });

        }
    }


    /**
     * execute before
     *
     * @param param
     * @throws Exception
     */
    public boolean processBefore(Param param) throws Exception {
        for (AbstractInterceptor interceptor : interceptors) {
            boolean access = interceptor.before(JupiterContext.getContext(), param);
            if (!access) {
                return access;
            }
        }
        return true;
    }

    /**
     * execute after
     *
     * @param param
     * @throws Exception
     */
    public void processAfter(Param param) throws Exception {
        for (AbstractInterceptor interceptor : interceptors) {
            interceptor.after(JupiterContext.getContext(), param);
        }
    }
}
