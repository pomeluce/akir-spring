package org.akir.core.web.service;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.akir.common.config.AkirProperty;
import org.akir.common.constants.JwtKeyConstants;
import org.akir.common.constants.RedisKeyConstants;
import org.akir.common.core.redis.RedisClient;
import org.akir.common.utils.StringUtils;
import org.akir.common.utils.id.IdGenerator;
import org.akir.common.utils.location.IpAddrUtils;
import org.akir.common.utils.location.LocationUtils;
import org.akir.common.utils.security.ECKeyPair;
import org.akir.common.utils.spring.ServletClient;
import org.akir.server.system.domain.model.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/28 17:22
 * @className : AkirTokenService
 * @description : Token 服务处理
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AkirTokenService {
    private final AkirProperty property;
    private final RedisClient redisClient;
    private AkirProperty.Token tokenProp;
    private final long MILLIS = 1000L;

    private @PostConstruct void init() {
        tokenProp = property.getToken();
    }

    /**
     * 创建 token 令牌
     *
     * @param account    用户账号 {@link String}
     * @param createTime 创建时间戳 {@link Long}
     * @param claims     自定义属性 {@link Map}
     * @return 返回一个 string 类型的 token 令牌
     */
    public String createToken(String account, Long createTime, Map<String, Object> claims) {
        return Jwts.builder()
                // 设置唯一编号
                .id(IdGenerator.timestamp().toString())
                // 设置主题
                .subject(account)
                // 签发日期
                .issuedAt(new Date(createTime))
                // 设置签发者
                .issuer(tokenProp.getIssuer())
                // 设置过期时间
                .expiration(new Date(createTime + tokenProp.getExpireTime() * MILLIS))
                // 自定义属性
                .claims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(getPrivateKey(), Jwts.SIG.ES256)
                .compact();
    }

    /**
     * 创建 token 令牌
     *
     * @param user 用户信息 {@link LoginUser}
     * @return 返回一个 string 类型的 token 令牌
     */
    public String accessToken(LoginUser user) {
        long currentTime = System.currentTimeMillis();
        String uid = IdGenerator.randomUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtKeyConstants.TOKEN_UID_CLAIM, uid);
        String token = createToken(user.getUsername(), currentTime, claims);

        user.setUid(uid);
        user.setToken(token);
        user.setExpireTime(currentTime + tokenProp.getExpireTime() * MILLIS);
        user.setRefreshTime(currentTime + tokenProp.getRefreshExpireTime() * MILLIS);
        setUserAgent(user);

        redisClient.hset(RedisKeyConstants.TOKEN_ACCESS_KEY, user.getUsername(), user);
        return token;
    }

    /**
     * 刷新 token 令牌
     *
     * @param user 用户信息 {@link LoginUser}
     * @return 返回一个 {@link String} 类型的 token 令牌
     */
    public String refreshToken(LoginUser user) {
        long currentTime = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtKeyConstants.TOKEN_UID_CLAIM, user.getUid());
        String token = createToken(user.getUsername(), currentTime, claims);

        user.setToken(token);
        user.setExpireTime(currentTime + tokenProp.getExpireTime() * MILLIS);
        user.setRefreshTime(currentTime + tokenProp.getRefreshExpireTime() * MILLIS);

        redisClient.hset(RedisKeyConstants.TOKEN_ACCESS_KEY, user.getUsername(), user);
        return token;
    }

    /**
     * 根据 request 对象获取 token
     *
     * @param request HttpServletRequest 对象 {@link HttpServletRequest}
     * @return 返回一个 {@link String} 类型的 token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenProp.getAccessHeader());
        return StringUtils.isNotBlank(token) && StringUtils.startsWithIgnoreCase(token, JwtKeyConstants.TOKEN_PREFIX)
                ? token.substring(JwtKeyConstants.TOKEN_PREFIX.length())
                : token;
    }

    /**
     * 根据 username 获取过度 token
     *
     * @param username 用户登录名 {@link String}
     * @return 返回一个 {@link Optional<String>} 类型的过度 token
     */
    public Optional<String> getTransitionToken(String username) {
        Object token = redisClient.get(RedisKeyConstants.TOKEN_REFRESH_TRANSITION + username);
        return Optional.ofNullable(token).map(Object::toString);
    }

    /**
     * 设置过渡 token
     *
     * @param username 用户登录名 {@link String}
     * @param token    token 信息 {@link String}
     */
    public void setTransitionToken(String username, String token) {
        redisClient.set(RedisKeyConstants.TOKEN_REFRESH_TRANSITION + username, token, tokenProp.getTransitionTime() * MILLIS, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据 username 获取登录信息
     *
     * @param username 登录名 {@link String}
     * @return 返回一个 {@link Optional<LoginUser>} 类型的用户登录对象
     */
    public Optional<LoginUser> getLoginUser(String username) {
        return Optional.ofNullable((LoginUser) redisClient.hget(RedisKeyConstants.TOKEN_ACCESS_KEY, username));
    }

    /**
     * 根据 username 删除登录信息
     *
     * @param username 登录名 {@link String}
     */
    public void delLoginUser(String username) {
        Optional.ofNullable(username).ifPresent(u -> redisClient.hdel(RedisKeyConstants.TOKEN_ACCESS_KEY, u));
    }

    /**
     * 根据 token 获取用户认证信息
     *
     * @param token token 信息 {@link String}
     * @return 反正一个 {@link Authentication} 类型的用户认证信息
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String username = claims.getSubject();
        return this.getLoginUser(username).map(user -> new UsernamePasswordAuthenticationToken(username, token, user.getAuthorities())).orElse(null);
    }

    /**
     * 根据 username 和 token 获取用户认证信息
     *
     * @param username 用户名 {@link String}
     * @param token    token 信息 {@link String}
     * @return 反正一个 {@link Authentication} 类型的用户认证信息
     */
    public Authentication getTransitionAuthentication(String username, String token) {
        return this.getLoginUser(username).map(user -> new UsernamePasswordAuthenticationToken(username, token, user.getAuthorities())).orElse(null);
    }

    /**
     * 根据 token 获取令牌 payload 信息
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 {@link Claims} 类型的令牌载荷信息
     */
    public Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * 判断 token 是否合法
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 {@link Boolean} 类型的判断结果
     */
    public boolean checkToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token);
        } catch (IllegalArgumentException e) {
            log.error("token information parsing failed, failed info: [{}]", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 设置用户代理信息
     *
     * @param user 登录用户信息 {@link LoginUser}
     */
    public void setUserAgent(LoginUser user) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletClient.getRequest().getHeader("User-Agent"));
        String ip = IpAddrUtils.getIpAddress(ServletClient.getRequest());
        user.setIp(ip);
        user.setLocation(LocationUtils.getRelativeLocation(ip));
        user.setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getBrowserVersion());
        user.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 获取 ES256 私钥
     *
     * @return 返回一个 {@link PrivateKey} 类型的私钥
     */
    public PrivateKey getPrivateKey() {
        InputStream is = this.getClass().getResourceAsStream("/" + tokenProp.getPrivateKey());
        return ECKeyPair.readPrivateKey(is);
    }

    /**
     * 获取 ES256 公钥
     *
     * @return 返回一个 {@link PublicKey} 类型的公钥
     */
    public PublicKey getPublicKey() {
        InputStream is = this.getClass().getResourceAsStream("/" + tokenProp.getPublicKey());
        return ECKeyPair.readPublicKey(is);
    }
}
