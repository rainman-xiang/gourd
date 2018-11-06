package org.tieland.gourd.stack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhouxiang
 * @date 2018/10/29 16:20
 */
@Data
@ConfigurationProperties(prefix = "gourd.stack")
public class StackProperties {

    private String serviceId;

}
