package org.tieland.gourd.core.support;

/**
 * 队列
 * @author zhouxiang
 * @date 2018/10/26 9:14
 */
public interface Queue<T> {

    /**
     * 入列
     * @param data
     */
    void push(T data);

    /**
     * 出列
     * @return
     */
    T pull();

}
