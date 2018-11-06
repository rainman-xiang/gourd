package org.tieland.gourd.core.support;

/**
 * 重试Handler
 * @author zhouxiang
 * @date 2018/10/26 14:48
 */
public interface RetryHandler<T> {

    /**
     * 处理重试
     * @param data
     */
    void handle(T data);

}
