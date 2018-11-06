package org.tieland.gourd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tieland.gourd.core.annotation.GourdListener;
import org.tieland.gourd.vo.TestVO;

/**
 * @author zhouxiang
 * @date 2018/10/24 17:38
 */
@Slf4j
@Component
public class TestConsumer {

    @GourdListener(queue = "demo-3")
    public void consumer(TestVO vo){
        log.info("vo:{}", vo);
    }

}
