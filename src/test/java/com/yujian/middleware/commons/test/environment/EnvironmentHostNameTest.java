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
public class EnvironmentHostNameTest {

    private static final String DEV_HOST_NAME_REGEX = "^(gosi|cs|wx|daishu|test|bigdata|jiagou).*";
    private static final String TEST_HOST_NAME_REGEX = "^mdc.*";
    private static final String PRE_PRODUCT_HOST_NAME_REGEX = "^MDC.*";
    private static final String PRODUCT_HOST_NAME_REGEX = "^JF.*";

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
        getEnvInfo("daishu");
        getEnvInfo("gosi-aaaa");
        getEnvInfo("cs-casd-fewfwg-bbb-wdqwf");
        getEnvInfo("wx_12-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
        getEnvInfo("daishu-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
        getEnvInfo("test-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
        getEnvInfo("bigdata-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
        getEnvInfo("jiagou-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
    }

    @Test
    public void testTest(){
        getEnvInfo("mdc");
        getEnvInfo("mdc-aaaa");
        getEnvInfo("mdc-casd-fewfwg-bbb-wdqwf");
        getEnvInfo("mdc_12-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
    }

    @Test
    public void preProductTest(){
        getEnvInfo("MDC");
        getEnvInfo("MDC-aaaa");
        getEnvInfo("MDC-casd-fewfwg-bbb-wdqwf");
        getEnvInfo("MDC_12-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
    }

    @Test
    public void productTest(){
        getEnvInfo("JF");
        getEnvInfo("JF-aaaa");
        getEnvInfo("JF-casd-fewfwg-bbb-wdqwf");
        getEnvInfo("JF_12-3^&$%U$%@!$&%^DCQE!~!YU%$IT");
    }

    public void getEnvInfo(String hostName) {
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.Environment.UNKNOWN;
        // 开发环境
        if (Pattern.matches(DEV_HOST_NAME_REGEX, hostName)){
            environment = YjEnvironmentHelper.Environment.DEV;
        } else if (Pattern.matches(TEST_HOST_NAME_REGEX, hostName)) { // 测试
            environment = YjEnvironmentHelper.Environment.TEST;
        } else if (Pattern.matches(PRE_PRODUCT_HOST_NAME_REGEX, hostName)) { // 预发布
            environment = YjEnvironmentHelper.Environment.PRE_PRODUCT;
        } else if (Pattern.matches(PRODUCT_HOST_NAME_REGEX, hostName)) { // 生产
            environment = YjEnvironmentHelper.Environment.PRODUCT;
        }
        System.out.println(environment);
    }
}
