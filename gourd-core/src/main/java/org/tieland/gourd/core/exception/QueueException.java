package org.tieland.gourd.core.exception;

/**
 * @author zhouxiang
 * @date 2018/11/5 10:28
 */
public class QueueException extends RuntimeException {

    public QueueException(final String message, Throwable throwable){
        super(message, throwable);
    }

}
