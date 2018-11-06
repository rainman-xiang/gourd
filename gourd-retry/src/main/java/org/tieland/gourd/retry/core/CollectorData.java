package org.tieland.gourd.retry.core;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouxiang
 * @date 2018/10/31 13:42
 */
@Data
public abstract class CollectorData<T extends Serializable> {

    /**
     * 数据集list
     */
    private List<T> list;

    /**
     * 是否有更多数据
     */
    private boolean hasMore;

}
