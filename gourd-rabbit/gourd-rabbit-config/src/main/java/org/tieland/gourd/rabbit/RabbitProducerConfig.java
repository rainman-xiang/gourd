package org.tieland.gourd.rabbit;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.core.send.*;
import org.tieland.gourd.core.send.support.GourdRetryHandler;
import org.tieland.gourd.core.send.support.DefaultGourdRouter;
import org.tieland.gourd.rabbit.core.GourdConfirmCallback;
import org.tieland.gourd.rabbit.core.GourdReturnCallback;
import org.tieland.gourd.rabbit.support.RabbitGourdDeliverHandler;
import org.tieland.gourd.stack.api.GourdStack;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:55
 */
@Configuration
public class RabbitProducerConfig {

    @Bean
    public GourdDeliverHandler deliverHandler(CachingConnectionFactory connectionFactory, GourdConfirmCallback confirmCallback,
                                              GourdReturnCallback returnCallback){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        return new RabbitGourdDeliverHandler(rabbitTemplate);
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
