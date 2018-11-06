package org.tieland.gourd.retry.support;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.core.receive.GourdEventQueue;
import org.tieland.gourd.retry.core.CompensateExecutor;

/**
 * @author zhouxiang
 * @date 2018/10/31 13:38
 */
@Slf4j
public class GourdEventCompensateExecutor implements CompensateExecutor<GourdEvent> {

    /**
     * GourdEvent队列
     */
    private GourdEventQueue queue;

    public void setQueue(GourdEventQueue queue) {
        this.queue = queue;
    }

    @Override
    public void execute(GourdEvent event) {
        try{
            log.debug(" start push event retry to queue. event:{} ", event);
            queue.push(event);
            log.debug(" success push event retry to queue. event:{} ", event);
        }catch (Exception ex){
            log.error(" push event retry to queue error ", ex);
        }
    }
}
