package org.rify.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.rify.common.core.domain.entity.RifyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/27下午11:28
 * @className : LoginUser
 * @description : 登录用户身份权限
 */
@JsonIgnoreProperties({"username", "password", "accountNonExpired", "credentialsNonExpired", "accountNonLocked", "enabled"})
public class LoginUser implements UserDetails {

    // 用户唯一标识
    private String uid;
    // 登录 ip
    private String ip;
    // 登录地点
    private String location;
    // 登录浏览器
    private String browser;
    // 登录操作系统
    private String os;
    // 用户信息
    private RifyUser user;
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
        return user.getStatus();
    }

    public LoginUser() {
    }

    public LoginUser(RifyUser user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(RifyUser user) {
        this.user = user;
    }

    public RifyUser getUser() {
        return user;
    }

    public void setUser(RifyUser user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginUser loginUser)) return false;

        if (!Objects.equals(uid, loginUser.uid)) return false;
        if (!Objects.equals(ip, loginUser.ip)) return false;
        if (!Objects.equals(location, loginUser.location)) return false;
        if (!Objects.equals(browser, loginUser.browser)) return false;
        if (!Objects.equals(os, loginUser.os)) return false;
        if (!Objects.equals(user, loginUser.user)) return false;
        if (!Objects.equals(expireTime, loginUser.expireTime)) return false;
        if (!Objects.equals(refreshTime, loginUser.refreshTime))
            return false;
        return Objects.equals(permissions, loginUser.permissions);
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (browser != null ? browser.hashCode() : 0);
        result = 31 * result + (os != null ? os.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (expireTime != null ? expireTime.hashCode() : 0);
        result = 31 * result + (refreshTime != null ? refreshTime.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "uid='" + uid + '\'' +
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
