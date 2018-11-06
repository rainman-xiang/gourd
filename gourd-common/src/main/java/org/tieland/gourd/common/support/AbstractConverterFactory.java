package org.tieland.gourd.common.support;

import com.google.common.collect.Iterators;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author zhouxiang
 * @date 2018/11/5 9:36
 */
public abstract class AbstractConverterFactory<T extends Converter> implements ConverterFactory {

    private T converter;

    protected AbstractConverterFactory(){
        load();
    }

    protected void load(){
        ServiceLoader<T> serviceLoader = ServiceLoader.load(getTypeClass());
        Iterator<T> converterIterator = serviceLoader.iterator();
        int size = Iterators.size(converterIterator);
        if(size > 1){
            throw new ConverterException(String.format("%s, it has %d converters, more than 1 converter ", converter, Iterators.size(converterIterator)));
        }

        if(size == 0){
            throw new ConverterException(String.format("%s, there is no converter ", converter));
        }

        converter = Iterators.getNext(serviceLoader.iterator(), null);
    }

    private Class<T> getTypeClass(){
        Type type = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        return (Class)params[0];
    }

    @Override
    public T get() {
        return converter;
    }
}
