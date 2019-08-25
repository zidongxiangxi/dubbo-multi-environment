package com.xdchen.dubbo.multi.env.rpc.loadbalance;

import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
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
    public static final String NAME = "multi-env-random";

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String baseVersion = ApplicationVersionConstants.BASE_VERSION;
        String currentVersion = ApplicationVersionConstants.CURRENT_VERSION;
        currentVersion = StringUtils.isEmpty(currentVersion) ? baseVersion : currentVersion;
        Map<String, List<Invoker<T>>> invokerGroup = invokers.stream().collect(Collectors.groupingBy(invoker -> {
            URL invokerUrl = invoker.getUrl();
            if (Objects.isNull(invokerUrl)) {
                return baseVersion;
            }
            return invokerUrl.getParameter(ApplicationVersionConstants.VERSION_PARAM_NAME, baseVersion);
        }));
        if (invokerGroup.containsKey(currentVersion)) {
            return super.doSelect(invokerGroup.get(currentVersion), url, invocation);
        } else if (invokerGroup.containsKey(baseVersion)) {
            return super.doSelect(invokerGroup.get(baseVersion), url, invocation);
        }
        log.warn("can not find appoint version : [{}] and base version : [{}]", currentVersion, baseVersion);
        return super.doSelect(invokers, url, invocation);
    }
}
