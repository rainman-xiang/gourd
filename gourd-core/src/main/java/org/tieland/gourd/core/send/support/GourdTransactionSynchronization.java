package org.tieland.gourd.core.send.support;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.tieland.gourd.core.send.GourdContextHolder;
import org.tieland.gourd.core.send.GourdRouter;
import org.tieland.gourd.core.exception.GourdContextException;

/**
 * Gourd事务同步器
 * @author zhouxiang
 * @date 2018/10/24 13:54
 */
public class GourdTransactionSynchronization extends TransactionSynchronizationAdapter{

    /**
     * Gourd路由器
     */
    private GourdRouter router;

    public GourdTransactionSynchronization(GourdRouter router){
        this.router = router;
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        if(GourdContextHolder.getContext() == null){
            throw new GourdContextException(" no GourdContext exist ");
        }

        router.begin(GourdContextHolder.getContext());
    }

    @Override
    public void afterCompletion(int status) {
        if(STATUS_COMMITTED == status){
            router.confirm(GourdContextHolder.getContext());
            return;
        }

        if(GourdContextHolder.getContext()!=null){
            router.cancel(GourdContextHolder.getContext());
        }
    }
}
