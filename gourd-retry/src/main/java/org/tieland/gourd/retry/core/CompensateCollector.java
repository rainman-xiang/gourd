package org.tieland.gourd.retry.core;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/31 10:51
 */
public interface CompensateCollector<T extends Serializable> {

    /**
     * 获取数据list
     * @param condition
     * @return
     */
    CollectorData<T> execute(Condition condition);

}
