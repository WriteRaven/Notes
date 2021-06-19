package com.writeraven.jupiter.mvc.server.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {

    String value() default "" ;
}
