package org.tieland.gourd.core.support;

import org.tieland.gourd.stack.api.HandleNode;

/**
 * 重试策略
 * @author zhouxiang
 * @date 2018/10/26 15:04
 */
public interface RetryStrategy {

    /**
     * 是否满足重试条件
     * @param node
     * @return
     */
    boolean decide(HandleNode node);

}
