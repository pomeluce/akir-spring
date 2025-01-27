package org.pomeluce.akir.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/14 21:27
 * @className : BaseEntity
 * @description : entity 基类
 */
@SuperBuilder
@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    private final static @Serial long serialVersionUID = 1L;
    /* 创建人: 默认管理员 */
    private @ColumnDefault("admin") String createBy;
    /* 创建时间 */
    private @CreationTimestamp Timestamp createTime;
    /* 更新人: 默认管理员 */
    private @ColumnDefault("admin") String updateBy;
    /* 更新时间 */
    private @UpdateTimestamp Timestamp updateTime;
    /* 备注 */
    private String remark;
    /* 请求参数, 空值不参与序列化 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private @Transient Map<String, Object> params;

    protected BaseEntity() {
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", params=" + params +
                '}';
    }
}
