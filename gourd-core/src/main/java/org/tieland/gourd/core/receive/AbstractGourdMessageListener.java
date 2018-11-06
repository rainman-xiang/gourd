package org.tieland.gourd.core.receive;

import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.common.GourdPayload;
import org.tieland.gourd.common.support.ObjectConverterFactory;
import org.tieland.gourd.stack.api.GourdEventStack;

/**
 * @author zhouxiang
 * @date 2018/10/26 11:05
 */
@Slf4j
public abstract class AbstractGourdMessageListener {

    /**
     * GourdEvent存储器
     */
    protected GourdEventStack eventStack;

    /**
     * GourdEvent队列
     */
    protected GourdEventQueue eventQueue;

    public void setEventStack(GourdEventStack eventStack) {
        this.eventStack = eventStack;
    }

    public void setEventQueue(GourdEventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    protected GourdEvent resolvePayload(final String queue, final String json){
        GourdPayload payload = ObjectConverterFactory.getInstance().get().to(json, GourdPayload.class);
        return GourdEvent.builder().payload(payload.getContent()).id(payload.getId())
                .queue(queue).build();
    }

}
