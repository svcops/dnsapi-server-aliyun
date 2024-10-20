package io.intellij.devops.server.dnsapi.utils;

import io.vertx.core.http.HttpServerRequest;

/**
 * RequestUtils
 *
 * @author tech@intellij.io
 */
public class RequestUtils {
    private RequestUtils() {
    }

    // 定义 IPv4 的正则表达式
    static final String IPv4_REGEX =
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";


    public static boolean isValidIPv4(String ipAddress) {
        if (ipAddress == null) {
            return false;
        }
        return ipAddress.matches(IPv4_REGEX);
    }

    public static String getRequestIp(HttpServerRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.remoteAddress().host();
        }
        // 对于通过代理的请求，第一个IP地址是客户端的真实IP地址
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }

        return ip;
    }

}
