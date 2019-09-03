package com.lts.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${name}")
    private String name;

    @Value("${age}")
    private Integer age;

    @Autowired
    private StudentProperties studentProperties;

    @RequestMapping("/hello")
    public String hello(){
        return "hello spring boot _ LTS";
    }

    @RequestMapping("/hello_new")
    public String hello_new(){
        return name + age;
    }

    @RequestMapping("/hello_new2")
    public String hello_new2(){
        return studentProperties.getName() + studentProperties.getAge();
    }

}
