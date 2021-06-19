package com.writeraven.jupiter.mvc.server.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

    String value() default "" ;
}
