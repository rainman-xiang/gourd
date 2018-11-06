package org.tieland.gourd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tieland.gourd.core.annotation.GourdListener;
import org.tieland.gourd.vo.TestVO;

/**
 * @author zhouxiang
 * @date 2018/11/1 11:50
 */
@Slf4j
@Component
public class StreamService {


    //@GourdListener(queue = "demo-3", group = "test-2")
    public void consumer(TestVO vo){
        log.info("vo:{}", vo);
    }

    public void consumer1(String json){
        log.info("json:{}", json);
    }

}
