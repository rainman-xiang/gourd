package org.tieland.gourd.retry.support;

import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.retry.core.CollectorData;
import org.tieland.gourd.retry.core.CompensateCollector;
import org.tieland.gourd.retry.core.Condition;
import org.tieland.gourd.retry.core.GourdEventCollectorData;
import org.tieland.gourd.stack.api.GourdEventStack;

import java.util.List;

/**
 * @author zhouxiang
 * @date 2018/10/31 13:36
 */
public class GourdEventCompensateCollector implements CompensateCollector<GourdEvent> {

    /**
     * GourdEvent存储器
     */
    private GourdEventStack stack;

    public void setStack(GourdEventStack stack) {
        this.stack = stack;
    }

    @Override
    public GourdEventCollectorData execute(Condition condition) {
        Integer count = stack.getRetryCount(condition.getMaxRetry());
        if(count>0){
            List<GourdEvent> list = stack.getRetryList(condition.getMaxRetry(), condition.getMaxSize());
            GourdEventCollectorData data = GourdEventCollectorData.builder().build();
            data.setHasMore(count>condition.getMaxSize());
            data.setList(list);
            return data;
        }

        return null;
    }
}
