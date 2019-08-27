package com.xdchen.dubbo.multi.env.rpc.filter;

import com.xdchen.dubbo.multi.env.constant.ApplicationVersionConstants;
import com.xdchen.dubbo.multi.env.support.ApplicationVersionUtils;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;

/**
 * 用于实现多环境下，传递想要访问的应用版本
 *
 * @author chenxudong
 * @date 2019/08/25
 */
@Activate(group = {"consumer"})
public class ApplicationVersionConsumerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String expectedVersion = ApplicationVersionUtils.getExpectedVersion();
        expectedVersion = StringUtils.isEmpty(expectedVersion) ? ApplicationVersionUtils.getSelfVersion() : expectedVersion;
        RpcContext.getContext().setAttachment(ApplicationVersionConstants.EXPECTED_VERSION_ATTACHMENT, expectedVersion);
        return invoker.invoke(invocation);
    }
}
