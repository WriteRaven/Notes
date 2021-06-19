package com.writeraven.jupiter.mvc.server.configuration;

import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/8 16:28
 * @since JDK 1.8
 */
public class ApplicationConfiguration extends AbstractConfiguration {

    public ApplicationConfiguration() {
        super.setPropertiesName(JupiterConstant.SystemProperties.APPLICATION_PROPERTIES);
    }

}
