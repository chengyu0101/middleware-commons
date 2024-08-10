
package com.yujian.middleware.commons.test.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class ClassLocationUtils {

    public static String where(final Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        final String classFile = clazz.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = clazz.getProtectionDomain();
        if (pd != null && pd.getCodeSource() != null && pd.getCodeSource().getLocation() != null) {
            URL result = pd.getCodeSource().getLocation();
            if ("file".equals(result.getProtocol())) {
                try {
                    if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip")) {// jar包中类
                        result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(classFile));
                    } else if (new File(result.getFile()).isDirectory()) { // 应用自己classes路径下类
                        result = new URL(result, classFile);
                    }
                } catch (MalformedURLException ignore) {
                }
            }
            return result.toString();
        }
        // jdk中所有类，包括扩展包
        final ClassLoader classLoader = clazz.getClassLoader();
        URL result = classLoader != null ? classLoader.getResource(classFile) : ClassLoader.getSystemResource(classFile);
        return result.toString();
    }

//    public static void main(String[] args) {
//        System.out.println(where(ClassLocationUtils.class));
//        System.out.println(where(JSON.class));
//        System.out.println(where(Object.class));
//        System.out.println(where(SecretKey.class));
//        System.out.println(findPossiblePathList("org/slf4j/impl/StaticLoggerBinder.class"));
//        System.out.println(findPossiblePathList("org/apache/commons/logging/LogFactory.class"));
//    }

    public static List<URL> findPossiblePathList(String classPath) {
        List<URL> pathList = new ArrayList<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = ClassLocationUtils.class.getClassLoader();
            Enumeration<URL> paths;
            if (loggerFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(classPath);
            } else {
                paths = loggerFactoryClassLoader.getResources(classPath);
            }
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                pathList.add(path);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return pathList;
    }

}
