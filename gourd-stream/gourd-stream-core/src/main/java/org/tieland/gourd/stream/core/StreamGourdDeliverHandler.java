package org.tieland.gourd.stream.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.send.support.AbstractGourdDeliverHandler;

/**
 * @author zhouxiang
 * @date 2018/10/30 13:33
 */
@Slf4j
public class StreamGourdDeliverHandler extends AbstractGourdDeliverHandler {

    @Override
    protected boolean match(Gourd gourd) {
        return gourd.getDestination() instanceof StreamDestination;
    }

    @Override
    protected void doHandle(Gourd gourd) {
        StreamDestination destination = (StreamDestination)gourd.getDestination();
        MessageChannel channel = MessageChannelBus.getChannel(destination.getDestination());
        if(channel == null){
            throw new StreamException(String.format(" %s no MessageChannel ", destination.getDestination()));
        }

        channel.send(MessageBuilder.withPayload(gourd.getPayload())
                .setCorrelationId(gourd.getId()).build());
    }
}
