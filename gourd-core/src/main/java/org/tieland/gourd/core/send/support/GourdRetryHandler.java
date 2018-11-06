package org.tieland.gourd.core.send.support;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.support.AbstractRetryHandler;
import org.tieland.gourd.core.support.RetryStrategy;
import org.tieland.gourd.core.send.GourdQueue;
import org.tieland.gourd.core.support.DefaultRetryStrategy;
import org.tieland.gourd.stack.api.GourdStack;
import org.tieland.gourd.stack.api.HandleNode;

/**
 * @author zhouxiang
 * @date 2018/10/26 14:49
 */
@Slf4j
public class GourdRetryHandler extends AbstractRetryHandler<Gourd> {

    /**
     * Gourd队列
     */
    private GourdQueue queue;

    /**
     * Gourd存储器
     */
    private GourdStack stack;

    /**
     * retry策略
     */
    private RetryStrategy retryStrategy;

    public GourdRetryHandler(){
        retryStrategy = new DefaultRetryStrategy();
    }

    public void setQueue(GourdQueue queue) {
        this.queue = queue;
    }

    public void setStack(GourdStack stack) {
        this.stack = stack;
    }

    public void setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    @Override
    protected boolean canRetry(Gourd gourd) {
        HandleNode handleNode = stack.getHandleNode(gourd.getId());
        return retryStrategy.decide(handleNode);
    }

    @Override
    protected void retry(Gourd gourd) {
        log.debug(" gourd:{} begin to push queue ", gourd);
        queue.push(gourd);
        log.debug(" gourd:{} success to push queue ", gourd);
    }
}
