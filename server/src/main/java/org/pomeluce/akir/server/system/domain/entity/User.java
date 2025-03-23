package org.pomeluce.akir.server.system.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.pomeluce.akir.common.core.domain.BaseEntity;
import org.pomeluce.akir.server.system.domain.enums.UserGender;
import org.pomeluce.akir.server.system.domain.enums.UserStatus;

import java.io.Serial;
import java.util.Objects;

/**
 * @author : marcus
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
    private String username;
    private String identityId;
    private String email;
    private String phone;
    private String avatar;
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
        return Objects.equals(id, user.id) && Objects.equals(account, user.account) && Objects.equals(password, user.password) && Objects.equals(username, user.username) && Objects.equals(identityId, user.identityId) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && Objects.equals(avatar, user.avatar) && gender == user.gender && status == user.status;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(account);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(identityId);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(phone);
        result = 31 * result + Objects.hashCode(avatar);
        result = 31 * result + Objects.hashCode(gender);
        result = 31 * result + Objects.hashCode(status);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", identityId='" + identityId + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                "} " + super.toString();
    }
}
