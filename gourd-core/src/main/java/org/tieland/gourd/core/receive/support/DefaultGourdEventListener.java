package org.tieland.gourd.core.receive.support;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.core.receive.GourdEventListener;
import java.lang.reflect.Method;

/**
 * @author zhouxiang
 * @date 2018/10/24 16:59
 */
@Data
@Builder
@Slf4j
public class DefaultGourdEventListener implements GourdEventListener {

    /**
     * queue
     */
    private String queue;

    /**
     * 方法
     */
    private Method method;

    /**
     * target
     */
    private Object target;

    @Override
    public void listen(GourdEvent event) {
        if(method.getParameterCount()>=1){
            Class<?> parameterType = method.getParameterTypes()[0];
            ReflectionUtils.invokeMethod(method, target, JSON.parseObject(event.getPayload(), parameterType));
        }
    }
}
