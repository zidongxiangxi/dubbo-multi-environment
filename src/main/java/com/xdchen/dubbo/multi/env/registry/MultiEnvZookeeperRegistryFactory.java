package com.xdchen.dubbo.multi.env.registry;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistryFactory;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;

/**
 * 用于实现多环境的服务注册
 *
 * @author chenxudong
 * @date 2019/08/26
 */
public class MultiEnvZookeeperRegistryFactory implements RegistryFactory {
    private ZookeeperTransporter zookeeperTransporter;

    /**
     * Invisible injection of zookeeper client via IOC/SPI
     * @param zookeeperTransporter
     */
    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    @Override
    public Registry getRegistry(URL url) {
        ZookeeperRegistryFactory zookeeperRegistryFactory = new ZookeeperRegistryFactory();
        zookeeperRegistryFactory.setZookeeperTransporter(zookeeperTransporter);
        return new MultiEnvZookeeperRegistry(zookeeperRegistryFactory.getRegistry(url));
    }
}
