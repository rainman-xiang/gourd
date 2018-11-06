package org.tieland.gourd.core.support;

import org.tieland.gourd.core.exception.QueueException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2018/10/26 9:19
 */
public abstract class AbstractQueue<T> implements Queue<T> {

    /**
     * 默认timeout为200ms
     */
    private static final Integer DEFAULT_TIMEOUT = 200;

    /**
     * timeout时间，ms为单位
     */
    private Integer timeoutMills;

    public AbstractQueue(final Integer timeoutMills){
        this.timeoutMills = timeoutMills;
    }

    public AbstractQueue(){
        this(DEFAULT_TIMEOUT);
    }

    private BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Override
    public void push(T data) {
        if(data == null){
            throw new IllegalArgumentException();
        }

        try{
            queue.offer(data, timeoutMills, TimeUnit.MILLISECONDS);
        }catch (Exception ex){
            throw new QueueException(" push error ", ex);
        }

    }

    @Override
    public T pull() {
        try {
            return queue.poll(timeoutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new QueueException(" pull error ", e);
        }
    }
}
