package org.tieland.gourd.rabbit.core;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.core.receive.AbstractGourdMessageListener;
import org.tieland.gourd.stack.api.DuplicateException;
import java.nio.charset.StandardCharsets;

/**
 * @author zhouxiang
 * @date 2018/10/24 17:27
 */
@Slf4j
public class GourdMessageListener extends AbstractGourdMessageListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String queue = message.getMessageProperties().getConsumerQueue();
        String content = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info(" receive queue:{}, content:{} ", queue, content);

        GourdEvent event;
        try{
            event = resolvePayload(queue, content);
        }catch (Exception ex){
            log.error("", ex);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        boolean pushable = false;
        try{
            eventStack.createNew(event);
            pushable = true;
        }catch (DuplicateException ex){
            log.error("", ex);
        }catch (Exception ex){
            log.error("", ex);
            channel.basicRecover();
            return;
        }

        log.debug(" event:{} start basicAck ", event);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.debug(" event:{} basicAck success ", event);

        if(pushable){
            try{
                eventQueue.push(event);
            }catch (Exception ex){
                log.error("", ex);
            }
        }

    }
}
