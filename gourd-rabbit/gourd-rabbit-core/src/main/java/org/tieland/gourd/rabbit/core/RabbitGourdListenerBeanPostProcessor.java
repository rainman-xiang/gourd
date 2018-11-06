package org.tieland.gourd.rabbit.core;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.StringUtils;
import org.tieland.gourd.core.annotation.GourdListener;
import org.tieland.gourd.core.receive.GourdListenerBeanPostProcessor;
import java.util.Collections;

/**
 * @author zhouxiang
 * @date 2018/10/24 17:23
 */
public class RabbitGourdListenerBeanPostProcessor extends GourdListenerBeanPostProcessor {

    @Override
    protected String getQueueName(GourdListener listener){
        return Strings.isNullOrEmpty(listener.group())?listener.queue() :
                Joiner.on(".").join(listener.queue(), listener.group());
    }

    @Override
    protected void createGourdListenerContainer(String containerName, GourdListener listener) {
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        buildBinding(beanFactory, listener);
        String customContainerName = new StringBuilder(containerName).append("MessageListenerContainer").toString();
        CachingConnectionFactory connectionFactory = configurableApplicationContext.getBean(CachingConnectionFactory.class);
        beanFactory.registerBeanDefinition(customContainerName,
                BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class)
                        .addConstructorArgValue(connectionFactory)
                        .addPropertyValue("queues", getQueueName(listener))
                        .addPropertyValue("acknowledgeMode", AcknowledgeMode.MANUAL)
                        .addPropertyReference("channelAwareMessageListener", StringUtils.uncapitalize(GourdMessageListener.class.getSimpleName()))
                        .getBeanDefinition()
        );
    }

    /**
     * 创建Rabbit binding相关
     * @param beanFactory
     * @param listener
     */
    private void buildBinding(BeanDefinitionRegistry beanFactory, GourdListener listener){
        //创建Exchange
        String exchangeName = Joiner.on("_").join(listener.queue(), "TopicExchange");
        beanFactory.registerBeanDefinition(exchangeName, BeanDefinitionBuilder.genericBeanDefinition(TopicExchange.class)
                .addConstructorArgValue(listener.queue())
                .getBeanDefinition());

        //创建Queue
        String queueName = getQueueName(listener);
        beanFactory.registerBeanDefinition(queueName, BeanDefinitionBuilder.genericBeanDefinition(Queue.class)
                .addConstructorArgValue(queueName)
                .getBeanDefinition());

        //创建Binding
        String bindingName = Joiner.on("_").join(queueName, "Binding");
        beanFactory.registerBeanDefinition(bindingName, BeanDefinitionBuilder.genericBeanDefinition(Binding.class)
                .addConstructorArgValue(queueName)
                .addConstructorArgValue(Binding.DestinationType.QUEUE)
                .addConstructorArgValue(listener.queue())
                .addConstructorArgValue("#")
                .addConstructorArgValue(Collections.emptyMap())
                .getBeanDefinition());
    }
}
