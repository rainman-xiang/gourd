package org.tieland.gourd.retry.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.tieland.gourd.retry.config.RetryProperties;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/31 11:10
 */
@Slf4j
public abstract class AbstractCompensator<T extends Serializable> implements Compensator {

    /**
     *
     */
    private Condition condition;

    /**
     * 补偿执行Executor
     */
    private CompensateExecutor<T> executor;

    /**
     * 补偿数据Collector
     */
    private CompensateCollector<T> collector;

    protected RetryProperties retryProperties;

    public AbstractCompensator(RetryProperties retryProperties){
        condition = Condition.builder().maxRetry(retryProperties.getMaxRetry())
                .maxSize(retryProperties.getMaxSize()).build();
        this.retryProperties = retryProperties;
    }

    public void setExecutor(CompensateExecutor<T> executor) {
        this.executor = executor;
    }

    public void setCollector(CompensateCollector<T> collector) {
        this.collector = collector;
    }

    @Override
    public final void compensate(){
        CollectorData<T> data = collector.execute(condition);

        if(data!=null){
            do{
                if(CollectionUtils.isNotEmpty(data.getList())){
                    for(T element:data.getList()){
                        executor.execute(element);
                    }
                }
            }while(data.isHasMore());

            return;
        }

        log.info(" no compensate data ");
    }

}
