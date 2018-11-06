package org.tieland.gourd;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/10/26 8:42
 */
@Data
@Builder
public class TestVO implements Serializable {
    private static final long serialVersionUID = 8715728409779830291L;
    private String name;
}
