package org.tieland.gourd.core.receive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.tieland.gourd.core.annotation.GourdListener;
import org.tieland.gourd.core.receive.support.DefaultGourdEventListener;

/**
 * @author zhouxiang
 * @date 2018/10/24 16:42
 */
@Slf4j
public abstract class GourdListenerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    protected ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
    }

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, method -> {
            GourdListener listener = AnnotationUtils.findAnnotation(method, GourdListener.class);
            if(listener!=null){
                String queue = getQueueName(listener);
                GourdEventListenerBus.register(queue, DefaultGourdEventListener.builder()
                        .queue(queue).method(method).target(bean).build());

                String containerName = beanName+"-"+method.getName();
                createGourdListenerContainer(containerName, listener);
            }
        });
        return bean;
    }

    /**
     * 组装queue name
     * @param listener
     * @return
     */
    protected abstract String getQueueName(GourdListener listener);

    /**
     * 创建监听container
     * @param beanName
     * @param listener
     */
    protected abstract void createGourdListenerContainer(String beanName, GourdListener listener);
}
