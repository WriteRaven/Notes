package com.writeraven.jupiter.mvc.server.route;

import io.netty.handler.codec.http.QueryStringDecoder;
import com.writeraven.jupiter.mvc.server.bean.BeanManager;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.enums.StatusEnum;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2018/11/13 21:18
 * @since JDK 1.8
 */
@Slf4j
public final class RouteProcess {

    private volatile static RouteProcess routeProcess;

    private final BeanManager beanManager = BeanManager.getInstance();

    public static RouteProcess getInstance() {
        if (routeProcess == null) {
            synchronized (RouteProcess.class) {
                if (routeProcess == null) {
                    routeProcess = new RouteProcess();
                }
            }
        }
        return routeProcess;
    }

    /**
     * invoke route method
     *
     * @param method
     * @param queryStringDecoder
     * @throws Exception
     */
    public void invoke(Method method, QueryStringDecoder queryStringDecoder) throws Exception {
        if (method == null) {
            return;
        }

        Object[] object = parseRouteParameter(method, queryStringDecoder);
        Object bean = beanManager.getBean(method.getDeclaringClass().getName());
        if (object == null) {
            method.invoke(bean);
        } else {
            method.invoke(bean, object);
        }
    }

    /**
     * parse route's parameter
     * 解析路由参数
     *
     * @param method
     * @param queryStringDecoder
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private Object[] parseRouteParameter(Method method, QueryStringDecoder queryStringDecoder) {

        Object[] instances = null;

        try {
            Class<?>[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length == 0) {
                return null;
            }

            if (parameterTypes.length > 2) {
                throw new JupiterException(StatusEnum.ILLEGAL_PARAMETER);
            }

            instances = new Object[parameterTypes.length];

            for (int i = 0; i < instances.length; i++) {
                //inject Jupiter context instance
                if (parameterTypes[i] == JupiterContext.class) {
                    instances[i] = JupiterContext.getContext();
                } else {
                    //inject custom pojo
                    Class<?> parameterType = parameterTypes[i];
                    Object instance = parameterType.getConstructor().newInstance();

                    Map<String, List<String>> parameters = queryStringDecoder.parameters();
                    // 遍历参数，并且将参数赋值给 参数容器对象
                    for (Map.Entry<String, List<String>> param : parameters.entrySet()) {
                        Field field = parameterType.getDeclaredField(param.getKey());
                        field.setAccessible(true);
                        field.set(instance, parseFieldValue(field, param.getValue().get(0)));
                    }
                    instances[i] = instance;
                }
            }
        } catch (NoSuchFieldException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("NoSuchFieldException", e);
        }
        return instances;
    }


    /**
     * 解析属性对应值
     * @param field
     * @param value
     * @return
     */
    private Object parseFieldValue(Field field, String value) {
        if (value == null) {
            return null;
        }

        Class<?> type = field.getType();
        if ("".equals(value)) {
            boolean base = type.equals(int.class) || type.equals(double.class) ||
                    type.equals(short.class) || type.equals(long.class) ||
                    type.equals(byte.class) || type.equals(float.class);
            if (base) {
                return 0;
            }
        }
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(String.class)) {
            return value;
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (type.equals(Short.class) || type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
            return Byte.parseByte(value);
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }

        // todo replace - to check 正确性
//        try {
//            return Class.forName(type.getTypeName()).getConstructor().newInstance(value);
//        }
//        catch (ClassNotFoundException | NoSuchMethodException e){
//            log.error("ClassNotFoundException ",e);
//        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
//            e.printStackTrace();
//        }
        return null;
        
    }

}
