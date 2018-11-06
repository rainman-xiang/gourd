package org.tieland.gourd.common;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/25 22:10
 */
@Data
@ToString
@Builder
public final class GourdEvent implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * mq队列
     */
    private String queue;

    /**
     * 内容-json
     */
    private String payload;

}
