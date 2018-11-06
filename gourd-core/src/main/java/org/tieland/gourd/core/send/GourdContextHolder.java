package org.tieland.gourd.core.send;

/**
 * @author zhouxiang
 * @date 2018/10/24 13:27
 */
public final class GourdContextHolder {

    private static ThreadLocal<GourdContext> threadLocal = new ThreadLocal<>();

    private GourdContextHolder(){
        //
    }

    /**
     * 绑定当前线程
     * @param context
     */
    public static void bind(GourdContext context){
        threadLocal.set(context);
    }

    /**
     * 解绑当前线程
     */
    public static void unbind(){
        threadLocal.remove();
    }

    public static GourdContext getContext(){
        return threadLocal.get();
    }
}
