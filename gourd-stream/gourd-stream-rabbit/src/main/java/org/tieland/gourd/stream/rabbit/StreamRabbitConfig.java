package org.tieland.gourd.stream.rabbit;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.stream.core.StreamBeanPostProcessor;

/**
 * @author zhouxiang
 * @date 2018/10/30 14:00
 */
@Configuration
@ImportAutoConfiguration({StreamProducerConfig.class, StreamConsumerConfig.class})
public class StreamRabbitConfig {

    @Bean
    public StreamBeanPostProcessor streamBeanPostProcessor(){
        StreamBeanPostProcessor beanPostProcessor = new StreamBeanPostProcessor();
        return beanPostProcessor;
    }

}
