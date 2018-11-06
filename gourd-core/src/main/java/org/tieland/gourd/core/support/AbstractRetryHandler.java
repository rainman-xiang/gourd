package org.tieland.gourd.core.support;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxiang
 * @date 2018/10/26 14:52
 */
@Slf4j
public abstract class AbstractRetryHandler<T> implements RetryHandler<T> {

    @Override
    public void handle(T data) {
        if(!canRetry(data)){
            log.info(" data:{} is can not retry ", data);
            return;
        }

        retry(data);
    }

    /**
     * 是否可以重试
     * @param data
     * @return
     */
    protected abstract boolean canRetry(T data);

    /**
     * 重试
     * @param data
     */
    protected abstract void retry(T data);
}
