package org.tieland.gourd.stream.core;

import lombok.*;
import org.tieland.gourd.common.Destination;

/**
 * @author zhouxiang
 * @date 2018/10/30 13:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StreamDestination implements Destination {

    /**
     * destination
     */
    private String destination;

}
