package org.tieland.gourd.core.send.support;

import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.exception.NotSupportException;
import org.tieland.gourd.core.send.GourdDeliverHandler;

/**
 * @author zhouxiang
 * @date 2018/10/24 11:05
 */
public abstract class AbstractGourdDeliverHandler implements GourdDeliverHandler {

    @Override
    public final void handle(Gourd gourd) {
        if(!match(gourd)){
            throw new NotSupportException(" not match gourd ");
        }

        doHandle(gourd);
    }

    /**
     * 判断是否匹配
     * @param gourd
     * @return
     */
    protected abstract boolean match(Gourd gourd);

    /**
     * 具体处理
     * @param gourd
     */
    protected abstract void doHandle(Gourd gourd);
}
