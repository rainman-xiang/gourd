package org.tieland.gourd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestVO implements Serializable {

    private static final long serialVersionUID = -6643285588033611801L;

    private Integer id;

    private String name;
}
