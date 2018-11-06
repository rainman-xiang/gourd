package org.tieland.gourd.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.core.send.GourdBus;
import org.tieland.gourd.core.annotation.GourdValve;
import org.tieland.gourd.domain.TestDO;
import org.tieland.gourd.domain.repository.TestRepository;
import org.tieland.gourd.rabbit.core.RabbitDestination;
import org.tieland.gourd.vo.TestVO;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:27
 */
@Component
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GourdValve(grade = GourdValve.ValveGrade.LOW)
    public void test(){
        GourdBus.getInstance().post(Gourd.builder().payload(TestVO.builder().id(100).name("test-100").build())
                .destination(RabbitDestination.builder().exchange("d-1").routingKey("test-2").build()).build());
    }

    @GourdValve
    @Transactional(rollbackFor = Exception.class)
    public void test1(){
        TestDO testDO = new TestDO();
        testDO.setName("test");
        testRepository.save(testDO);
        GourdBus.getInstance().post(Gourd.builder().payload(TestVO.builder().id(101).name("test-100").build())
                .destination(RabbitDestination.builder().exchange("d-1").routingKey("test-2").build()).build());
    }

    @GourdValve
    @Transactional(rollbackFor = Exception.class)
    public void test2(){
        TestDO testDO = new TestDO();
        testDO.setName("test");
        testRepository.save(testDO);
        GourdBus.getInstance().post(Gourd.builder().payload(TestVO.builder().id(102).name("test-100").build())
                .destination(RabbitDestination.builder().exchange("d-1").routingKey("test-1").build()).build());
        //throw new RuntimeException();
    }

    @GourdValve
    @Transactional(rollbackFor = RuntimeException.class)
    public void test3(){
        TestDO testDO = new TestDO();
        testDO.setName("test");
        testRepository.save(testDO);
        GourdBus.getInstance().post(Gourd.builder().payload(TestVO.builder().id(103).name("test-100").build())
                .destination(RabbitDestination.builder().exchange("d-1").routingKey("test-2").build()).build());
        throw new RuntimeException();
    }

    @GourdValve(grade = GourdValve.ValveGrade.LOW)
    public void test4(){
        GourdBus.getInstance().post(Gourd.builder().payload(TestVO.builder().id(100).name("test-100").build())
                .destination(RabbitDestination.builder().exchange("d-1").routingKey("test-2").build()).build());
        rabbitTemplate.convertAndSend("d-1", "test-2", TestVO.builder().id(104).name("test-105").build());
    }

}
