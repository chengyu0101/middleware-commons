package com.yujian.middleware.commons.test.environment;

import com.yujian.middleware.commons.util.YjEnvironmentHelper;
import org.junit.Test;

/**
 *
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class LocalFileTest {

    @Test
    public void localTest(){
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void devTest(){
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void testTest(){
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void preProductTest(){
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

    @Test
    public void productTest(){
        YjEnvironmentHelper.Environment environment = YjEnvironmentHelper.getEnvironment();
        System.out.println(environment);
    }

}
