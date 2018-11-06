package org.tieland.gourd.rabbit.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:45
 */
@Slf4j
public class GourdReturnCallback extends AbstractCallback implements RabbitTemplate.ReturnCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error(" message:{}, replyCode:{}, replyText:{}, exchange:{}, routingKey:{} ",
                message, replyCode, replyText, exchange, routingKey);
        try{
            String messageId = message.getMessageProperties().getMessageId();
            CallbackErrorsHolder.getInstance().error(messageId);
            doFail(messageId);
        }catch (Exception ex){
            log.error("", ex);
        }

    }

}
