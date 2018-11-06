package org.tieland.gourd.common.util;

/**
 * @author zhouxiang
 * @date 2018/10/26 15:44
 */
public final class SystemUtils {

    private SystemUtils(){
        //
    }

    /**
     * 当前线程sleep
     * @param mills
     */
    public static void sleep(long mills){
        try{
            Thread.sleep(mills);
        }catch (InterruptedException ex){
            //
        }
    }

}
