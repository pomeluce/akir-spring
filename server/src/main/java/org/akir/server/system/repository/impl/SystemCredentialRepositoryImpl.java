package org.akir.server.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import jakarta.persistence.EntityManager;
import org.akir.common.core.repository.BaseRepositoryImpl;
import org.akir.server.system.domain.entity.Credential;
import org.akir.server.system.repository.SystemCredentialRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-15 20:59
 * @className : SystemCredentialRepositoryImpl
 * @description : Credential 数据持久层实现
 */
@Repository
@Transactional
public class SystemCredentialRepositoryImpl extends BaseRepositoryImpl<Credential, Long> implements SystemCredentialRepository {

    public SystemCredentialRepositoryImpl(EntityManager entityManager, CriteriaBuilderFactory factory) {
        super(Credential.class, entityManager, factory);
    }

    /**
     * 根据凭据 Id 查找用户凭据
     *
     * @param credentialId 凭据 Id
     * @return 返回符合条件的用户凭据信息
     */
    @Override
    public @Transactional(readOnly = true) Optional<Credential> findByCredentialId(String credentialId) {
        return Optional.ofNullable(factory.create(em, Credential.class, "cred").where("cred.credentialId").eq(credentialId).getSingleResult());
    }
}
