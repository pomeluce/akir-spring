package org.pomeluce.akir;

import org.pomeluce.akir.common.security.ECKeyPairGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2025/1/24 22:50
 * @className : ECKeyPairGeneratorTest
 * @description : ESC256 密钥对生成测试类
 */
public class ECKeyPairGeneratorTest {
    public static void main(String[] args) {
        try {
            ECKeyPairGenerator eck = ECKeyPairGenerator.build();

            FileOutputStream fos = new FileOutputStream("ECCPrivateKey.pem");
            fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(eck.getPrivateKey().getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----\n".getBytes());
            fos.close();

            fos = new FileOutputStream("ECCPublicKey.pem");
            fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(eck.getPublicKey().getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----\n".getBytes());
            fos.close();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
