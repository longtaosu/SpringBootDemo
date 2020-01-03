# 1.新建项目

参考：<http://www.longtaosu.com/article/16>

# 2.配置

## 2.1POM文件

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

## 2.2application.properties文件

```yaml
spring.application.name=Spring-boot-rabbitmq

spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## 2.3配置类

```java
@Configuration
public class RabbitConfig {

    @Bean
    public Queue Queue() {
        return new Queue("hello");
    }

}
```

# 3.发送/接收

发送

```java
@component
public class HelloSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

}
```

接收

```java
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver  : " + hello);
    }

}
```

测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloTest {

    @Autowired
    private HelloSender helloSender;

    @Test
    public void hello() throws Exception {
        helloSender.send();
    }

}
```

测试结果

![01Hello.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/01Hello.png)

# 4.对象

定义对象：

```java
public class User implements Serializable {
    private String name;
    private String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String toString(){
        return "User{" +
                "name='" + name + '\'' +
                ",pass='" + pass + '\'' +
                '}';
    }
}
```

声明队列

```java
@Configuration
public class RabbitConfig {
    @Bean
    public Queue objectQueue() {
        return new Queue("object");
    }
}
```

发送者

```java
@Component
public class ObjectSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(User user){
        System.out.println("Sender object: " + user.toString());
        this.rabbitTemplate.convertAndSend("object",user);
    }
}
```

接收者

```java
@Component
@RabbitListener(queues = "object")
public class ObjectReceiver {

    @RabbitHandler
    public void process(User user){
        System.out.println("Receiver object : " + user);
    }
}
```

测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectTest {

    @Autowired
    private ObjectSender sender;

    @Test
    public void sendObject() throws Exception{
        User user = new User();
        user.setName("neo");
        user.setPass("123456");

        sender.send(user);
    }
}
```

测试结果

![02object.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/02object.png)

# 5.Fanout

Fanout 就是我们熟悉的广播模式或者订阅模式，给 Fanout 交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。

分别声明 `fanout.A`、`fanout.B`、`fanout.C` 三个队列，声明交换器 `fanoutExchange` ，然后将队列绑定到交换器。

```java
@Configuration
public class FanoutRabbitConfig {

    @Bean
    public Queue AMessage(){
        return new Queue("fanout.A");
    }

    @Bean
    public Queue BMessage(){
        return new Queue("fanout.B");
    }

    @Bean
    public Queue CMessage(){
        return new Queue("fanout.C");
    }

    @Bean
    public FanoutExchange fanoutMessage(){
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(BMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(CMessage).to(fanoutExchange);
    }

}
```

发送者

```java
public class FanoutSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(){
        String context = "hi, fanout msg";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("fanoutExchange","",context);
    }
}
```

接收者A：

```java
@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {
    @RabbitHandler
    public void process(String message){
        System.out.println("fanout Receiver A: " + message);
    }
}
```

接收者B：

```java
@Component
@RabbitListener(queues = "fanout.B")
public class FanoutReceiverB {
    @RabbitHandler
    public void process(String message){
        System.out.println("fanout Receiver B: " + message);
    }
}
```

测试：

```java
@SpringBootTest
public class FanoutTest {
    @Autowired
    private FanoutSender fanoutSender;

    @Test
    public void fanoutSender() throws Exception{
        fanoutSender.send();
    }
}
```

测试结果：

![03fanout.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/03fanout.png)

# 6.Topic

```java
@Configuration
public class TopicRabbitConfig {
    final static String message = "topic.message";
    final static String messages = "topic.messages";

    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }
}
```

发送者

```java
@Component
public class TopicSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void Send(){
        String context = "hi, i am message all";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange","topic.1",context);
    }

    public void Send1(){
        String context = "hi, i am message 1";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange","topic.message",context);
    }

    public void Send2(){
        String context = "hi, i am message all";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange","topic.messages",context);
    }
}
```

接收者1

```java
@Component
@RabbitListener(queues = "topic.message")
public class TopicReceiver1 {

    @RabbitHandler
    public void process(String message){
        System.out.println("Topic Receiver1 : " + message);
    }
}
```

接收者2

```java
@Component
@RabbitListener(queues = "topic.messages")
public class TopicReceiver2 {

    @RabbitHandler
    public void process(String message){
        System.out.println("Topic Receiver2 : " + message);
    }
}
```

测试

```java
@Test
public void topic() throws Exception{
    sender.Send();
}
```

![04topic_1.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/04topic_1.png)

```java
@Test
public void topic1() throws Exception{
    sender.Send1();
}
```

![04topic_2.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/04topic_2.png)

```java
@Test
public void topic2() throws Exception{
	sender.Send2();
}
```

![04topic_3.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/04topic_3.png)



# 参考

https://www.cnblogs.com/ityouknow/p/6120544.html

https://github.com/ityouknow/spring-boot-examples/blob/master/spring-boot-rabbitmq/src/main/java/com/neo/rabbit/RabbitConfig.java