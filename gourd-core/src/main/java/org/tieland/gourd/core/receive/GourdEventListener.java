package org.tieland.gourd.core.receive;

import org.tieland.gourd.common.GourdEvent;

/**
 * @author zhouxiang
 * @date 2018/10/26 9:09
 */
public interface GourdEventListener {

    /**
     * 监听GourdEvent
     * @param event
     */
    void listen(GourdEvent event);

}
