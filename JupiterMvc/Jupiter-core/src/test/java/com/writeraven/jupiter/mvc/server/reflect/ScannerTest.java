package com.writeraven.jupiter.mvc.server.reflect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ScannerTest {


    @Test
    public void getClasses() throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses("com.writeraven.jupiter.server");

        log.info("classes=[{}]", JSON.toJSONString(classes));
    }


    @Test
    public void getActionAction() throws Exception{
        Map<String, Class<?>> Action = ClassScanner.getBeans("com.writeraven.jupiter.server");
        log.info("classes=[{}]", JSON.toJSONString(Action));
    }


    @Test
    public void getConfiguration() throws Exception {
        List<Class<?>> configuration = ClassScanner.getConfigurations("com.writeraven.jupiter.server");
        log.info("configuration=[{}]",configuration.toString());
    }


    @Test
    public void stringTest(){
        String text = "/Jupiter-example/routeAction/getUser" ;
        text = text.replace("/Jupiter-example","");
        log.info(text);
    }



}