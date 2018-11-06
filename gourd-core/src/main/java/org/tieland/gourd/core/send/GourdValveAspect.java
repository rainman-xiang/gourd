package org.tieland.gourd.core.send;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.tieland.gourd.core.annotation.GourdValve;
import org.tieland.gourd.core.exception.GourdContextException;
import org.tieland.gourd.core.send.support.GourdTransactionSynchronization;

/**
 * @author zhouxiang
 * @date 2018/10/24 13:57
 */
@Slf4j
@Aspect
public final class GourdValveAspect {

    /**
     * Gourd路由器
     */
    private GourdRouter router;

    public GourdValveAspect(GourdRouter router){
        this.router = router;
    }

    @Around(value = "@annotation(org.tieland.gourd.core.annotation.GourdValve)")
    public final void around(ProceedingJoinPoint pjp) throws Throwable {
        doBegin(pjp);

        try{
            pjp.proceed();
        }catch (Exception ex){
            doCancel();
            throw ex;
        }

        doConfirm();
    }

    /**
     * 准备发送Gourd阶段
     * 判断是否存在事务上下文
     * @param pjp
     */
    private void doBegin(ProceedingJoinPoint pjp){
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        GourdValve valve = AnnotationUtils.findAnnotation(signature.getMethod(), GourdValve.class);

        if(GourdValve.ValveGrade.HIGH == valve.grade()){
            if(!TransactionSynchronizationManager.isActualTransactionActive()){
                throw new GourdContextException(" no transaction exist ");
            }

            TransactionSynchronizationManager.registerSynchronization(new GourdTransactionSynchronization(router));
        }else{
            if(TransactionSynchronizationManager.isActualTransactionActive()){
                throw new GourdContextException(" because exist transaction, so must use GourdValve.ValveGrade.HIGH ");
            }
        }

    }

    /**
     * 确认发送Gourd
     */
    private void doConfirm(){
        if(!TransactionSynchronizationManager.isActualTransactionActive()){
            if(GourdContextHolder.getContext() == null){
                throw new GourdContextException(" no GourdContext exist ");
            }

            router.confirm(GourdContextHolder.getContext());
        }
    }

    /**
     * 取消发送Gourd
     */
    private void doCancel(){
        if(!TransactionSynchronizationManager.isActualTransactionActive()){
            router.cancel(GourdContextHolder.getContext());
        }
    }
}
