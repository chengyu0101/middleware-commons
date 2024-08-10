package com.yujian.middleware.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 判断系统所在的服务器是属于哪个环境的帮助类。
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class YjEnvironmentHelper {

    private static final Logger logger = LoggerFactory.getLogger(YjEnvironmentHelper.class);

    private static final String LOCAL_ENVIRONMENT_FILE = "/data/environment";

    private static final String DEV_ADDRESS_REGEX = "^192.168.(16|20|21).\\d{1,3}$";
    private static final String TEST_ADDRESS_REGEX = "^172.20.100.\\d{1,3}$";
    private static final String PRE_PRODUCT_ADDRESS_REGEX = "^172.21.43.\\d{1,3}$";
    private static final String PRODUCT_ADDRESS_REGEX = "^172.(20.(?!100)\\d{1,3}|21.(?!43)\\d{1,3}).\\d{1,3}$";

    private static final String DEV_HOST_NAME_REGEX = "^(gosi|cs|wx|daishu|test|bigdata|jiagou).*";
    private static final String TEST_HOST_NAME_REGEX = "^mdc.*";
    private static final String PRE_PRODUCT_HOST_NAME_REGEX = "^JF.*";
    private static final String PRODUCT_HOST_NAME_REGEX = "^JF.*";
    public static final String SYSTEM_ENVIRONMENT_PROPERTY_KEY = "WDEnvironment";

    /**
     * 获取应用当前所在服务器所在的环境，判断优先级如下：
     * 1.从java系统属性中获取WDEnvironment获取环境信息（DEV,TEST,PRE_PRODUCT,PRODUCT）
     * 2.从服务器的本地文件{@link YjEnvironmentHelper#LOCAL_ENVIRONMENT_FILE}获取环境信息
     * 3.从ip和环境变量的hostName中判断应用所在的环境
     *
     * @return
     */
    public static Environment getEnvironment(){
        // 默认为unknown
        Environment environment;

        // 1.判断系统变量
        String wdEnvironment = System.getProperty(SYSTEM_ENVIRONMENT_PROPERTY_KEY);
        environment = Environment.convert(wdEnvironment);
        if (!environment.equals(Environment.UNKNOWN)){
            return environment;
        }

        // 2.判断服务器本地配置
        String fileText = readEnvironmentFile();
        environment = Environment.convert(fileText);
        if (!environment.equals(Environment.UNKNOWN)){
            return environment;
        }

        // 3.判断网段和hostName
        InetAddress localAddress = NetUtils.getLocalAddress();
        String address = localAddress.getHostAddress();
        String hostName= readHostName();
        if (isBlank(hostName) || isBlank(address)){
            return environment;
        }

        // 开发环境
        if (Pattern.matches(DEV_HOST_NAME_REGEX, hostName) && Pattern.matches(DEV_ADDRESS_REGEX, address)){
            environment = Environment.DEV;
        } else if (Pattern.matches(TEST_HOST_NAME_REGEX, hostName) && Pattern.matches(TEST_ADDRESS_REGEX, address)) { // 测试
            environment = Environment.TEST;
        } else if (Pattern.matches(PRE_PRODUCT_HOST_NAME_REGEX, hostName) && Pattern.matches(PRE_PRODUCT_ADDRESS_REGEX, address)) { // 预发布
            environment = Environment.PRE_PRODUCT;
        } else if (Pattern.matches(PRODUCT_HOST_NAME_REGEX, hostName) && Pattern.matches(PRODUCT_ADDRESS_REGEX, address)) { // 生产
            environment = Environment.PRODUCT;
        }
        return environment;
    }

    private static String readEnvironmentFile() {
        File localFile = new File(LOCAL_ENVIRONMENT_FILE);
        if (!localFile.exists()) {
            return null;
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(localFile));
            StringBuilder text = new StringBuilder(16);
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                text.append(line);
            }
            return text.toString();
        } catch (IOException e) {
            logger.error("读取" + LOCAL_ENVIRONMENT_FILE + "文件失败！", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                logger.error("读取" + LOCAL_ENVIRONMENT_FILE + "文件关闭输入流失败！", e);
            }
        }
        return null;
    }

    /**
     * 从环境变量中读取HOSTNAME属性
     *
     * @return
     */
    private static String readHostName() {
        Map<String, String> env = System.getenv();
        String hostName = env.get("HOSTNAME");
        return hostName;
    }

    private static boolean isBlank(String string) {
        return string == null || "".equals(string.trim());
    }

    public enum Environment {
        DEV,TEST,PRE_PRODUCT,PRODUCT,UNKNOWN;

        /**
         * 将字符串转化为{@link Environment}
         * 大小写不敏感
         * 未知字符串返回为{@link Environment#UNKNOWN}
         *
         * @param environmentString
         * @return
         */
        public static Environment convert(String environmentString){
            if (environmentString == null || "".equals(environmentString.trim())){
                return Environment.UNKNOWN;
            }
            Environment[] values = values();
            for (Environment value : values) {
                if (value.name().toUpperCase().equals(environmentString.toUpperCase().trim())){
                    return value;
                }
            }
            return Environment.UNKNOWN;
        }
    }

}
