package org.akir.server.system.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.akir.common.core.domain.BaseEntity;

import java.io.Serial;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-15 17:44
 * @className : Credential
 * @description : 用户登录凭证信息实体类
 */

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "akir_credential")
public class Credential extends BaseEntity {
    private static final @Serial long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
    /* base64 编码的 credential id */
    private @Column(nullable = false, unique = true) String credentialId;
    /* 原始的 cose 公钥 */
    @Lob
    private @Column(nullable = false) byte[] publicKeyCose;
    private @Column(nullable = false) long signatureCount;
    @ManyToOne(fetch = FetchType.LAZY)
    private @JoinColumn(name = "user_id", nullable = false) User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Credential that = (Credential) o;
        return signatureCount == that.signatureCount && Objects.equals(id, that.id) && Objects.equals(credentialId, that.credentialId) && Arrays.equals(publicKeyCose, that.publicKeyCose) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(credentialId);
        result = 31 * result + Arrays.hashCode(publicKeyCose);
        result = 31 * result + Long.hashCode(signatureCount);
        result = 31 * result + Objects.hashCode(user);
        return result;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", credentialId='" + credentialId + '\'' +
                ", publicKeyCose=" + Arrays.toString(publicKeyCose) +
                ", signatureCount=" + signatureCount +
                ", user=" + user +
                "} " + super.toString();
    }
}
