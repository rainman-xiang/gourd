package org.tieland.gourd.common.support;

/**
 * @author zhouxiang
 * @date 2018/10/29 15:01
 */
public final class DestinationConverterFactory extends AbstractConverterFactory<DestinationConverter> {

    private static DestinationConverterFactory instance = new DestinationConverterFactory();

    private DestinationConverterFactory(){
        super.load();
    }

    public static DestinationConverterFactory getInstance() {
        return instance;
    }
}
