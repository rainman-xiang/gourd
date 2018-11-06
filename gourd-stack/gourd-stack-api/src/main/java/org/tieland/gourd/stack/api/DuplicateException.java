package org.tieland.gourd.stack.api;

/**
 * @author zhouxiang
 * @date 2018/10/31 17:24
 */
public class DuplicateException extends RuntimeException {

    public DuplicateException(final String message){
        super(message);
    }

}
