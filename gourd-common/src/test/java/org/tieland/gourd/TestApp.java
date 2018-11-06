package org.tieland.gourd;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.tieland.gourd.common.support.ObjectConverterFactory;

/**
 * @author zhouxiang
 * @date 2018/10/25 22:05
 */
@Slf4j
public class TestApp {

    @Test
    public void test(){
        log.info(ObjectConverterFactory.getInstance().get().from(TestVO.builder().name("123").build()));
    }

}
