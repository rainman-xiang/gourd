package org.tieland.gourd.retry.core;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/31 10:52
 */
public interface CompensateExecutor<T extends Serializable> {

    /**
     * 执行
     * @param data
     */
    void execute(T data);

}
