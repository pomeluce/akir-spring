package org.akir.core.web.config;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import lombok.RequiredArgsConstructor;
import org.akir.common.config.AkirProperty;
import org.akir.core.web.service.AkirAuthnCredentialService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-15 21:57
 * @className : WebAuthnConfig
 * @description : RelyingParty 注入配置
 */
@RequiredArgsConstructor
@Configuration
public class WebAuthnConfig {
    private final AkirProperty property;
    private final AkirAuthnCredentialService service;

    public @Bean RelyingParty relyingParty() {
        AkirProperty.WebAuthn webAuthn = property.getWebAuthn();
        RelyingPartyIdentity identity = RelyingPartyIdentity.builder()
                .id(webAuthn.getRpId())
                .name(webAuthn.getRpName())
                .build();
        return RelyingParty.builder()
                .identity(identity)
                .credentialRepository(service)
                .origins(Optional.of(Collections.singleton(webAuthn.getOrigin())))
                .build();
    }
}
