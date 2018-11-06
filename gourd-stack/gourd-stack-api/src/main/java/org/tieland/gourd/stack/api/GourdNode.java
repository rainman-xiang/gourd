package org.tieland.gourd.stack.api;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.tieland.gourd.common.Gourd;

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
public class GourdNode implements Serializable {

    private static final long serialVersionUID = 8902950270389777581L;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * gourd
     */
    private Gourd gourd;

    /**
     * 处理node
     */
    private HandleNode handleNode;

}
