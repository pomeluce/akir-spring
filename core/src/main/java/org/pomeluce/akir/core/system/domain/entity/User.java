package org.pomeluce.akir.core.system.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.pomeluce.akir.common.core.domain.BaseEntity;
import org.pomeluce.akir.core.system.domain.enums.UserStatus;

import java.io.Serial;
import java.util.Objects;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/2 11:23
 * @className : User
 * @description : 用户实体类
 */
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "akir_user")
public class User extends BaseEntity {
    private static final @Serial long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
    private @Column(unique = true) String account;
    private String password;
    private String email;
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private @Enumerated(EnumType.STRING) UserStatus status;
    private String role;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(account, user.account) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && status == user.status && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, password, email, status, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", role='" + role + '\'' +
                "} " + super.toString();
    }
}
