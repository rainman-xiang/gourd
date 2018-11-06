package org.tieland.gourd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.send.GourdBus;
import org.tieland.gourd.core.annotation.GourdValve;
import org.tieland.gourd.stream.core.StreamDestination;
import org.tieland.gourd.vo.TestVO;

/**
 * @author zhouxiang
 * @date 2018/10/30 9:28
 */
@Slf4j
@Component
@EnableBinding({Source.class})
public class TestService {

    @GourdValve(grade = GourdValve.ValveGrade.LOW)
    public void test(){
        GourdBus.getInstance().post(Gourd.builder().payload(TestVO.builder().id(2000).name("zzz").build())
                .destination(StreamDestination.builder().destination(Source.OUTPUT).build()).build());
    }

    public void test1(){

    }

}
