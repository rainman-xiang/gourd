package org.tieland.gourd.core.send;

import org.tieland.gourd.common.Gourd;

/**
 * @author zhouxiang
 * @date 2018/10/24 9:21
 */
public final class GourdBus {

    private static GourdBus instance = new GourdBus();

    private GourdBus(){
        //
    }

    public static GourdBus getInstance(){
        return instance;
    }

    /**
     * 发送gourd
     * @param gourd
     */
    public void post(Gourd gourd){
        GourdContextHolder.bind(new GourdContext(gourd));
    }

}
