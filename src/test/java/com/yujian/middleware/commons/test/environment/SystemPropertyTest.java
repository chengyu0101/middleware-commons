package com.yujian.middleware.commons.test.environment;

import com.yujian.middleware.commons.util.YjEnvironmentHelper;
import org.junit.Test;

/**
 *
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class SystemPropertyTest {

    @Test
    public void localTest(){
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void devTest(){
        System.setProperty("WDEnvironment", "DEV");
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void testTest(){
        System.setProperty("WDEnvironment", "test");
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void preProductTest(){
        System.setProperty("WDEnvironment", "pre_product");
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void productTest(){
        System.setProperty("WDEnvironment", "Product");
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

}
