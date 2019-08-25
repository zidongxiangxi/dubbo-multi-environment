package com.xdchen.dubbo.multi.env.support;

import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
import org.springframework.util.StringUtils;

/**
 * 用于实现多环境下，线程期望的应用版本
 *
 * @author chenxudong
 * @date 2019/08/25
 */
public class ApplicationVersionUtils {
    private static final ThreadLocal<String> EXPECTED_VERSION = new ThreadLocal<>();

    private ApplicationVersionUtils() {}

    public static String getExpectedVersion() {
        return EXPECTED_VERSION.get();
    }

    public static String getSelfVersion() {
        String version = ApplicationVersionConstants.CURRENT_VERSION;
        version = StringUtils.isEmpty(version) ? ApplicationVersionConstants.BASE_VERSION : version;
        return version;
    }

    public static void setExpectedVersion(String expectedVersion) {
        EXPECTED_VERSION.set(expectedVersion);
    }

    public static void removeExpectedVersion() {
        EXPECTED_VERSION.remove();
    }
}
