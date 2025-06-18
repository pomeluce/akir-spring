package org.akir.controller.system;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.akir.common.annotation.RestAPIController;
import org.akir.common.core.controller.BaseController;
import org.akir.core.web.service.AkirAuthnCredentialService;
import org.akir.server.system.domain.entity.User;
import org.akir.server.system.domain.model.RegisterOptionsRequest;
import org.akir.server.system.services.SystemUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-15 22:42
 * @className : SystemRegisterController
 * @description : 用户注册控制器
 */
@RequiredArgsConstructor
@RestAPIController("/auth")
public class SystemRegisterController extends BaseController {
    private final RelyingParty relyingParty;
    private final AkirAuthnCredentialService credentialService;
    private final SystemUserService service;

    public @PostMapping("/register/options") PublicKeyCredentialCreationOptions registerOptions(@RequestBody RegisterOptionsRequest request, HttpSession session) {
        User user = service.findByAccount(request.account());
        if (user == null) {
            byte[] userId = new byte[16];
            new SecureRandom(userId);
            user = service.save(User.builder()
                    .account(request.account())
                    .username(request.username())
                    .userHandle(Base64.getUrlEncoder().withoutPadding().encodeToString(userId))
                    .build());
        }
        return null;
    }
}
