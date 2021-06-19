package com.writeraven.jupiter.mvc.server.util;

import com.writeraven.jupiter.mvc.server.config.AppConfig;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/1 17:46
 * @since JDK 1.8
 */
public class PathUtil {


    /**
     * Get Root Path
     * /Jupiter-example/demoAction
     * @param path
     * @return Jupiter-example
     */
    public static String getRootPath(String path) {
        return "/" + path.split("/")[1];
    }

    /**
     * Get Action Path
     * /Jupiter-example/demoAction
     * @param path
     * @return demoAction
     */
    public static String getActionPath(String path) {
        return path.split("/")[2];
    }

    /**
     * Get Action Path
     * /Jupiter-example/routeAction/getUser
     * @param path
     * @return getUser
     */
    public static String getRoutePath(String path) {
        AppConfig instance = AppConfig.getInstance();
        return path.replace(instance.getRootPackageName(),"") ;
    }


}
