package org.tieland.gourd.core.send;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.common.util.SystemUtils;
import org.tieland.gourd.core.support.RetryHandler;
import org.tieland.gourd.stack.api.GourdStack;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:50
 */
@Slf4j
public class GourdDeliverDispatcher {

    private final Integer DEFAULT_INTERVAL_MILLS = 100;

    /**
     * Gourd队列
     */
    private GourdQueue queue;

    /**
     * Gourd存储器
     */
    private GourdStack stack;

    /**
     * Gourd发送Handler
     */
    private GourdDeliverHandler deliverHandler;

    private RetryHandler retryHandler;

    public void setQueue(GourdQueue queue) {
        this.queue = queue;
    }

    public void setStack(GourdStack stack) {
        this.stack = stack;
    }

    public void setDeliverHandler(GourdDeliverHandler deliverHandler) {
        this.deliverHandler = deliverHandler;
    }

    public void setRetryHandler(RetryHandler retryHandler) {
        this.retryHandler = retryHandler;
    }

    /**
     * 初始化
     */
    public void init(){
        createAndStartManagerThread();
    }

    private void createAndStartManagerThread(){
        String threadName = "GourdDeliverDispatcher-"+System.identityHashCode(this);
        DeliverManagerThread thread = new DeliverManagerThread(threadName);
        thread.start();
    }

    /**
     * 发送线程
     */
    class DeliverManagerThread extends Thread {

        DeliverManagerThread(final String name){
            super(name);
            setDaemon(true);
        }

        @Override
        public void run() {

            log.info(" GourdDeliverDispatcher is starting ");

            for(;;){

                try{
                    Gourd gourd = queue.pull();
                    if(gourd == null){
                        SystemUtils.sleep(DEFAULT_INTERVAL_MILLS);
                        continue;
                    }

                    doTrigger(gourd);

                    try{
                        deliverHandler.handle(gourd);
                    }catch (Exception ex){
                        doFail(gourd);
                        throw ex;
                    }

                    doSuccess(gourd);
                }catch (Exception ex){
                    log.error("", ex);
                }
            }
        }
    }

    /**
     * 更新处理状态
     * @param gourd
     */
    private void doTrigger(Gourd gourd){
        log.debug(" start trigger Gourd, gourd:{} ", gourd);
        stack.trigger(gourd);
        log.debug(" end trigger Gourd, gourd:{} ", gourd);
    }

    /**
     * 处理失败
     * @param gourd
     */
    private void doFail(Gourd gourd){
        log.debug(" start failed Gourd, gourd:{} ", gourd);
        stack.failed(gourd.getId());
        log.debug(" end failed Gourd, gourd:{} ", gourd);

        log.debug(" start retry Gourd, gourd:{} ", gourd);
        retryHandler.handle(gourd);
        log.debug(" end retry Gourd, gourd:{} ", gourd);
    }

    /**
     * 处理成功
     * @param gourd
     */
    private void doSuccess(Gourd gourd){
        if(!deliverHandler.isAsync()){
            log.debug(" start succeed Gourd, gourd:{} ", gourd);
            stack.succeed(gourd.getId());
            log.debug(" end succeed Gourd, gourd:{} ", gourd);
        }
    }
}
