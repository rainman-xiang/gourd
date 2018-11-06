package org.tieland.gourd.stream.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxiang
 * @date 2018/10/30 13:27
 */
public class StreamBindingBus implements InitializingBean {

    private BindingServiceProperties bindingServiceProperties;

    private Map<String, String> busMap = new ConcurrentHashMap<>();

    public StreamBindingBus(BindingServiceProperties bindingServiceProperties){
        this.bindingServiceProperties = bindingServiceProperties;
    }

    public String getBindingName(final String destination){
        return busMap.get(destination);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, BindingProperties> bindingMap = bindingServiceProperties.getBindings();
        for(Map.Entry<String, BindingProperties> entry : bindingMap.entrySet()){
            String destination = entry.getValue().getDestination();
            if(destination == null){
                destination = entry.getKey();
            }

            busMap.put(destination, entry.getKey());
        }
    }
}
