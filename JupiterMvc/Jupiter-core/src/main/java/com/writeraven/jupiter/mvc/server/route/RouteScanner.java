package com.writeraven.jupiter.mvc.server.route;

import com.writeraven.jupiter.mvc.server.annotation.Action;
import com.writeraven.jupiter.mvc.server.annotation.Route;
import com.writeraven.jupiter.mvc.server.config.AppConfig;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.enums.StatusEnum;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import com.writeraven.jupiter.mvc.server.reflect.ClassScanner;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/11/13 01:48
 * @since JDK 1.8
 */
public class RouteScanner {

    private static Map<String, Method> routes = null;

    private volatile static RouteScanner routeScanner;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     *
     * @return
     */
    public static RouteScanner getInstance() {
        if (routeScanner == null) {
            synchronized (RouteScanner.class) {
                if (routeScanner == null) {
                    routeScanner = new RouteScanner();
                }
            }
        }
        return routeScanner;
    }

    private RouteScanner() {
    }

    /**
     * get route method
     *
     * @param queryStringDecoder
     * @return
     * @throws Exception
     */
    public Method routeMethod(QueryStringDecoder queryStringDecoder) throws Exception {
        if (routes == null) {
            routes = new HashMap<>(16);
            loadRouteMethods(appConfig.getRootPackageName());
        }

        //default response
        boolean defaultResponse = defaultResponse(queryStringDecoder.path());

        if (defaultResponse) {
            return null;
        }

        Method method = routes.get(queryStringDecoder.path());

        if (method == null) {
            throw new JupiterException(StatusEnum.NOT_FOUND);
        }

        return method;
    }

    private boolean defaultResponse(String path) {
        if (appConfig.getRootPath().equals(path)) {
            JupiterContext.getContext().html("<center> Hello Jupiter <br/><br/>" +
                    "Power by <a href='https:/Jupiter'>@Jupiter</a> </center>");
            return true;
        }
        return false;
    }


    /**
     * 暴力法，扫描所有类，并且扫描类的方法
     * @param packageName
     * @throws Exception
     */
    private void loadRouteMethods(String packageName) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses(packageName);

        for (Class<?> aClass : classes) {
            Method[] declaredMethods = aClass.getMethods();

            for (Method method : declaredMethods) {
                Route annotation = method.getAnnotation(Route.class);
                if (annotation == null) {
                    continue;
                }

                Action Action = aClass.getAnnotation(Action.class);
                routes.put(appConfig.getRootPath() + "/" + Action.value() + "/" + annotation.value(), method);
            }
        }
    }
}
