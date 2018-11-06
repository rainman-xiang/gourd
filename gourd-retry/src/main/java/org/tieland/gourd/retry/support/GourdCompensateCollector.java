package org.tieland.gourd.retry.support;

import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.retry.core.CompensateCollector;
import org.tieland.gourd.retry.core.Condition;
import org.tieland.gourd.retry.core.GourdCollectorData;
import org.tieland.gourd.stack.api.GourdStack;

import java.util.List;

/**
 * @author zhouxiang
 * @date 2018/10/31 13:35
 */
public class GourdCompensateCollector implements CompensateCollector<Gourd> {

    /**
     * Gourd存储器
     */
    private GourdStack stack;

    public void setStack(GourdStack stack) {
        this.stack = stack;
    }

    @Override
    public GourdCollectorData execute(Condition condition) {
        Integer count = stack.getRetryCount(condition.getMaxRetry());
        if(count>0){
            List<Gourd> list = stack.getRetryList(condition.getMaxRetry(), condition.getMaxSize());
            GourdCollectorData data = GourdCollectorData.builder().build();
            data.setHasMore(count>condition.getMaxSize());
            data.setList(list);
            return data;
        }

        return null;
    }
}
