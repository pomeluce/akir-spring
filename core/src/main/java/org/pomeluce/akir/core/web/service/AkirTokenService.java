package org.pomeluce.akir.core.web.service;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.pomeluce.akir.common.config.AkirProperty;
import org.pomeluce.akir.common.core.redis.RedisClient;
import org.pomeluce.akir.common.enums.CacheKey;
import org.pomeluce.akir.common.utils.StringUtils;
import org.pomeluce.akir.common.utils.id.IdGenerator;
import org.pomeluce.akir.common.utils.location.IpAddrUtils;
import org.pomeluce.akir.common.utils.location.LocationUtils;
import org.pomeluce.akir.common.utils.security.ECKeyPair;
import org.pomeluce.akir.common.utils.spring.ServletClient;
import org.pomeluce.akir.core.system.domain.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/9/28 17:22
 * @className : AkirTokenService
 * @description : Token 服务处理
 */
@Service
public class AkirTokenService {
    private final Logger log = LoggerFactory.getLogger(AkirTokenService.class);
    private @Resource AkirProperty property;
    private @Resource RedisClient redisClient;
    private AkirProperty.Token tokenProp;
    private final long MILLIS = 1000L;

    private @PostConstruct void init() {
        tokenProp = property.getToken();
    }

    /**
     * 创建 token 令牌
     *
     * @param claims 自定义属性 {@link Map}
     * @return 返回一个 string 类型的 token 令牌
     */
    public String createToken(Map<String, Object> claims) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                // 设置唯一编号
                .id(IdGenerator.timestamp().toString())
                // 设置主题
                .subject("akir-create-token")
                // 签发日期
                .issuedAt(new Date(currentTimeMillis))
                // 设置签发者
                .issuer(tokenProp.getIssuer())
                // 设置过期时间
                .expiration(new Date(currentTimeMillis + tokenProp.getExpireTime() * MILLIS))
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
        user.setUid(uid);
        setUserAgent(user);
        user.setExpireTime(currentTime + tokenProp.getExpireTime() * MILLIS);
        user.setRefreshTime(currentTime + tokenProp.getRefreshExpireTime() * MILLIS);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CacheKey.TOKEN_ACCESS_KEY.value(), uid);
        String token = createToken(claims);
        redisClient.hset(CacheKey.TOKEN_LOGIN_USER_KEY.value(), token, user);
        setExpiredTime(token, currentTime + tokenProp.getExpireTime() * MILLIS);
        setRefreshTime(token, currentTime + tokenProp.getRefreshExpireTime() * MILLIS);
        return CacheKey.TOKEN_PREFIX_KEY.value() + token;
    }

    /**
     * 刷新 token 令牌
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 string 类型的 token 令牌
     */
    public String refreshToken(String token) {
        long currentTime = System.currentTimeMillis();
        LoginUser user = getLoginUser(token);
        user.setExpireTime(currentTime + tokenProp.getExpireTime() * MILLIS);
        user.setRefreshTime(currentTime + tokenProp.getRefreshExpireTime() * MILLIS);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CacheKey.TOKEN_ACCESS_KEY.value(), user.getUid());
        token = createToken(claims);
        redisClient.hset(CacheKey.TOKEN_LOGIN_USER_KEY.value(), token, user);
        setExpiredTime(token, currentTime + tokenProp.getExpireTime() * MILLIS);
        setRefreshTime(token, currentTime + tokenProp.getRefreshExpireTime() * MILLIS);
        return CacheKey.TOKEN_PREFIX_KEY.value() + token;
    }

    /**
     * 根据 request 对象获取 token
     *
     * @param request HttpServletRequest 对象 {@link HttpServletRequest}
     * @return 返回一个 String 类型的 token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenProp.getHeaderKey());
        return StringUtils.isNotBlank(token) && StringUtils.startsWithIgnoreCase(token, CacheKey.TOKEN_PREFIX_KEY.value())
                ? token.substring(CacheKey.TOKEN_PREFIX_KEY.value().length())
                : token;
    }

    /**
     * 根据 token 获取 loginUser
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 LoginUser 类型的用户登录对象
     */
    public LoginUser getLoginUser(String token) {
        Object user = redisClient.hget(CacheKey.TOKEN_LOGIN_USER_KEY.value(), token);
        return user != null ? (LoginUser) user : null;
    }

    /**
     * 判断 token 是否合法
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public boolean checkToken(String token) {
        try {
            Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token);
        } catch (JwtException e) {
            log.error("token information is expired: [{}]", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("token information parsing failed, failed info: [{}]", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 判断 token 是否在黑名单中
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public boolean isBlackList(String token) {
        return redisClient.hexists(CacheKey.TOKEN_BLACK_KEY.value(), token);
    }

    /**
     * 判断 token 是否在黑名单外
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public boolean isNotBlackList(String token) {
        return !isBlackList(token);
    }

    /**
     * 添加 token 到黑名单中
     *
     * @param token token 信息 {@link String}
     */
    public void putBlackList(String token) {
        redisClient.hset(CacheKey.TOKEN_BLACK_KEY.value(), token, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 判断 token 是否过期并且加入黑名单中
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public boolean isExpiredAndBlackList(String token) {
        boolean expired = isExpired(token);
        if (expired) putBlackList(token);
        return expired;
    }

    /**
     * 设置 token 的过期时间
     *
     * @param token       token 信息 {@link String}
     * @param expiredTime 过期时间 {@link  Long}
     */
    public void setExpiredTime(String token, Long expiredTime) {
        redisClient.hset(CacheKey.TOKEN_EXPIRED_TIME_KEY.value(), token, expiredTime);
    }

    /**
     * 根据 token 获取过期时间
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 Long 类型的过期时间
     */
    public long getExpiredTime(String token) {
        Object expiredTime = redisClient.hget(CacheKey.TOKEN_EXPIRED_TIME_KEY.value(), token);
        return expiredTime == null ? 0L : (Long) expiredTime;
    }

    /**
     * 设置 token 的刷新时间
     *
     * @param token       token 信息 {@link String}
     * @param refreshTime 刷新时间 {@link  Long}
     */
    public void setRefreshTime(String token, Long refreshTime) {
        redisClient.hset(CacheKey.TOKEN_REFRESH_TIME_KEY.value(), token, refreshTime);
    }

    /**
     * 根据 token 获取刷新时间
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 Long 类型的刷新时间
     */
    public long getRefreshTime(String token) {
        Object refreshTime = redisClient.hget(CacheKey.TOKEN_REFRESH_TIME_KEY.value(), token);
        return refreshTime == null ? 0L : (Long) refreshTime;
    }

    /**
     * 判断 token 是否过期
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public boolean isExpired(String token) {
        return System.currentTimeMillis() > getExpiredTime(token);
    }

    /**
     * 判断 token 是否可以刷新
     *
     * @param token token 信息 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public boolean isRefresh(String token) {
        return System.currentTimeMillis() <= getRefreshTime(token);
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
     * @return 返回一个 PrivateKey{@link PrivateKey} 类型的私钥
     */
    public PrivateKey getPrivateKey() {
        InputStream is = this.getClass().getResourceAsStream("/" + tokenProp.getPrivateKey());
        return ECKeyPair.readPrivateKey(is);
    }

    /**
     * 获取 ES256 公钥
     *
     * @return 返回一个 PublicKey{@link PublicKey} 类型的公钥
     */
    public PublicKey getPublicKey() {
        InputStream is = this.getClass().getResourceAsStream("/" + tokenProp.getPublicKey());
        return ECKeyPair.readPublicKey(is);
    }
}
