package org.tieland.gourd.core.send;

import org.tieland.gourd.common.Gourd;

/**
 * @author zhouxiang
 * @date 2018/10/24 11:05
 */
public interface GourdDeliverHandler {

    /**
     * 处理gourd
     * @param gourd
     */
    void handle(Gourd gourd);

    /**
     * 是否异步回调确认
     * @return
     */
    default boolean isAsync() {
        return false;
    }

}
