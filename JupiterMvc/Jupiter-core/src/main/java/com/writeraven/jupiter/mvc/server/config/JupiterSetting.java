package com.writeraven.jupiter.mvc.server.config;

import com.writeraven.jupiter.mvc.server.JupiterServer;
import com.writeraven.jupiter.mvc.server.bean.BeanManager;
import com.writeraven.jupiter.mvc.server.bootstrap.InitializeHandler;
import com.writeraven.jupiter.mvc.server.configuration.AbstractConfiguration;
import com.writeraven.jupiter.mvc.server.configuration.ApplicationConfiguration;
import com.writeraven.jupiter.mvc.server.configuration.ConfigurationHolder;
import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import com.writeraven.jupiter.mvc.server.reflect.ClassScanner;
import com.writeraven.jupiter.mvc.server.thread.ThreadLocalHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/10 20:29
 * @since JDK 1.8
 */
public final class JupiterSetting {

    /**
     * @param clazz
     * @param rootPath
     * @throws Exception
     */
    public static void setting(Class<?> clazz, String rootPath) throws Exception {

        // Jupiter logo
        logo();

        //Initialize the application configuration
        initConfiguration(clazz);

        //Set application configuration
        setAppConfig(rootPath);

        //initBean route bean factory
        BeanManager.getInstance().initBeans(rootPath);
    }


    public static void initHandle() throws Exception{
        List<Class<?>> configuration = ClassScanner.getInitHandles(AppConfig.getInstance().getRootPackageName());
        for (Class<?> clazz : configuration) {
            InitializeHandler handle = (InitializeHandler) clazz.getConstructor().newInstance();
            handle.handle();
        }
    }


    private static void logo() {
        System.out.println(JupiterConstant.SystemProperties.LOGO);
        Thread.currentThread().setName(JupiterConstant.SystemProperties.APPLICATION_THREAD_MAIN_NAME) ;
    }


    /**
     * Set application configuration
     *
     * @param rootPath
     */
    private static void setAppConfig(String rootPath) {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);

        if (rootPath == null) {
            rootPath = applicationConfiguration.get(JupiterConstant.ROOT_PATH);
        }
        String port = applicationConfiguration.get(JupiterConstant.Jupiter_PORT);

        if (rootPath == null) {
            throw new JupiterException("No [Jupiter.root.path] exists ");
        }
        if (port == null) {
            throw new JupiterException("No [Jupiter.port] exists ");
        }
        AppConfig.getInstance().setRootPath(rootPath);
        AppConfig.getInstance().setPort(Integer.parseInt(port));
    }


    /**
     * Initialize the application configuration
     *
     * @param clazz
     * @throws Exception
     */
    private static void initConfiguration(Class<?> clazz) throws Exception {
        ThreadLocalHolder.setLocalTime(System.currentTimeMillis());
        AppConfig.getInstance().setRootPackageName(clazz);

        List<Class<?>> configuration = ClassScanner.getConfigurations(AppConfig.getInstance().getRootPackageName());
        if (Objects.isNull(configuration) || configuration.isEmpty()){
            return ;
        }
        for (Class<?> aClass : configuration) {
            AbstractConfiguration conf = (AbstractConfiguration) aClass.getConstructor().newInstance();

            // First read
            InputStream stream ;
            String systemProperty = System.getProperty(conf.getPropertiesName());
            if (systemProperty != null) {
                stream = new FileInputStream(new File(systemProperty));
            } else {
                stream = JupiterServer.class.getClassLoader().getResourceAsStream(conf.getPropertiesName());
            }

            Properties properties = new Properties();
            properties.load(stream);
            conf.setProperties(properties);

            // add configuration cache
            ConfigurationHolder.addConfiguration(aClass.getName(), conf);
        }
    }
}
