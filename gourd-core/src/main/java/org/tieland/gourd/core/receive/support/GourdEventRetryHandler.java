package org.tieland.gourd.core.receive.support;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.core.support.AbstractRetryHandler;
import org.tieland.gourd.core.support.RetryStrategy;
import org.tieland.gourd.core.receive.GourdEventQueue;
import org.tieland.gourd.core.support.DefaultRetryStrategy;
import org.tieland.gourd.stack.api.GourdEventStack;
import org.tieland.gourd.stack.api.HandleNode;

/**
 * GourdEvent重试Handler
 * @author zhouxiang
 * @date 2018/10/26 15:14
 */
@Slf4j
public class GourdEventRetryHandler extends AbstractRetryHandler<GourdEvent> {

    /**
     * queue
     */
    private GourdEventQueue queue;

    /**
     * 存储stack
     */
    private GourdEventStack stack;

    /**
     * 重试策略
     */
    private RetryStrategy retryStrategy;

    public GourdEventRetryHandler(){
        retryStrategy = new DefaultRetryStrategy();
    }

    public void setQueue(GourdEventQueue queue) {
        this.queue = queue;
    }

    public void setStack(GourdEventStack stack) {
        this.stack = stack;
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    @Override
    protected boolean canRetry(GourdEvent event) {
        HandleNode handleNode = stack.getHandleNode(event.getId());
        return retryStrategy.decide(handleNode);
    }

    @Override
    protected void retry(GourdEvent event) {
        log.debug(" event:{} begin to push Queue ", event);
        queue.push(event);
        log.debug(" event:{} success push Queue ", event);
    }
}
