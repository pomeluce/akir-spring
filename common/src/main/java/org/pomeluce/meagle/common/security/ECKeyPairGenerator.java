package org.pomeluce.meagle.common.security;

import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2025/1/24 22:44
 * @className : ECKeyPairGenerator
 * @description : ESC256 密钥对生成
 */
public class ECKeyPairGenerator {
    private KeyPair keyPair;

    public ECKeyPairGenerator() {
    }

    ECKeyPairGenerator setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
        return this;
    }

    public PublicKey getPublicKey() {
        return this.keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }

    public static ECKeyPairGenerator build() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException {
        // 创建 KeyPairGenerator 实例，指定椭圆曲线算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");

        // 初始化 KeyPairGenerator，指定使用的曲线
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");  // ES256 使用的曲线
        keyPairGenerator.initialize(ecSpec);

        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new ECKeyPairGenerator().setKeyPair(keyPair);
    }
}
