package cn.maple.dubbo.nacos.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXTraceIdContextUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

@Activate(group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER}, order = Ordered.LOWEST_PRECEDENCE)
public class GXDubboTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceIdAttachment = invocation.getAttachment(GXTraceIdContextUtils.TRACE_ID_KEY);
        if (CharSequenceUtil.isEmpty(traceIdAttachment)) {
            traceIdAttachment = GXTraceIdContextUtils.getTraceId();
        }
        GXTraceIdContextUtils.setTraceId(traceIdAttachment);
        RpcContext.getServerAttachment().setAttachment(GXTraceIdContextUtils.TRACE_ID_KEY, traceIdAttachment);
        Result invoke = invoker.invoke(invocation);
        GXTraceIdContextUtils.removeTraceId();
        return invoke;
    }
}
