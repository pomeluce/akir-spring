package org.pomeluce.akir.core.system.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.pomeluce.akir.common.core.domain.BaseEntity;
import org.pomeluce.akir.core.system.domain.enums.UserGender;
import org.pomeluce.akir.core.system.domain.enums.UserStatus;

import java.io.Serial;
import java.util.Objects;

/**
 * @author : lucas
 * @version : 1.0
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
    private String identityId;
    private String email;
    private String phone;
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private @Enumerated(EnumType.STRING) UserGender gender;
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private @Enumerated(EnumType.STRING) UserStatus status;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(account, user.account) && Objects.equals(password, user.password) && Objects.equals(identityId, user.identityId) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && gender == user.gender && status == user.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, password, identityId, email, phone, gender, status);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", identityId='" + identityId + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                "} " + super.toString();
    }
}
