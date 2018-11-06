package org.tieland.gourd.rabbit.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.send.support.AbstractGourdDeliverHandler;
import org.tieland.gourd.rabbit.core.GourdMessagePostProcessor;
import org.tieland.gourd.rabbit.core.RabbitDestination;

/**
 * @author zhouxiang
 * @date 2018/10/24 11:12
 */
@Slf4j
public class RabbitGourdDeliverHandler extends AbstractGourdDeliverHandler {

    private RabbitTemplate rabbitTemplate;

    public RabbitGourdDeliverHandler(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected boolean match(Gourd gourd) {
        return gourd.getDestination() instanceof RabbitDestination;
    }

    @Override
    protected void doHandle(Gourd gourd) {
        RabbitDestination destination = (RabbitDestination)gourd.getDestination();
        log.debug(" invoke rabbitTemplate.convertAndSend. destination:{} ", destination);
        rabbitTemplate.convertAndSend(destination.getExchange(), destination.getRoutingKey(),
                gourd.getPayload(), new GourdMessagePostProcessor(gourd), new CorrelationData(gourd.getId()));

    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
