package com.xdchen.dubbo.multi.env.listener;

import com.xdchen.dubbo.multi.env.ApplicationEnv;
import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * spring环境事件监听器
 *
 * @author chenxudong
 * @date 2019/08/25
 */
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        ConfigurableEnvironment environment = applicationEnvironmentPreparedEvent.getEnvironment();
        ApplicationEnv.APPLICATION_VERSION = environment.getProperty(ApplicationVersionConstants.APPLICATION_VERSION_PROPERTY);
    }
}
