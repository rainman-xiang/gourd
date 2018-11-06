package org.tieland.gourd.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tieland.gourd.service.TestService;

/**
 * @author zhouxiang
 * @date 2018/10/24 10:25
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
        testService.test1();
        return "success";
    }

    @GetMapping("/test2")
    public String test2(){
        testService.test2();
        return "success";
    }

    @GetMapping("/test3")
    public String test3(){
        testService.test3();
        return "success";
    }

    @GetMapping("/test4")
    public String test4(){
        testService.test4();
        return "success";
    }

}
