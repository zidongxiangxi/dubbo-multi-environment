package com.xdchen.dubbo.multi.env.remote.zookeeper;

import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.zookeeper.ZookeeperClient;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;
import org.springframework.util.StringUtils;

/**
 * 用于实现多环境的包装
 *
 * @author chenxudong
 * @date 2019/08/23
 */
public class MultiEnvZookeeperTransporterWrapper implements ZookeeperTransporter {
    private ZookeeperTransporter zookeeperTransporter;

    public MultiEnvZookeeperTransporterWrapper(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    @Override
    public ZookeeperClient connect(URL url) {
        String expectVersion = url.getParameter(ApplicationVersionConstants.VERSION_PARAM_NAME);
        if (StringUtils.isEmpty(expectVersion)) {
            String version = ApplicationVersionConstants.CURRENT_VERSION;
            version = StringUtils.isEmpty(version) ? ApplicationVersionConstants.BASE_VERSION : version;
            url.addParameter(ApplicationVersionConstants.VERSION_PARAM_NAME,
                version);
        }
        return zookeeperTransporter.connect(url);
    }
}
