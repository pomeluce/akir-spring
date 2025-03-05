package org.pomeluce.akir;

import org.pomeluce.akir.common.security.ECKeyPairGenerator;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2025/1/24 22:50
 * @className : ECKeyPairGeneratorTest
 * @description : ES256 密钥对生成测试类
 */
public class ECKeyPairGeneratorTest {
    public static void main(String[] args) {
        try {
            ECKeyPairGenerator eck = ECKeyPairGenerator.build();
            eck.write("EC1");
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
