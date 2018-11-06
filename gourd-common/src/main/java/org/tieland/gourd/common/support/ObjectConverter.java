package org.tieland.gourd.common.support;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/11/5 9:34
 */
public interface ObjectConverter extends Converter {

    /**
     * object转化成json
     * @param object
     * @return
     */
    <T extends Serializable> String from(T object);

    /**
     * json转化成Object
     * @param json
     * @param clazz
     * @return
     */
    <T extends Serializable> T to(String json, Class<T> clazz);

}
