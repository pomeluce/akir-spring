package org.pomeluce.akir.common.utils.location;

import jakarta.servlet.http.HttpServletRequest;
import org.pomeluce.akir.common.utils.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/29 23:29
 * @className : IpAddrUtils
 * @description : ip 地址工具类
 */
public class IpAddrUtils {
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    /**
     * 获取 ip 地址: 穿透代理
     *
     * @param request request 对象 {@link HttpServletRequest}
     * @return 返回一个 String 类型的 ip 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = Arrays.stream(HEADERS_TO_TRY)
                .map(request::getHeader)
                .filter(ip -> StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip))
                .findFirst()
                .orElse(request.getRemoteAddr());
        return "0:0:0:0:0:0:0:1".equals(ipAddress) ? "127.0.0.1" : getMultistageReverseProxyIp(ipAddress);
    }

    /**
     * 获取多级反向代理 ip 地址
     *
     * @param ips 待检测的多级反向代理 ip 地址 {@link String}
     * @return 返回一个 String 类型的 ip 地址
     */
    public static String getMultistageReverseProxyIp(String ips) {
        if (StringUtils.isNotEmpty(ips) && ips.contains(",")) {
            return Arrays.stream(ips.trim().split(","))
                    .filter(ip -> StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip))
                    .findFirst()
                    .orElse("unknown");
        }
        return StringUtils.substring(ips, 0);
    }


    /**
     * 判断 ip 地址是否是内网地址
     *
     * @param ipAddress ip 地址 {@link String}
     * @return 返回一个 boolean 类型的判断结果
     */
    public static boolean isInternalIP(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            byte[] addressBytes = inetAddress.getAddress();
            long address = ((long) (addressBytes[0] & 0xFF) << 24) |
                    ((addressBytes[1] & 0xFF) << 16) |
                    ((addressBytes[2] & 0xFF) << 8) |
                    (addressBytes[3] & 0xFF);

            return (address >= 167772160L && address <= 184549375L) || // 10.0.0.0 - 10.255.255.255
                    (address >= 2886729728L && address <= 2887778303L) || // 172.16.0.0 - 172.31.255.255
                    (address >= 3232235520L && address <= 3232301055L); // 192.168.0.0 - 192.168.255.255
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 获取本地主机名
     *
     * @return 返回一个 String 类型的本地主机名
     */
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
