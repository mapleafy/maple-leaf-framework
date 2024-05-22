package cn.maple.core.framework.wrapper.mdc;

import cn.maple.core.framework.util.GXMdcThreadUtils;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * GXMdcWrapperThreadPoolTaskExecutor
 * 线程池任务包装类
 *
 * <p>
 * 解决场景:
 * 在生产环境种，查询发现错误日志时，需要定位跟踪到最终问题，但是项目中用到了很多的异步线程池，导致定位问题时根据traceId，只能找到一部分日志内容，无法最终定位到问题所在；
 *
 * <p>
 * 原因:
 * 这是由于MDC的实现是通过ThreadLocal实现的，然后线程池种的线程和用户线程不是同一个线程，所以在异步调用的时候，
 * 用户线程的内容没有同步到线程池线程中，导致最终没有打印出关联的tracId，从而导致最终无法关联相关日志，无法定位到具体问题；
 *
 * <p>
 * 使用方法:
 * 在需要使用线程池的时候, 使用GXMdcWrapperThreadPoolTaskExecutor类创建线程池去执行相关逻辑
 *
 * @author gapleaf@163.com
 */
@SuppressWarnings("all")
public class GXMdcWrapperThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    @Override
    public void execute(Runnable task) {
        super.execute(GXMdcThreadUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        super.execute(GXMdcThreadUtils.wrap(task, MDC.getCopyOfContextMap()), startTimeout);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(GXMdcThreadUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(GXMdcThreadUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return super.submitListenable(GXMdcThreadUtils.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(GXMdcThreadUtils.wrap(task, MDC.getCopyOfContextMap()));
    }
}
