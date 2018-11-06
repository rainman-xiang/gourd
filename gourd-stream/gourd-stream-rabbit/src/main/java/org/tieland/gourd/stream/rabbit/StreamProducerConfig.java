package org.tieland.gourd.stream.rabbit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.core.send.*;
import org.tieland.gourd.core.send.support.GourdRetryHandler;
import org.tieland.gourd.core.send.support.DefaultGourdRouter;
import org.tieland.gourd.stack.api.GourdStack;
import org.tieland.gourd.stream.core.StreamGourdDeliverHandler;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:55
 */
@Configuration
public class StreamProducerConfig {

    @Bean
    public GourdDeliverHandler deliverHandler(){
        StreamGourdDeliverHandler deliverHandler = new StreamGourdDeliverHandler();
        return deliverHandler;
    }

    @Bean
    public GourdQueue gourdQueue(){
        return new GourdQueue();
    }

    @Bean
    @ConditionalOnMissingBean(GourdRetryHandler.class)
    public GourdRetryHandler retryHandler(GourdStack stack, GourdQueue queue){
        GourdRetryHandler retryHandler = new GourdRetryHandler();
        retryHandler.setStack(stack);
        retryHandler.setQueue(queue);
        return retryHandler;
    }

    @Bean(initMethod = "init")
    public GourdDeliverDispatcher deliverDispatcher(GourdStack stack, GourdQueue queue,
                                                    GourdRetryHandler retryHandler, GourdDeliverHandler deliverHandler){
        GourdDeliverDispatcher dispatcher = new GourdDeliverDispatcher();
        dispatcher.setStack(stack);
        dispatcher.setQueue(queue);
        dispatcher.setRetryHandler(retryHandler);
        dispatcher.setDeliverHandler(deliverHandler);
        return dispatcher;
    }

    @Bean
    @ConditionalOnMissingBean(GourdRouter.class)
    public GourdRouter gourdRouter(GourdStack stack, GourdQueue queue){
        DefaultGourdRouter router = new DefaultGourdRouter();
        router.setStack(stack);
        router.setQueue(queue);
        return router;
    }

    @Bean
    public GourdValveAspect gourdValveAspect(GourdRouter gourdRouter){
        return new GourdValveAspect(gourdRouter);
    }

}
