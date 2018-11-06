package org.tieland.gourd.retry.core;

/**
 * @author zhouxiang
 * @date 2018/10/31 10:37
 */
public interface Compensator {

    /**
     * 补偿
     */
    void compensate();

}
