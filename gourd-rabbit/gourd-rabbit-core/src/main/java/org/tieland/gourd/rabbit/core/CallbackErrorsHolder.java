package org.tieland.gourd.rabbit.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouxiang
 * @date 2018/10/26 23:01
 */
public final class CallbackErrorsHolder {

    private static CallbackErrorsHolder instance = new CallbackErrorsHolder();

    private Map<String, Boolean> errors = new ConcurrentHashMap<>();

    private CallbackErrorsHolder(){
        //
    }

    public static CallbackErrorsHolder getInstance() {
        return instance;
    }

    /**
     * 记录error
     * @param id
     */
    public void error(String id){
        errors.put(id, Boolean.TRUE);
    }

    public boolean hasError(String id){
        return errors.get(id) != null;
    }

    public void remove(String id){
        errors.remove(id);
    }
}
