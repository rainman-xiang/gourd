package org.tieland.gourd.rabbit.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.tieland.gourd.common.Gourd;

import java.nio.charset.StandardCharsets;

/**
 * @author zhouxiang
 * @date 2018/10/24 11:15
 */
@Slf4j
public class GourdMessagePostProcessor implements MessagePostProcessor {

    /**
     * Gourd
     */
    private Gourd gourd;

    public GourdMessagePostProcessor(final Gourd gourd){
        this.gourd = gourd;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setMessageId(gourd.getId());
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.name());
        return message;
    }

}
