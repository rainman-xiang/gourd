package org.tieland.gourd.core.receive;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.tieland.gourd.common.GourdEvent;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxiang
 * @date 2018/10/24 16:53
 */
@Slf4j
public final class GourdEventListenerBus {

    private static Map<String, GourdEventListener> listeners = new ConcurrentHashMap<>();

    private GourdEventListenerBus(){
        //
    }

    /**
     * 注册GourdEventListener
     * @param queue
     * @param listener
     */
    public static void register(final String queue, GourdEventListener listener){
        if(Strings.isNullOrEmpty(queue)){
            throw new IllegalArgumentException(" queue must not null or empty ");
        }

        listeners.put(queue, listener);
    }

    /**
     * 取消GourdEventListener
     * @param queue
     */
    public static void unregister(final String queue){
        listeners.remove(queue);
    }

    /**
     * 根据GourdEvent分发GourdEventListener
     * @param event
     */
    public static void post(GourdEvent event){
        GourdEventListener listener = getListener(event);
        if(listener == null){
            log.warn(" event:{} has no listener ", event);
            return;
        }

        listener.listen(event);
    }

    /**
     * 选择GourdEvent对应的GourdEventListener
     * @param event
     * @return
     */
    private static GourdEventListener getListener(GourdEvent event){
        if(Objects.isNull(event)){
            throw new IllegalArgumentException(" GourdEvent must not null ");
        }

        if(Strings.isNullOrEmpty(event.getQueue())){
            throw new IllegalArgumentException(" queue must not null or empty with GourdEvent ");
        }

        for(Map.Entry<String, GourdEventListener> entry:listeners.entrySet()){
            if(entry.getKey().equals(event.getQueue())){
                return entry.getValue();
            }
        }

        return null;
    }
}
