package com.writeraven.jupiter.mvc.server.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/9 20:05
 * @since JDK 1.8
 */
public class ConfigurationHolder {

    private static Map<String, AbstractConfiguration> config = new HashMap<>(8) ;

    /**
     * Add holder cache
     * @param key
     * @param configuration
     */
    public static void addConfiguration(String key, AbstractConfiguration configuration){
        config.put(key, configuration);
    }


    /**
     * Get class from cache by class name
     * @param clazz
     * @return
     */
    public static AbstractConfiguration getConfiguration(Class<? extends AbstractConfiguration> clazz){
        return config.get(clazz.getName()) ;
    }
}
