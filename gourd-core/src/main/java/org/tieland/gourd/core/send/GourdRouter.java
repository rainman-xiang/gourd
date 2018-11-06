package org.tieland.gourd.core.send;

/**
 * @author zhouxiang
 * @date 2018/10/24 13:39
 */
public interface GourdRouter {

    /**
     * 开始传递
     * @param context
     */
    void begin(GourdContext context);

    /**
     * 确认传递
     * @param context
     */
    void confirm(GourdContext context);

    /**
     * 直接传递
     * @param context
     */
    void direct(GourdContext context);

    /**
     * 取消传递
     * @param context
     */
    void cancel(GourdContext context);
}
