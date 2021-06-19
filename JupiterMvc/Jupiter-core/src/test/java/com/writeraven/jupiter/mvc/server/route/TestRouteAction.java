package com.writeraven.jupiter.mvc.server.route;

import com.writeraven.jupiter.mvc.server.annotation.Action;
import com.writeraven.jupiter.mvc.server.annotation.Route;

/**
 * @class: Jupiter
 * @description:
 * @author: cyj
 * @create: 2021-05-21 19:06
 **/
@Action("/test")
public class TestRouteAction {

    @Route("/hello")
    public void hello() {
        System.out.println("/test/hello" + "\t" + "TestRout.hello");
    }

    @Route("/hi")
    public void hi() {
        System.out.println("/test/hi" + "\t" + "TestRout.hi");
    }

}
