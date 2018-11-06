package org.tieland.gourd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tieland.gourd.service.TestService;

/**
 * @author zhouxiang
 * @date 2018/10/30 9:31
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public String test(){
        testService.test();
        return "success";
    }

    @GetMapping("/test1")
    public String test1(){
        return "success";
    }

}
