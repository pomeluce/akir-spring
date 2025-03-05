package org.pomeluce.akir.common.security;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.Objects;

/**
 * @author : lucas
 * @version : 1.0
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

    /**
     * 生成 ES256 曲线的密钥对
     *
     * @return 返回一个 ES256 密钥对的 ECKeyPairGenerator {@link ECKeyPairGenerator} 对象
     * @throws NoSuchAlgorithmException           if {@code stdName} is null
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key pair generator
     */
    public static ECKeyPairGenerator build() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return build("secp256r1");
    }

    /**
     * 生成指定 {@code stdName} 曲线的密钥对
     *
     * @param stdName 生成曲线 {@link String}
     * @return 返回一个 {@code stdName} 密钥对的 ECKeyPairGenerator {@link ECKeyPairGenerator} 对象
     * @throws NoSuchAlgorithmException           if {@code stdName} is null
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key pair generator
     */
    public static ECKeyPairGenerator build(String stdName) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // 创建 KeyPairGenerator 实例，指定椭圆曲线算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");

        // 初始化 KeyPairGenerator，指定使用的曲线
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");  // ES256 使用的曲线
        keyPairGenerator.initialize(ecSpec);

        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new ECKeyPairGenerator().setKeyPair(keyPair);
    }

    /**
     * 生成 pem 文件
     *
     * @param prefix 名称前缀 {@link String}
     * @throws IOException IO 异常
     */
    public void write(String prefix) throws IOException {
        this.write(prefix, Objects.requireNonNull(this.getClass().getResource("/")).getPath());
    }

    /**
     * 生成 pem 文件
     *
     * @param prefix 名称前缀 {@link String}
     * @param path   文件路径 {@link String}
     * @throws IOException IO 异常
     */
    public void write(String prefix, String path) throws IOException {
        path = path.endsWith("/") ? path : path + "/";

        FileOutputStream fos = new FileOutputStream(path + prefix + "PrivateKey.pem");
        fos.write("-----BEGIN EC PRIVATE KEY-----\n".getBytes());
        fos.write(Base64.getEncoder().encode(this.getPrivateKey().getEncoded()));
        fos.write("\n-----END EC PRIVATE KEY-----".getBytes());
        fos.close();

        fos = new FileOutputStream(path + prefix + "PublicKey.pem");
        fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
        fos.write(Base64.getEncoder().encode(this.getPublicKey().getEncoded()));
        fos.write("\n-----END PUBLIC KEY-----".getBytes());
        fos.close();
    }
}
