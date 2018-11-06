package org.tieland.gourd.common.support;

/**
 * @author zhouxiang
 * @date 2018/11/5 9:21
 */
public interface ConverterFactory<T extends Converter> {

    /**
     * 获取Converter
     * @return
     */
    T get();
}
