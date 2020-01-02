package com.example.lts;

import com.example.lts.Properities.StudentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private StudentProperties studentProperties;

    @RequestMapping("hello")
    public String hello(){
        return "hello spring_boot_lts";
    }

    @RequestMapping("getStudent")
    public String GetStudent(){
        return "my name is :" + studentProperties.getName() + ", age is :" + studentProperties.getAge();
    }
}
