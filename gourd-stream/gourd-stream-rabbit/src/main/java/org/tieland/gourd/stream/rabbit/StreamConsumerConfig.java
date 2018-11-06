package org.tieland.gourd.stream.rabbit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.core.receive.GourdEventQueue;
import org.tieland.gourd.core.receive.support.GourdEventRetryHandler;
import org.tieland.gourd.core.receive.GourdListenerDispatcher;
import org.tieland.gourd.rabbit.core.GourdMessageListener;
import org.tieland.gourd.rabbit.core.RabbitGourdListenerBeanPostProcessor;
import org.tieland.gourd.stack.api.GourdEventStack;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:54
 */
@Configuration
public class StreamConsumerConfig {

    @Bean
    public GourdEventQueue eventQueue(){
        return new GourdEventQueue();
    }

    @Bean
    @ConditionalOnMissingBean(GourdEventRetryHandler.class)
    public GourdEventRetryHandler eventRetryHandler(GourdEventQueue eventQueue, GourdEventStack eventStack){
        GourdEventRetryHandler retryHandler = new GourdEventRetryHandler();
        retryHandler.setQueue(eventQueue);
        retryHandler.setStack(eventStack);
        return retryHandler;
    }

    @Bean(initMethod = "init")
    public GourdListenerDispatcher listenerDispatcher(GourdEventQueue eventQueue, GourdEventStack eventStack,
                                                      GourdEventRetryHandler retryHandler){
        GourdListenerDispatcher dispatcher = new GourdListenerDispatcher();
        dispatcher.setEventQueue(eventQueue);
        dispatcher.setEventStack(eventStack);
        dispatcher.setRetryHandler(retryHandler);
        return dispatcher;
    }

    @Bean
    public RabbitGourdListenerBeanPostProcessor rabbitGourdListenerBeanPostProcessor(){
        return new RabbitGourdListenerBeanPostProcessor();
    }

    @Bean
    public GourdMessageListener gourdMessageListener(GourdEventQueue eventQueue, GourdEventStack eventStack){
        GourdMessageListener listener = new GourdMessageListener();
        listener.setEventQueue(eventQueue);
        listener.setEventStack(eventStack);
        return listener;
    }

}
