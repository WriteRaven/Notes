package com.writeraven.jupiter.mvc.example.action;

import com.writeraven.jupiter.mvc.server.annotation.Route;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RouteActionTest {


    @Test
    public void reflect() throws Exception {

        Map<Class<?>,Method> routes = new HashMap<>() ;

        Class<?> aClass = Class.forName("com.writeraven.jupiter.example.action.Route");

        Method[] declaredMethods = aClass.getMethods();

        for (Method method : declaredMethods) {


            Route annotation = method.getAnnotation(Route.class) ;
            if (annotation == null){
                continue;
            }

            routes.put(aClass,method) ;
        }

        log.info(routes.toString());

    }

    @Test
    public void reflect2() throws Exception{
        Class<?> aClass = Class.forName("com.writeraven.jupiter.example.action.DemoAction");
        String name = aClass.getName();
        Class<?>[] interfaces = aClass.getInterfaces() ;
//        log.info((interfaces[0].getName() == WorkAction.class.getName()) + "");
    }

    @Test
    public void reflect3(){
        try {
            Class<?> aClass = Class.forName("com.writeraven.jupiter.bean.ioc.JupiterIoc");
            log.info(aClass.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void costTest(){
        Map<Integer,Integer> hashmap = new HashMap<>(16);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            hashmap.put(i,i) ;
        }
        long end = System.currentTimeMillis();
        log.info("hashmap cost time=[{}] size=[{}]",(end -start),hashmap.size());

        hashmap=null;


        Map<Integer,Integer> concurrentHashMap = new ConcurrentHashMap<>(16);
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            concurrentHashMap.put(i,i) ;
        }
        end = System.currentTimeMillis();
        log.info("hashmap cost time=[{}] size=[{}]",(end -start),concurrentHashMap.size());
    }

    @Test
    public void exTest(){

            log.info("===========");
            me();
            log.info("+++++++++++");

           // log.error("e",e);

    }


    private void me(){
        log.info("me");
        return ;
    }
}