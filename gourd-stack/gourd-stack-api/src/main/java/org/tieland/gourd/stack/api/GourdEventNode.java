package org.tieland.gourd.stack.api;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.tieland.gourd.common.GourdEvent;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhouxiang
 * @date 2018/10/26 9:42
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class GourdEventNode implements Serializable {

    private static final long serialVersionUID = -8031372364608403546L;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * event
     */
    private GourdEvent event;

    /**
     * 处理node
     */
    private HandleNode handleNode;

}
