package org.tieland.gourd.rabbit.support;

import org.tieland.gourd.common.Destination;
import org.tieland.gourd.common.support.DestinationConverter;
import org.tieland.gourd.common.support.ObjectConverterFactory;
import org.tieland.gourd.rabbit.core.RabbitDestination;

/**
 * @author zhouxiang
 * @date 2018/10/29 15:07
 */
public class RabbitDestinationConverter implements DestinationConverter {

    @Override
    public String from(Destination destination) {
        RabbitDestination rabbitDestination = (RabbitDestination)destination;
        return ObjectConverterFactory.getInstance().get().from(rabbitDestination);
    }

    @Override
    public Destination to(String json) {
        return ObjectConverterFactory.getInstance().get().to(json, RabbitDestination.class);
    }
}
