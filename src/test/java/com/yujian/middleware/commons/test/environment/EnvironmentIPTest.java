package com.yujian.middleware.commons.test.environment;

import com.yujian.middleware.commons.util.YjEnvironmentHelper;
import org.junit.Test;

import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class EnvironmentIPTest {

    private static final String DEV_ADDRESS_REGEX = "^192.168.(16|20|21).\\d{1,3}$";
    private static final String TEST_ADDRESS_REGEX = "^172.20.100.\\d{1,3}$";
    private static final String PRE_PRODUCT_ADDRESS_REGEX = "^172.21.43.\\d{1,3}$";
    private static final String PRODUCT_ADDRESS_REGEX = "^172.(20.(?!100)\\d{1,3}|21.(?!43)\\d{1,3}).\\d{1,3}$";

    @Test
    public void getHostName() {
        Map<String, String> envs = System.getenv();
        String computerName = envs.get("HOSTNAME");
        System.out.println(computerName);
    }

    @Test
    public void localTest(){
        getEnvInfo("192.168.1.100");
        getEnvInfo("192.168.25.100");
        getEnvInfo("192.168.100.100");
    }

    @Test
    public void devTest(){
        getEnvInfo("192.168.16.100");
        getEnvInfo("192.168.20.100");
        getEnvInfo("192.168.21.100");
    }

    @Test
    public void testTest(){
        getEnvInfo("172.20.100.100");
        getEnvInfo("172.20.100.1");
        getEnvInfo("172.20.100.56");
    }

    @Test
    public void preProductTest(){
        getEnvInfo("172.22.1.100");
        getEnvInfo("172.22.1.1");
        getEnvInfo("172.22.1.56");
        getEnvInfo("172.21.43.56");
    }

    @Test
    public void productTest(){
        getEnvInfo("172.20.5.100");
        getEnvInfo("172.20.203.1");
        getEnvInfo("172.20.1.56");
        getEnvInfo("172.21.43.56");
        getEnvInfo("172.20.100.56");
        getEnvInfo("172.21.100.56");
    }

    public void getEnvInfo(String address) {
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.Environment.UNKNOWN;
        // 开发环境
        if (Pattern.matches(DEV_ADDRESS_REGEX, address)){
            environment = YjEnvironmentHelper.Environment.DEV;
        } else if (Pattern.matches(TEST_ADDRESS_REGEX, address)) { // 测试
            environment = YjEnvironmentHelper.Environment.TEST;
        } else if (Pattern.matches(PRE_PRODUCT_ADDRESS_REGEX, address)) { // 预发布
            environment = YjEnvironmentHelper.Environment.PRE_PRODUCT;
        } else if (Pattern.matches(PRODUCT_ADDRESS_REGEX, address)) { // 生产
            environment = YjEnvironmentHelper.Environment.PRODUCT;
        }
        System.out.println(environment);
    }
}
