package org.tieland.gourd.stream.core;

import org.springframework.messaging.MessageChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxiang
 * @date 2018/10/30 11:00
 */
public final class MessageChannelBus {

    private static Map<String, MessageChannel> map = new ConcurrentHashMap<>();

    private MessageChannelBus(){
        //
    }

    /**
     * 注册channel
     * @param name
     * @param channel
     */
    public static void register(String name, MessageChannel channel){
        map.put(name, channel);
    }

    public static MessageChannel getChannel(String name){
        return map.get(name);
    }

}
