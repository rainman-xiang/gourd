package org.tieland.gourd.retry.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.retry.config.RetryProperties;
import org.tieland.gourd.retry.core.AbstractCompensator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouxiang
 * @date 2018/10/31 14:51
 */
@Slf4j
public class GourdCompensator extends AbstractCompensator<Gourd> {

    public GourdCompensator(RetryProperties retryProperties) {
        super(retryProperties);
    }

    public void init(){
        log.info(" starting GourdCompensator ");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
                new BasicThreadFactory.Builder().namingPattern("GourdCompensator-Scheduled-Pool-%d").daemon(true).build());
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
