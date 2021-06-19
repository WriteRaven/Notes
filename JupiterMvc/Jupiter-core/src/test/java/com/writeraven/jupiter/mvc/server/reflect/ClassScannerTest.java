package com.writeraven.jupiter.mvc.server.reflect;

import org.junit.Test;

import java.util.Set;

public class ClassScannerTest {
    @Test
    public void getJupiterClasses() throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses("com.writeraven.jupiter.mvc.server.reflect");
        System.out.println(classes.size());
        for(Class c : classes){
            System.out.println(c.getName());
        }
    }

}