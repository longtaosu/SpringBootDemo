# 1.配置

## 1.1pom文件

```xaml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

## 1.2application.properties

```yaml
# Redis数据库索引（默认为0）
spring.redis.database=0  
# Redis服务器地址
spring.redis.host=127.0.0.1
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器连接密码（默认为空）
spring.redis.password=
# 连接池最大连接数（使用负值表示没有限制） 默认 8
spring.redis.lettuce.pool.max-active=8
# 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
spring.redis.lettuce.pool.max-wait=-1
# 连接池中的最大空闲连接 默认 8
spring.redis.lettuce.pool.max-idle=8
# 连接池中的最小空闲连接 默认 0
spring.redis.lettuce.pool.min-idle=0
```



# 2.测试

## 2.1测试程序

测试功能，获取当前日志并存入Redis，然后查询并打印；

```java
@Component
public class RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    public void test(){
        ValueOperations ops = redisTemplate.opsForValue();
        Object date = new Date();
        ops.set("k1",date);
        System.out.println("Redis存入数据：" + date);
        Object obj = ops.get("k1");
        System.out.println("Redis查询数据：" + obj);
    }
}
```

## 2.2测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void redisTest() throws Exception{
        redisService.test();
    }
}
```

![01_Redis_测试.png](https://gitee.com/imstrive/ImageBed/raw/master/20200103/01_Redis_测试.png)