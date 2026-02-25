package org.example.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @DubboReference
    private HelloService helloService;

    @GetMapping("/ping")
    public String ping(@RequestParam(defaultValue = "dubbo") String name) {
        return helloService.sayHello(name);
    }
}
