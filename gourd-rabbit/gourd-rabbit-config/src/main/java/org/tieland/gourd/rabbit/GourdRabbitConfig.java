package org.tieland.gourd.rabbit;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.core.send.GourdRouter;
import org.tieland.gourd.core.send.support.GourdRetryHandler;
import org.tieland.gourd.rabbit.core.GourdConfirmCallback;
import org.tieland.gourd.rabbit.core.GourdReturnCallback;
import org.tieland.gourd.rabbit.support.ProxyRabbitTemplate;
import org.tieland.gourd.stack.api.GourdStack;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:55
 */
@Configuration
@ImportAutoConfiguration({RabbitConsumerConfig.class, RabbitProducerConfig.class})
public class GourdRabbitConfig {

    @Autowired
    public void connectionFactory(CachingConnectionFactory connectionFactory){
        if(!connectionFactory.isPublisherConfirms()){
            connectionFactory.setPublisherConfirms(true);
        }

        if(!connectionFactory.isPublisherReturns()){
            connectionFactory.setPublisherReturns(true);
        }
    }

    @Bean
    public GourdConfirmCallback confirmCallback(GourdStack stack, GourdRetryHandler retryHandler){
        GourdConfirmCallback callback = new GourdConfirmCallback();
        callback.setStack(stack);
        callback.setRetryHandler(retryHandler);
        return callback;
    }

    @Bean
    public GourdReturnCallback returnCallback(GourdStack stack, GourdRetryHandler retryHandler){
        GourdReturnCallback callback = new GourdReturnCallback();
        callback.setStack(stack);
        callback.setRetryHandler(retryHandler);
        return callback;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory, GourdRouter gourdRouter){
        ProxyRabbitTemplate rabbitTemplate = new ProxyRabbitTemplate(connectionFactory);
        rabbitTemplate.setGourdRouter(gourdRouter);
        return rabbitTemplate;
    }
}
