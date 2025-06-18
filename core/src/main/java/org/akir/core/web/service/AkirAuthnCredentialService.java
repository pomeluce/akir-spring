package org.akir.core.web.service;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akir.server.system.domain.entity.Credential;
import org.akir.server.system.domain.entity.User;
import org.akir.server.system.repository.SystemCredentialRepository;
import org.akir.server.system.repository.SystemUserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-15 18:38
 * @className : AkirAuthnCredentialService
 * @description : 凭证存储处理
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AkirAuthnCredentialService implements CredentialRepository {
    private final SystemUserRepository userRepository;
    private final SystemCredentialRepository credentialRepository;

    /**
     * 返回给定用户名的所有已注册凭证描述符, 用于构造 allowCredentials 或 excludeCredentials 列表
     */
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String account) {
        return userRepository.findByAccount(account)
                .map(user -> user.getCredentials().stream()
                        .map(credential -> {
                            ByteArray id = new ByteArray(Base64.getUrlDecoder().decode(credential.getCredentialId()));
                            return PublicKeyCredentialDescriptor.builder().id(id).type(PublicKeyCredentialType.PUBLIC_KEY).build();
                        })
                        .collect(Collectors.toSet())
                ).orElse(Collections.emptySet());
    }

    /**
     * 根据用户名返回用户句柄(ByteArray), 用户句柄在注册时自定义生成并存储为 Base64URL 字符串
     */
    @Override
    public Optional<ByteArray> getUserHandleForUsername(String account) {
        return userRepository.findByAccount(account).map(user -> {
            byte[] decoded = Base64.getUrlDecoder().decode(user.getUserHandle());
            return new ByteArray(decoded);
        });
    }

    /**
     * 根据用户句柄查找用户名
     * 用户句柄为 ByteArray, 需要与数据库中 Base64URL 存储值匹配
     */
    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(userHandle.getBytes());
        return userRepository.findByAuthnUserId(base64).map(User::getAccount);
    }

    /**
     * 查找指定用户(由 userHandle)拥有的特定 credentialId 对应的已注册凭证, 返回 RegisteredCredential
     */
    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        String credId = Base64.getUrlEncoder().withoutPadding().encodeToString(credentialId.getBytes());
        Optional<Credential> cred = credentialRepository.findByCredentialId(credId);
        if (cred.isEmpty()) return Optional.empty();

        Credential credential = cred.get();
        String handle = credential.getUser().getUserHandle();
        ByteArray storeUserHandle = new ByteArray(Base64.getUrlDecoder().decode(handle));

        if (!Arrays.equals(storeUserHandle.getBytes(), userHandle.getBytes())) {
            log.warn("lookup: credentialId {} 对应的 userHandle 与传入不匹配, 返回 empty", handle);
            return Optional.empty();
        }

        return Optional.of(RegisteredCredential.builder()
                .credentialId(new ByteArray(Base64.getUrlDecoder().decode(credential.getCredentialId())))
                .userHandle(storeUserHandle)
                .publicKeyCose(new ByteArray(credential.getPublicKeyCose()))
                .signatureCount(credential.getSignatureCount())
                .build()
        );
    }

    /**
     * 查找所有拥有此 credentialId 的 RegisteredCredential(跨用户), 一般用于检测凭证 ID 唯一性或其他场景
     */
    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        String credId = Base64.getUrlEncoder().withoutPadding().encodeToString(credentialId.getBytes());
        Optional<Credential> cred = credentialRepository.findByCredentialId(credId);
        if (cred.isEmpty()) return Collections.emptySet();

        Credential credential = cred.get();

        return Set.of(RegisteredCredential.builder()
                .credentialId(new ByteArray(Base64.getUrlDecoder().decode(credential.getCredentialId())))
                .userHandle(new ByteArray(Base64.getUrlDecoder().decode(credential.getUser().getUserHandle())))
                .publicKeyCose(new ByteArray(credential.getPublicKeyCose()))
                .signatureCount(credential.getSignatureCount())
                .build()
        );
    }

    /**
     * 在 RelyingParty.finishAssertion 验证通过后, 需要更新签名计数, 防止重放攻击
     * 注意: lookup/lookupAll 返回的 RegisteredCredential 中 signatureCount 是旧值, 调用完 finishAssertion 后传入的新值需更新到数据库
     */
    public void updateSignatureCount(ByteArray credentialId, long newCount) {
        String credId = Base64.getUrlEncoder().withoutPadding().encodeToString(credentialId.getBytes());
        credentialRepository.findByCredentialId(credId).ifPresent(cred -> {
            cred.setSignatureCount(newCount);
            userRepository.save(cred.getUser());
        });
    }

    /**
     * 在注册成功后保存新凭证
     */
    public void addCredential(User user, ByteArray credentialId, ByteArray publicKeyCose, long signatureCount) {
        Credential credential = Credential.builder()
                .user(user)
                .credentialId(Base64.getUrlEncoder().withoutPadding().encodeToString(credentialId.getBytes()))
                .publicKeyCose(publicKeyCose.getBytes())
                .signatureCount(signatureCount)
                .build();
        user.getCredentials().add(credential);
        userRepository.save(user);
    }
}
