package org.pomeluce.akir.core.system.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2025/2/23 16:24
 * @className : Role
 * @description : 角色实体类
 */
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "akir_role")
public class Role {
    private @Id Long id;
    private @Column(unique = true) String key;
    private String description;

    public Role() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(key, role.key) && Objects.equals(description, role.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, description);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
