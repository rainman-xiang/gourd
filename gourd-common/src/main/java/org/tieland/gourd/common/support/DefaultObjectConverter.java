package org.tieland.gourd.common.support;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/11/5 10:05
 */
public class DefaultObjectConverter implements ObjectConverter {

    @Override
    public <T extends Serializable> String from(T object) {
        return JSON.toJSONString(object);
    }

    @Override
    public <T extends Serializable> T to(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
