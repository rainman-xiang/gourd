package org.tieland.gourd.stack.api;

/**
 * @author zhouxiang
 * @date 2018/10/25 14:16
 */
public enum HandleStatus {

    /**
     * 初始
     */
    NEW(0),

    /**
     * 处理中
     */
    PROCESSING(1),

    /**
     * 重试中
     */
    RETRYING(2),

    /**
     * 处理失败
     */
    FAILED(-1),

    /**
     * 处理成功
     */
    SUCCEED(99);


    HandleStatus(Integer code){
        this.code = code;
    };

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public static HandleStatus get(final Integer code){
        if(code != null){
            for(HandleStatus value:values()){
                if(value.getCode().equals(code)){
                    return value;
                }
            }
        }

        return null;
    }
}
