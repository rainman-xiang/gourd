package org.tieland.gourd.core.send.support;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.core.send.GourdContext;
import org.tieland.gourd.core.send.GourdContextHolder;
import org.tieland.gourd.core.send.GourdQueue;
import org.tieland.gourd.core.send.GourdRouter;
import org.tieland.gourd.stack.api.GourdStack;

/**
 * @author zhouxiang
 * @date 2018/10/24 15:40
 */
@Slf4j
public abstract class AbstractGourdRouter implements GourdRouter {

    /**
     * Gourd队列
     */
    protected GourdQueue queue;

    /**
     * Gourd存储器
     */
    private GourdStack stack;

    public void setQueue(GourdQueue queue) {
        this.queue = queue;
    }

    public void setStack(GourdStack stack) {
        this.stack = stack;
    }

    @Override
    public void begin(GourdContext context) {
        log.debug(" begin to prepare:{}", context.getGourd());
        stack.prepare(context.getGourd());
        log.debug(" success prepare:{} ", context.getGourd());
    }

    @Override
    public void confirm(GourdContext context) {
        try{
            direct(context);
        }finally {
            GourdContextHolder.unbind();
        }
    }

    @Override
    public void direct(GourdContext context) {
        log.debug(" begin to createNew:{} ", context.getGourd());
        stack.createNew(context.getGourd());
        log.debug(" success createNew:{} ", context.getGourd());

        try{
            queue.push(context.getGourd());
            log.debug(" push to deliverQueue success:{} ", context.getGourd());
        }catch (Throwable throwable){
            log.error(" push to deliverQueue error:{} ", context.getGourd(), throwable);
        }
    }

    @Override
    public void cancel(GourdContext context) {
        try{
            stack.cancelPrepare(context.getGourd());
        }catch (Throwable throwable){
            log.error(" cancelBook error: {}", context.getGourd(), throwable);
        }finally {
            GourdContextHolder.unbind();
        }
    }

}
