package org.tieland.gourd.common;

import lombok.*;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/25 18:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GourdPayload implements Serializable {

    private static final long serialVersionUID = 2225465178299533763L;

    /**
     * id
     */
    private String id;

    /**
     * 内容
     */
    private String content;

}
