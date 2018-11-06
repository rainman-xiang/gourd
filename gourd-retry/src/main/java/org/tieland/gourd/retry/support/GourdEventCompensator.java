package org.tieland.gourd.retry.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.retry.config.RetryProperties;
import org.tieland.gourd.retry.core.AbstractCompensator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2018/10/31 14:52
 */
@Slf4j
public class GourdEventCompensator extends AbstractCompensator<GourdEvent> {

    public GourdEventCompensator(RetryProperties retryProperties) {
        super(retryProperties);
    }

    public void init(){
        log.info(" starting GourdEventCompensator ");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
                new BasicThreadFactory.Builder().namingPattern("GourdEventCompensator-Scheduled-Pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(()->{
                    try{
                        compensate();
                    }catch (Exception ex){
                        log.error("", ex);
                    }
                }, 30,
                retryProperties.getRetryInterval(), TimeUnit.SECONDS);
    }

}
