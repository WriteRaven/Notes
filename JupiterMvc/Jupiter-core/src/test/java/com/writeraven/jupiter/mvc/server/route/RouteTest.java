package com.writeraven.jupiter.mvc.server.route;

import com.writeraven.jupiter.mvc.server.annotation.Route;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @class: Jupiter
 * @description:
 * @author: cyj
 * @create: 2021-05-21 19:18
 **/
public class RouteTest {

    @Test
    public void getRouteMethod() throws Exception {
        RouteScanner scanner = RouteScanner.getInstance();
        QueryStringDecoder decoder = new QueryStringDecoder("/test/hello");

        Method method = scanner.routeMethod(decoder);

        System.out.println(method.getName());

    }

}
