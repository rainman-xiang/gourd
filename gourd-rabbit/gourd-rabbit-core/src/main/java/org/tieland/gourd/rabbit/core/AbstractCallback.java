package org.tieland.gourd.rabbit.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.tieland.gourd.common.util.SystemUtils;
import org.tieland.gourd.core.support.RetryHandler;
import org.tieland.gourd.stack.api.GourdStack;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:49
 */
@Slf4j
public abstract class AbstractCallback {

    private final Integer DEFAULT_SLEEP_MILLS = 500;

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
            new BasicThreadFactory.Builder().namingPattern("RabbitAsyncCallback-Scheduled-Pool-%d").daemon(true).build());

    /**
     * 重试Handler
     */
    private RetryHandler retryHandler;

    /**
     * Gourd存储器
     */
    private GourdStack stack;

    public void setRetryHandler(RetryHandler retryHandler) {
        this.retryHandler = retryHandler;
    }

    public void setStack(GourdStack stack) {
        this.stack = stack;
    }

    protected void doSuccess(final String id){
        stack.succeed(id);
    }

    protected void doFail(final String id){
        stack.failed(id);
        executorService.execute(new AsyncCallback(id));
    }

    class AsyncCallback implements Runnable{

        private  String id;

        AsyncCallback(final String id){
            this.id = id;
        }

        @Override
        public void run() {
            SystemUtils.sleep(DEFAULT_SLEEP_MILLS);
            retryHandler.handle(stack.get(id));
        }
    }
}
