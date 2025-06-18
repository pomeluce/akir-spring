package org.pomeluce.akir;

import org.akir.common.utils.security.ECKeyPair;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/1/24 22:50
 * @className : ECKeyPairTest
 * @description : ECC 密钥对生成测试类
 */
public class ECKeyPairTest {
    public static void main(String[] args) {
        try {
            ECKeyPair eck = ECKeyPair.ES256();
            eck.write("EC");
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
