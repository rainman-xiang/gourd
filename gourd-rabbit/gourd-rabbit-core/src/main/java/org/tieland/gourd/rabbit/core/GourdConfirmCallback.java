package org.tieland.gourd.rabbit.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:44
 */
@Slf4j
public class GourdConfirmCallback extends AbstractCallback implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" CorrelationData:{}, ack:{}, cause:{} ", correlationData, ack, cause);

        try {
            if(ack){
                if (!CallbackErrorsHolder.getInstance().hasError(correlationData.getId())){
                    try{
                        doSuccess(correlationData.getId());
                    }catch (Exception ex){
                        log.error("", ex);
                    }

                    return;
                }
            }

            log.error(" CorrelationData:{}, ack:{}, cause:{} ", correlationData, ack, cause);

            try{
                if(!CallbackErrorsHolder.getInstance().hasError(correlationData.getId())){
                    CallbackErrorsHolder.getInstance().error(correlationData.getId());
                    doFail(correlationData.getId());
                }
            }catch (Exception ex){
                log.error("", ex);
            }

        }finally {
            CallbackErrorsHolder.getInstance().remove(correlationData.getId());
        }
    }
}
