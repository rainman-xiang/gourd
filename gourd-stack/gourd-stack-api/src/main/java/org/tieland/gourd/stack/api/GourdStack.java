package org.tieland.gourd.stack.api;

import org.tieland.gourd.common.Gourd;

/**
 * @author zhouxiang
 * @date 2018/10/26 8:53
 */
public interface GourdStack extends HandleStack<String, Gourd> {

    /**
     * 预准备
     * @param gourd
     */
    void prepare(Gourd gourd);

    /**
     * 取消预准备
     * @param gourd
     */
    void cancelPrepare(Gourd gourd);

}
