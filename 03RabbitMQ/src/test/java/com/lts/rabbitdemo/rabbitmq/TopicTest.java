package com.lts.rabbitdemo.rabbitmq;

import com.lts.rabbitdemo.rabbit.topic.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {
    @Autowired
    private TopicSender sender;

    @Test
    public void topic() throws Exception{
        sender.Send();
    }

    @Test
    public void topic1() throws Exception{
        sender.Send1();
    }

    @Test
    public void topic2() throws Exception{
        sender.Send2();
    }
}
