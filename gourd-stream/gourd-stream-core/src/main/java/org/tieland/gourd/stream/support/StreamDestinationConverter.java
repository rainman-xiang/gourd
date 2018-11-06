package org.tieland.gourd.stream.support;

import org.tieland.gourd.common.Destination;
import org.tieland.gourd.common.support.DestinationConverter;
import org.tieland.gourd.common.support.ObjectConverterFactory;
import org.tieland.gourd.stream.core.StreamDestination;

/**
 * @author zhouxiang
 * @date 2018/10/30 13:57
 */
public class StreamDestinationConverter implements DestinationConverter {

    @Override
    public String from(Destination destination) {
        StreamDestination streamDestination = (StreamDestination)destination;
        return ObjectConverterFactory.getInstance().get().from(streamDestination);
    }

    @Override
    public Destination to(String json) {
        return ObjectConverterFactory.getInstance().get().to(json, StreamDestination.class);
    }
}
