package org.tieland.gourd.retry.core;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhouxiang
 * @date 2018/10/31 13:47
 */
@Data
@Builder
public class Condition {

    /**
     * 最大数据集大小
     */
    private Integer maxSize;

    /**
     * 最大补偿次数
     */
    private Integer maxRetry;
}
