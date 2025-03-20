package org.pomeluce.akir.common.utils.security;

import org.pomeluce.akir.common.exception.AkirCommonUtilException;
import org.pomeluce.akir.common.utils.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025/1/24 22:44
 * @className : ECKeyPair
 * @description : ECC 密钥对生成
 */
public class ECKeyPair {
    private KeyPair keyPair;

    public ECKeyPair() {
    }

    ECKeyPair setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
        return this;
    }

    public PublicKey getPublicKey() {
        return this.keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }

    enum Spec {
        ES256("sepc256r1"),
        ES384("secp384r1"),
        ES512("secp521r1");

        private final String stdName;

        Spec(String stdName) {
            this.stdName = stdName;
        }

        public String value() {
            return this.stdName;
        }
    }

    /**
     * 生成 ES256 曲线的密钥对
     *
     * @return 返回一个 ES256 密钥对的 ECKeyPairGenerator {@link ECKeyPair} 对象
     * @throws NoSuchAlgorithmException           if {@code stdName} is null
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key pair generator
     */
    public static ECKeyPair ES256() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return build(Spec.ES256);
    }


    /**
     * 生成 ES384 曲线的密钥对
     *
     * @return 返回一个 ES384 密钥对的 ECKeyPairGenerator {@link ECKeyPair} 对象
     * @throws NoSuchAlgorithmException           if {@code stdName} is null
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key pair generator
     */
    public static ECKeyPair ES384() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return build(Spec.ES384);
    }

    /**
     * 生成 ES512 曲线的密钥对
     *
     * @return 返回一个 ES512 密钥对的 ECKeyPairGenerator {@link ECKeyPair} 对象
     * @throws NoSuchAlgorithmException           if {@code stdName} is null
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key pair generator
     */
    public static ECKeyPair ES512() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return build(Spec.ES512);
    }

    /**
     * 生成指定 {@code stdName} 曲线的密钥对
     *
     * @param stdName 生成曲线 {@link Spec}
     * @return 返回一个 {@code stdName} 密钥对的 ECKeyPairGenerator {@link ECKeyPair} 对象
     * @throws NoSuchAlgorithmException           if {@code stdName} is null
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key pair generator
     */
    private static ECKeyPair build(Spec stdName) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // 创建 KeyPairGenerator 实例，指定椭圆曲线算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");

        // 初始化 KeyPairGenerator，指定使用的曲线
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(stdName.value());  // ES256 使用的曲线
        keyPairGenerator.initialize(ecSpec);

        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new ECKeyPair().setKeyPair(keyPair);
    }

    /**
     * 读取私钥
     *
     * @param is 输入流 {@link InputStream}
     * @return 返回一个 PrivateKey{@link PrivateKey} 类型的私钥
     */
    public static PrivateKey readPrivateKey(InputStream is) {
        try {
            byte[] encodeKey = Objects.requireNonNull(is).readAllBytes();
            is.close();
            String content = new String(encodeKey)
                    .replace("-----BEGIN EC PRIVATE KEY-----", "")
                    .replace("-----END EC PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decode = Base64.getDecoder().decode(content);
            return KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(decode));
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new AkirCommonUtilException(StringUtils.format("failed to parse private key: {}", e.getMessage()));
        }
    }

    /**
     * 读取公钥
     *
     * @param is 输入流 {@link InputStream}
     * @return 返回一个 PublicKey{@link PublicKey} 类型的公钥
     */
    public static PublicKey readPublicKey(InputStream is) {
        try {
            byte[] encodeKey = Objects.requireNonNull(is).readAllBytes();
            is.close();
            String content = new String(encodeKey)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decode = Base64.getDecoder().decode(content);

            return KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(decode));
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new AkirCommonUtilException(StringUtils.format("failed to parse public key: {}", e.getMessage()));
        }
    }

    /**
     * 生成 pem 文件
     *
     * @param name 名称 {@link String}
     * @throws IOException IO 异常
     */
    public void write(String name) throws IOException {
        this.write(name, "");
    }

    /**
     * 生成 pem 文件
     *
     * @param name 名称 {@link String}
     * @param path 文件路径 {@link String}
     * @throws IOException IO 异常
     */
    public void write(String name, String path) throws IOException {
        path = path.endsWith("/") ? path : path + "/";

        FileOutputStream fos = new FileOutputStream(path + name + "PrivateKey.pem");
        fos.write("-----BEGIN EC PRIVATE KEY-----\n".getBytes());
        fos.write(Base64.getEncoder().encode(this.getPrivateKey().getEncoded()));
        fos.write("\n-----END EC PRIVATE KEY-----".getBytes());
        fos.close();

        fos = new FileOutputStream(path + name + "PublicKey.pem");
        fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
        fos.write(Base64.getEncoder().encode(this.getPublicKey().getEncoded()));
        fos.write("\n-----END PUBLIC KEY-----".getBytes());
        fos.close();
    }
}
