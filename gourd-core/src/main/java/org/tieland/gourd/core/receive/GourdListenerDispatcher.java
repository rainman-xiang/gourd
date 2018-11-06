package org.tieland.gourd.core.receive;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.common.util.SystemUtils;
import org.tieland.gourd.core.support.RetryHandler;
import org.tieland.gourd.core.receive.support.GourdEventRetryHandler;
import org.tieland.gourd.stack.api.GourdEventStack;

/**
 * GourdListener分发处理
 * @author zhouxiang
 * @date 2018/10/26 13:39
 */
@Slf4j
public final class GourdListenerDispatcher {

    /**
     * 默认以100ms取一次queue
     */
    private static final Integer DEFAULT_INTERVAL_MILLS = 100;

    /**
     * 默认并发度为2
     */
    private static final Integer DEFAULT_CONCURRENCY = 2;

    /**
     * 并发度
     */
    private Integer concurrency;

    public GourdListenerDispatcher(){
        this(DEFAULT_CONCURRENCY);
    }

    public GourdListenerDispatcher(Integer concurrency){
        if(concurrency<=0){
            throw new IllegalArgumentException(" concurrency must greater than 0 ");
        }
        this.concurrency = concurrency;
    }

    /**
     * GourdEvent存储器
     */
    private GourdEventStack eventStack;

    /**
     * GourdEvent队列
     */
    private GourdEventQueue eventQueue;

    /**
     * 重试Handler
     */
    private RetryHandler retryHandler;

    public void setEventStack(GourdEventStack eventStack) {
        this.eventStack = eventStack;
    }

    public void setEventQueue(GourdEventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void setRetryHandler(GourdEventRetryHandler retryHandler) {
        this.retryHandler = retryHandler;
    }

    public void init(){
        createAndStartDispatcherThread();
    }

    private void createAndStartDispatcherThread(){
        for(int i=0; i<concurrency; i++){
            String threadName = "GourdListenerDispatcher-"+i+"-"+System.identityHashCode(this);
            DispatcherThread thread = new DispatcherThread(threadName);
            thread.start();
        }
    }

    /**
     * 处理线程
     */
    class DispatcherThread extends Thread{

        DispatcherThread(final String name){
            super(name);
            setDaemon(true);
        }

        @Override
        public void run() {

            log.info(" GourdListenerDispatcher is starting ");

            for(;;){

                try{
                    GourdEvent event = eventQueue.pull();
                    if(event == null){
                        SystemUtils.sleep(DEFAULT_INTERVAL_MILLS);
                        continue;
                    }

                    doTrigger(event);

                    try{
                        GourdEventListenerBus.post(event);
                    }catch (Exception ex){
                        doFail(event);
                        throw ex;
                    }

                    doSuccess(event);
                }catch (Exception ex){
                    log.error("", ex);
                }
            }
        }
    }

    /**
     * 更新处理状态
     * @param event
     */
    private void doTrigger(GourdEvent event){
        log.debug(" start trigger GourdEvent, event:{} ", event);
        eventStack.trigger(event);
        log.debug(" end trigger GourdEvent, event:{} ", event);
    }

    /**
     * 处理失败
     * @param event
     */
    private void doFail(GourdEvent event){
        log.debug(" start failed GourdEvent, event:{} ", event);
        eventStack.failed(event.getId());
        log.debug(" end failed GourdEvent, event:{} ", event);

        log.debug(" start retry GourdEvent, event:{} ", event);
        retryHandler.handle(event);
        log.debug(" end retry GourdEvent, event:{} ", event);
    }

    /**
     * 处理成功
     * @param event
     */
    private void doSuccess(GourdEvent event){
        log.debug(" start succeed GourdEvent, event:{} ", event);
        eventStack.succeed(event.getId());
        log.debug(" end succeed GourdEvent, event:{} ", event);
    }

}
