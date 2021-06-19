package com.writeraven.jupiter.mvc.example.configuration;

import com.writeraven.jupiter.mvc.server.configuration.AbstractConfiguration;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/8 16:28
 * @since JDK 1.8
 */
public class RedisConfiguration extends AbstractConfiguration {


    public RedisConfiguration() {
        super.setPropertiesName("redis.properties");
    }

}
