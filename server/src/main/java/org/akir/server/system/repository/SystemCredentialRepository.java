package org.akir.server.system.repository;

import org.akir.common.core.repository.BaseRepository;
import org.akir.server.system.domain.entity.Credential;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-15 20:57
 * @className : SystemCredentialRepository
 * @description : Credential 数据持久层
 */
@NoRepositoryBean
public interface SystemCredentialRepository extends BaseRepository<Credential, Long> {

    /**
     * 根据凭据 Id 查找用户凭据
     *
     * @param credentialId 凭据 Id
     * @return 返回符合条件的用户凭据信息
     */
    Optional<Credential> findByCredentialId(String credentialId);
}
