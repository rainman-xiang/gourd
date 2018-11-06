package org.tieland.gourd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tieland.gourd.stream.core.StreamBindingBus;

/**
 * @author zhouxiang
 * @date 2018/11/1 9:26
 */
@Configuration
public class TestConfig {

//    @Bean
//    public StreamBindingBus bindingBus(BindingServiceProperties bindingServiceProperties, ProvisioningProvider provisioningProvider){
//        ConsumerProperties consumerProperties = bindingServiceProperties.getConsumerProperties("input");
//        provisioningProvider.provisionConsumerDestination("demo-3", "test", consumerProperties);
//        return new StreamBindingBus(bindingServiceProperties);
//    }

}
