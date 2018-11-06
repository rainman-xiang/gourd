package org.tieland.gourd.retry.support;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.send.GourdQueue;
import org.tieland.gourd.retry.core.CompensateExecutor;

/**
 * @author zhouxiang
 * @date 2018/10/31 10:59
 */
@Slf4j
public class GourdCompensateExecutor implements CompensateExecutor<Gourd> {

    /**
     * Gourd队列
     */
    private GourdQueue queue;

    public void setQueue(GourdQueue queue) {
        this.queue = queue;
    }

    @Override
    public void execute(Gourd gourd) {
        try{
            log.debug(" start push gourd retry to queue. gourd:{} ", gourd);
            queue.push(gourd);
            log.debug(" success push gourd retry to queue. gourd:{} ", gourd);
        }catch (Exception ex){
            log.error(" push gourd retry to queue error ", ex);
        }
    }

}
