package org.tieland.gourd.common.support;

import org.tieland.gourd.common.Destination;

/**
 * @author zhouxiang
 * @date 2018/10/29 14:46
 */
public interface DestinationConverter extends Converter {

    /**
     * Destination转化为json
     * @param destination
     * @return
     */
    String from(Destination destination);

    /**
     * json转化为Destination对象
     * @param json
     * @return
     */
    Destination to(String json);

}
