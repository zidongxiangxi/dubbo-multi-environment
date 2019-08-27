package com.xdchen.dubbo.multi.env.rpc.loadbalance;

import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
import com.xdchen.dubbo.multi.env.support.ApplicationVersionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用于实现多环境的负载均衡
 *
 * @author chenxudong
 * @date 2019/08/25
 */
@Slf4j
public class MultiEnvLoadBalance extends RandomLoadBalance {
    public static final String NAME = "multiEnvRandom";

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String baseVersion = ApplicationVersionConstants.BASE_VERSION;
        String expectedVersion = ApplicationVersionUtils.getExpectedVersion();
        expectedVersion = StringUtils.isEmpty(expectedVersion) ? ApplicationVersionUtils.getSelfVersion() : expectedVersion;
        Map<String, List<Invoker<T>>> invokerGroup = invokers.stream().collect(Collectors.groupingBy(invoker -> {
            URL invokerUrl = invoker.getUrl();
            if (Objects.isNull(invokerUrl)) {
                return baseVersion;
            }
            return invokerUrl.getParameter(ApplicationVersionConstants.VERSION_PARAM_NAME, baseVersion);
        }));
        if (invokerGroup.containsKey(expectedVersion)) {
            return super.doSelect(invokerGroup.get(expectedVersion), url, invocation);
        } else if (invokerGroup.containsKey(baseVersion)) {
            return super.doSelect(invokerGroup.get(baseVersion), url, invocation);
        }
        log.warn("can not find expected version : [{}] and base version : [{}]", expectedVersion, baseVersion);
        return super.doSelect(invokers, url, invocation);
    }
}
