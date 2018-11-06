package org.tieland.gourd.stack.api;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

/**
 * 处理节点
 * @author zhouxiang
 * @date 2018/10/25 18:37
 */
@Data
@Builder
public class HandleNode {

    /**
     * id
     */
    private String id;

    /**
     * 处理状态
     */
    private HandleStatus status;

    /**
     * 处理次数
     */
    private Integer times;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 数据版本
     */
    private Integer version;

}
