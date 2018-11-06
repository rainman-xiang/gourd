package org.tieland.gourd.stack.api;

import java.io.Serializable;
import java.util.List;

/**
 * 处理stack
 * @author zhouxiang
 * @date 2018/10/25 14:28
 */
public interface HandleStack<K extends Serializable, V> {

    /**
     * 获取
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 新创建处理
     * @param value
     */
    void createNew(V value);

    /**
     * 触发处理
     * @param value
     */
    void trigger(V value);

    /**
     * 处理成功
     * @param key
     */
    void succeed(K key);

    /**
     * 处理失败
     * @param key
     */
    void failed(K key);

    /**
     * 删除
     * @param key
     */
    void remove(K key);

    /**
     * 获取处理node
     * @param key
     * @return
     */
    HandleNode getHandleNode(K key);

    /**
     * 获取需要重试总数
     * @param maxRetry 最大重试次数
     * @return
     */
    Integer getRetryCount(Integer maxRetry);

    /**
     * 获取重试list
     * @param maxRetry 最大重试次数
     * @param maxSzie 最大处理条数
     * @return
     */
    List<V> getRetryList(Integer maxRetry, Integer maxSzie);
}
