package org.tieland.gourd.rabbit.support;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.exception.NotSupportException;
import org.tieland.gourd.core.send.GourdContext;
import org.tieland.gourd.core.send.GourdRouter;
import org.tieland.gourd.rabbit.core.RabbitDestination;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/29 9:40
 */
@Slf4j
public class ProxyRabbitTemplate extends RabbitTemplate {

    /**
     * Gourd路由器
     */
    private GourdRouter gourdRouter;

    public ProxyRabbitTemplate(ConnectionFactory connectionFactory){
        super(connectionFactory);
    }

    public void setGourdRouter(GourdRouter gourdRouter) {
        this.gourdRouter = gourdRouter;
    }

    @Override
    public void doSend(Channel channel, String exchange, String routingKey, Message message, boolean mandatory, CorrelationData correlationData) throws Exception {
        log.debug(" proxy doSend ");
        Object object = getMessageConverter().fromMessage(message);
        if(!(object instanceof Serializable)){
            throw new NotSupportException(" Message must be Serializable ");
        }

        if(TransactionSynchronizationManager.isActualTransactionActive()){
            throw new NotSupportException(" it is exist transaction at current thread ");
        }

        log.debug(" start confirm to router ");
        gourdRouter.direct(new GourdContext(Gourd.builder().payload((Serializable)object).destination(RabbitDestination.builder()
                .exchange(exchange).routingKey(routingKey).build()).build()));
        log.debug(" end confirm to router ");

    }
}
