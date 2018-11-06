package org.tieland.gourd.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:39
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue1(){
        return new Queue("demo");
    }

    @Bean
    public Queue queue2(){
        return new Queue("demo-2");
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("d-1");
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue1()).to(directExchange()).with("test-2");
    }

}
