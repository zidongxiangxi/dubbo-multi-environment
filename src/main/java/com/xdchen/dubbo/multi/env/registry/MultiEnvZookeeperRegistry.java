package com.xdchen.dubbo.multi.env.registry;

import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
import com.xdchen.dubbo.multi.env.support.ApplicationVersionUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用于实现多环境的服务注册
 *
 * @author chenxudong
 * @date 2019/08/26
 */
public class MultiEnvZookeeperRegistry implements Registry {
    private Registry registry;

    public MultiEnvZookeeperRegistry(Registry registry) {
        this.registry = registry;
    }

    @Override
    public URL getUrl() {
        getUrlWithApplicationVersion(registry.getUrl());
        return registry.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return registry.isAvailable();
    }

    @Override
    public void destroy() {
        registry.destroy();
    }

    @Override
    public void register(URL url) {
        registry.register(getUrlWithApplicationVersion(url));
    }

    @Override
    public void unregister(URL url) {
        registry.unregister(getUrlWithApplicationVersion(url));
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        registry.subscribe(url, listener);
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        registry.unsubscribe(url, listener);
    }

    @Override
    public List<URL> lookup(URL url) {
        return registry.lookup(url);
    }

    private URL getUrlWithApplicationVersion(URL url) {
        String expectVersion = url.getParameter(ApplicationVersionConstants.VERSION_PARAM_NAME);
        if (StringUtils.isEmpty(expectVersion)) {
            String version = ApplicationVersionUtils.getSelfVersion();
            return url.addParameter(ApplicationVersionConstants.VERSION_PARAM_NAME, version);
        }
        return url;
    }
}
