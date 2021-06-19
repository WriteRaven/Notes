package com.writeraven.jupiter.mvc.server.reflect;

import com.writeraven.jupiter.mvc.base.bean.BeanFactory;
import com.writeraven.jupiter.mvc.server.annotation.Action;
import com.writeraven.jupiter.mvc.server.annotation.Bean;
import com.writeraven.jupiter.mvc.server.annotation.Interceptor;
import com.writeraven.jupiter.mvc.server.configuration.AbstractConfiguration;
import com.writeraven.jupiter.mvc.server.configuration.ApplicationConfiguration;
import com.writeraven.jupiter.mvc.server.enums.StatusEnum;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import lombok.extern.slf4j.Slf4j;
import com.writeraven.jupiter.mvc.base.bean.DefaultBean;
import com.writeraven.jupiter.mvc.server.bootstrap.InitializeHandler;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Function: package Scanner
 *          获取configrations,
 *          Beans,
 *          jupiterInterceptor,
 *          BeanFactory,
 *
 *          getClasses,
 *
 * @describtion: 可以做适当的代码重构，进一步抽象，增加复用
 *
 * @author  cyj
 *         Date: 2021/2/1 11:36
 * @since JDK 1.8
 */
@Slf4j
public final class ClassScanner {

    private static Map<String, Class<?>> beansMap = null;
    private static Map<Class<?>, Integer> interceptorMap = null;

    private static Set<Class<?>> classes = null;
    private static Set<Class<?>> jupiter_classes = null;

    private static List<Class<?>> configurationList = null;

    private static List<Class<?>> initHandlerList = null;



    /**
     * get Configuration
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getConfigurations(String packageName) throws Exception {

        if (configurationList == null) {
            // 获取目标classes
            Set<Class<?>> clsList = getClasses(packageName);

            // if there is target class. if not, return null.
            if (clsList == null || clsList.isEmpty()) {
                return null;
            }

            // Manually add ApplicationConfiguration. Then add others as following.
            clsList.add(ApplicationConfiguration.class) ;

            configurationList = new ArrayList<>(16);
            for (Class<?> clz : clsList) {

                if (clz.getSuperclass() != AbstractConfiguration.class) {
                    continue;
                }

                // add selected class to the list.
                configurationList.add(clz) ;
            }
        }
        return configurationList;
    }

    /**
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getInitHandles(String packageName) throws Exception{
        if (initHandlerList == null){
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return null;
            }

            initHandlerList = new ArrayList<>(16);
            for (Class<?> clz : clsList) {

                if (clz.getSuperclass() != InitializeHandler.class) {
                    continue;
                }
                initHandlerList.add(clz) ;
            }
        }
        return initHandlerList ;
    }

    /**
     * get @Action & @Bean
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<String, Class<?>> getBeans(String packageName) throws Exception {

        if (beansMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return beansMap;
            }

            beansMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {

                Action action = cls.getAnnotation(Action.class);
                Bean bean = cls.getAnnotation(Bean.class);
                if (action == null && bean == null) {
                    continue;
                }

                // action 也是jupiter bean
                if (action != null){
                    beansMap.put(action.value() == null ? cls.getName() : action.value(), cls);
                }

                if (bean != null){
                    beansMap.put(bean.value() == null ? cls.getName() : bean.value(), cls);
                }

            }
        }
        return beansMap;
    }

    /**
     * whether is the target class
     * @param clazz
     * @param target
     * @return
     */
    // todo problem  根据代码,应该是是否含有目标class
    public static boolean isInterface(Class<?> clazz,Class<?> target){
        for (Class<?> aClass : clazz.getInterfaces()) {
            if (aClass.getName().equals(target.getName())){
                return true ;
            }
        }
        return false ;
    }

    /**
     * get @Interceptor
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<Class<?>, Integer> getJupiterInterceptor(String packageName) throws Exception {

        if (interceptorMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return interceptorMap;
            }

            interceptorMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {
                Annotation annotation = cls.getAnnotation(Interceptor.class);
                if (annotation == null) {
                    continue;
                }

                Interceptor interceptor = (Interceptor) annotation;
                interceptorMap.put(cls, interceptor.order());

            }
        }

        return interceptorMap;
    }

    /**
     * get All classes
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception {

        if (classes == null){
            classes = new HashSet<>(32) ;

            baseScanner(packageName,classes);
        }

        return classes;
    }

    private static void baseScanner(String packageName,Set res) {
        boolean recursive = true;

        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, res);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        res.add(Class.forName(packageName + '.' + className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("ClassScanner.baseScanner has IOException", e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("ClassScanner.baseScanner has IOException", e);
        }
    }


    private static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        for (File file : files) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.error("ClassNotFoundException", e);
                }
            }
        }
    }



    /**
     * get JupiterFactory object by spi.
     * @return
     */
    public static BeanFactory getBeanFactory() {
        // serviceLoader - spi key class
        ServiceLoader<BeanFactory> BeanFactories = ServiceLoader.load(BeanFactory.class);
        if (BeanFactories.iterator().hasNext()){
            return BeanFactories.iterator().next() ;
        }

        return new DefaultBean();
    }


    @Deprecated
    private static final String BASE_PACKAGE = "com.writeraven.Jupiter";

//    /**
//     * get custom route bean
//     * @return
//     * @throws Exception
//     */
//    @Deprecated
//    public static Class<?> getBeanFactory() throws Exception {
//        List<Class<?>> classList = new ArrayList<>();
//
//
//        Set<Class<?>> classes = getCustomRouteBeanClasses(BASE_PACKAGE) ;
//        for (Class<?> aClass : classes) {
//
//            if (aClass.getInterfaces().length == 0){
//                continue;
//            }
//            if (aClass.getInterfaces()[0] != BeanFactory.class){
//                continue;
//            }
//            classList.add(aClass) ;
//        }
//
//        if (classList.size() > 2){
//            throw new JupiterException(StatusEnum.DUPLICATE_IOC);
//        }
//
//        if (classList.size() == 2){
//            Iterator<Class<?>> iterator = classList.iterator();
//            while (iterator.hasNext()){
//                if (iterator.next()== DefaultBean.class){
//                    iterator.remove();
//                }
//            }
//        }
//
//        return classList.get(0);
//    }

    @Deprecated
    private static Set<Class<?>> getCustomRouteBeanClasses(String packageName) throws Exception {

        if (jupiter_classes == null){
            jupiter_classes = new HashSet<>(32) ;

            baseScanner(packageName,jupiter_classes);
        }

        return jupiter_classes;
    }
}
