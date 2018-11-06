package org.tieland.gourd.common.support;

/**
 * @author zhouxiang
 * @date 2018/11/5 10:02
 */
public final class ObjectConverterFactory extends AbstractConverterFactory<ObjectConverter> {

    private static ObjectConverterFactory instance = new ObjectConverterFactory();
    
    private ObjectConverterFactory(){
        super();
    }

    public static ObjectConverterFactory getInstance() {
        return instance;
    }

}
