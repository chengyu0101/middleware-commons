package com.yujian.middleware.commons.support;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.io.*;
import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public final class YjDBPasswordFactory implements FactoryBean<String> {

    private static final String  LOCAL_KEY_MARK      = "@";
    private static final String  LOCAL_KEY_FILE_PATH = "/root/private/dbsecretkey";
    private static final String  DEFAULT_KEY         = "strong";
    @Deprecated
    private static final String  ENV_CMD_OLD         = "db.passwd.protect";
    private static final String  ENV_CMD             = "middleware.db.protect";
    private static final boolean ENABLE;

    static {
        if (Boolean.FALSE.toString().equalsIgnoreCase(System.getProperty(ENV_CMD, System.getProperty(ENV_CMD_OLD)))) {
            ENABLE = false;
        } else {
            ENABLE = true;
        }

        if (!ENABLE) {
            System.out.println("YjDBPasswordFactory is disable!!");
        }
    }

    /**
     * 密文
     */
    @Setter
    private String password;

    /**
     * 使用默认值即可
     */
    @Setter
    private String localKeyFilePath = LOCAL_KEY_FILE_PATH;

    @Override
    public String getObject() throws Exception {
        if (ENABLE) {
            return password == null ? null : decode(password, localKeyFilePath);
        } else {
            return password;
        }
    }

    @Override
    public Class<String> getObjectType() {
        return String.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private static String decode(String secret, String localKeyFilePath) throws Exception {
        String keyString = secret.startsWith(LOCAL_KEY_MARK) ? readKeyFromLocalFile(localKeyFilePath) : DEFAULT_KEY;
        String secretString = secret.startsWith(LOCAL_KEY_MARK) ? secret.substring(LOCAL_KEY_MARK.length()) : secret;
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyString.getBytes(), "Blowfish"));
        byte[] decode = cipher.doFinal(new BigInteger(secretString, 16).toByteArray());
        return new String(decode);
    }

    private static String readKeyFromLocalFile(String localFile) throws IOException {
        if (localFile == null || localFile.trim().length() == 0) {
            throw new IllegalArgumentException("密码加解密采用本地Key文件方式，但是文件路径被设置为空，请检查!");
        }
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new FileReader(localFile));
            } catch (FileNotFoundException ex) {
                throw new IllegalStateException(String.format("密码加解密采用本地Key文件方式，但是文件%s不存在或者无访问权限，请联系运维配置!", localFile));
            }
            String keyString = reader.readLine();
            if (keyString == null || (keyString = keyString.trim()).length() == 0) {
                throw new IllegalStateException(String.format("密码加解密采用本地Key文件方式，但是文件%s内容为空，请联系运维配置!", localFile));
            }
            return keyString;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
