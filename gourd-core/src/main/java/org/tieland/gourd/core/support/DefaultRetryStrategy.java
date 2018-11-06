package org.tieland.gourd.core.support;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.stack.api.HandleNode;
import org.tieland.gourd.stack.api.HandleStatus;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:08
 */
@Slf4j
public class DefaultRetryStrategy implements RetryStrategy {

    /**
     * 默认最大重试3次
     */
    private static final Integer DEFAULT_MAX_RETRY_TIMES = 3;

    /**
     * 最大重试次数
     */
    private Integer maxRetryTimes;

    public DefaultRetryStrategy(){
        this(DEFAULT_MAX_RETRY_TIMES);
    }

    public DefaultRetryStrategy(Integer maxRetryTimes){
        this.maxRetryTimes = maxRetryTimes;
    }

    @Override
    public boolean decide(HandleNode node) {
        if(node.getStatus()!= HandleStatus.SUCCEED
                && node.getStatus()!=HandleStatus.NEW){
            if(node.getTimes()<maxRetryTimes){
                return true;
            }
        }

        return false;
    }
}
