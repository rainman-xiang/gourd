package org.tieland.gourd.retry.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.core.receive.GourdEventQueue;
import org.tieland.gourd.core.send.GourdQueue;
import org.tieland.gourd.retry.support.*;
import org.tieland.gourd.stack.api.GourdEventStack;
import org.tieland.gourd.stack.api.GourdStack;

/**
 * @author zhouxiang
 * @date 2018/10/31 9:34
 */
@Configuration
@EnableConfigurationProperties(RetryProperties.class)
public class RetryConfig {

    @Bean
    public GourdCompensateCollector gourdCompensateCollector(GourdStack stack){
        GourdCompensateCollector collector = new GourdCompensateCollector();
        collector.setStack(stack);
        return collector;
    }

    @Bean
    public GourdCompensateExecutor gourdCompensateExecutor(GourdQueue queue){
        GourdCompensateExecutor executor = new GourdCompensateExecutor();
        executor.setQueue(queue);
        return executor;
    }

    @Bean
    public GourdEventCompensateCollector gourdEventCompensateCollector(GourdEventStack stack){
        GourdEventCompensateCollector collector = new GourdEventCompensateCollector();
        collector.setStack(stack);
        return collector;
    }

    @Bean
    public GourdEventCompensateExecutor gourdEventCompensateExecutor(GourdEventQueue queue){
        GourdEventCompensateExecutor executor = new GourdEventCompensateExecutor();
        executor.setQueue(queue);
        return executor;
    }

    @Bean(initMethod = "init")
    public GourdCompensator gourdCompensator(RetryProperties retryProperties,
                                             GourdCompensateCollector collector, GourdCompensateExecutor executor){
        GourdCompensator compensator = new GourdCompensator(retryProperties);
        compensator.setCollector(collector);
        compensator.setExecutor(executor);
        return compensator;
    }

    @Bean(initMethod = "init")
    public GourdEventCompensator gourdEventCompensator(RetryProperties retryProperties,
                                                       GourdEventCompensateCollector collector, GourdEventCompensateExecutor executor){
        GourdEventCompensator compensator = new GourdEventCompensator(retryProperties);
        compensator.setCollector(collector);
        compensator.setExecutor(executor);
        return compensator;
    }
}
