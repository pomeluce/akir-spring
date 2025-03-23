package org.pomeluce.akir.server.system.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.pomeluce.akir.server.system.domain.entity.User;
import org.pomeluce.akir.server.system.domain.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/27下午11:28
 * @className : LoginUser
 * @description : 登录用户身份权限
 */
@Getter
@Setter
@JsonIgnoreProperties({"username", "password", "accountNonExpired", "credentialsNonExpired", "accountNonLocked", "enabled"})
public class LoginUser implements UserDetails {
    private static final @Serial long serialVersionUID = 1L;

    // 用户唯一标识
    private String uid;
    // 用户授权令牌
    private String token;
    // 登录 ip
    private String ip;
    // 登录地点
    private String location;
    // 登录浏览器
    private String browser;
    // 登录操作系统
    private String os;
    // 用户信息
    private User user;
    // 过期时间
    private Long expireTime;
    // 刷新时间
    private Long refreshTime;
    // 权限列表
    private List<String> permissions;

    public @Override Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public @Override String getPassword() {
        return user.getPassword();
    }

    public @Override String getUsername() {
        return user.getAccount();
    }

    /* 账号是否未过期 */
    public @Override boolean isAccountNonExpired() {
        return true;
    }

    /* 账号是否解锁 */
    public @Override boolean isAccountNonLocked() {
        return true;
    }

    /* 账号凭证是否有效 */
    public @Override boolean isCredentialsNonExpired() {
        return true;
    }

    /* 账号是否可用 */
    public @Override boolean isEnabled() {
        return UserStatus.ENABLED.equals(user.getStatus());
    }

    public LoginUser() {
    }

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(uid, loginUser.uid) && Objects.equals(token, loginUser.token) && Objects.equals(ip, loginUser.ip) && Objects.equals(location, loginUser.location) && Objects.equals(browser, loginUser.browser) && Objects.equals(os, loginUser.os) && Objects.equals(user, loginUser.user) && Objects.equals(expireTime, loginUser.expireTime) && Objects.equals(refreshTime, loginUser.refreshTime) && Objects.equals(permissions, loginUser.permissions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(uid);
        result = 31 * result + Objects.hashCode(token);
        result = 31 * result + Objects.hashCode(ip);
        result = 31 * result + Objects.hashCode(location);
        result = 31 * result + Objects.hashCode(browser);
        result = 31 * result + Objects.hashCode(os);
        result = 31 * result + Objects.hashCode(user);
        result = 31 * result + Objects.hashCode(expireTime);
        result = 31 * result + Objects.hashCode(refreshTime);
        result = 31 * result + Objects.hashCode(permissions);
        return result;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", ip='" + ip + '\'' +
                ", location='" + location + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", user=" + user +
                ", expireTime=" + expireTime +
                ", refreshTime=" + refreshTime +
                ", permissions=" + permissions +
                '}';
    }
}
