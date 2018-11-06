package org.tieland.gourd.rabbit.core;

import lombok.*;
import org.tieland.gourd.common.Destination;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RabbitDestination implements Destination {

    private static final long serialVersionUID = -6346893602422829930L;

    /**
     * exchange
     */
    private String exchange;

    /**
     * routingKey
     */
    private String routingKey;

}
