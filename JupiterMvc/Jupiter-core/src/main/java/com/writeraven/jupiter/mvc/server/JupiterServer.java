package com.writeraven.jupiter.mvc.server;

import com.writeraven.jupiter.mvc.server.bootstrap.BootStrap;
import com.writeraven.jupiter.mvc.server.config.JupiterSetting;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/8/30 12:48
 * @since JDK 1.8
 */
public final class JupiterServer {


    /**
     * Start Jupiter server by path
     * @param clazz
     * @param path
     * @throws Exception
     */
    public static void start(Class<?> clazz,String path) throws Exception {
        JupiterSetting.setting(clazz,path) ;

        JupiterSetting.initHandle();

        BootStrap.startJupiter();
    }


    /**
     * Start the service through the port in the configuration file
     * @param clazz
     * @throws Exception
     */
    public static void start(Class<?> clazz) throws Exception {
        start(clazz,null);
    }

}
