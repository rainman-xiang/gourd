package org.tieland.gourd.stream.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageChannel;

/**
 * @author zhouxiang
 * @date 2018/10/30 10:57
 */
@Slf4j
public class StreamBeanPostProcessor implements BeanPostProcessor {

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof MessageChannel){
            MessageChannelBus.register(beanName, (MessageChannel)bean);
        }
        return bean;
    }
}
