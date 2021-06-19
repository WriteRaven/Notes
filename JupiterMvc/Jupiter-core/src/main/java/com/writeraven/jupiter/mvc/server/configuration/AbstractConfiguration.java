package com.writeraven.jupiter.mvc.server.configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/8 16:28
 * @since JDK 1.8
 */
@Setter
@Getter
public abstract class AbstractConfiguration {

    /**
     * file name
     */
    private String propertiesName;

    private Properties properties;


    public String get(String key) {
        return properties.get(key) == null ? null : properties.get(key).toString();
    }

    @Override
    public String toString() {
        return "AbstractJupiterConfiguration{" +
                "propertiesName='" + propertiesName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
