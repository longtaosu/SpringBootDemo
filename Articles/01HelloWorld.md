# 1.新建项目

File --> New Project 

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\01初始化.png)

Next

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\02项目配置.png)

Next

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\03选择模块.png)

Next

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\04配置目录.png)

项目目录结构如下

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\05目录结构.png)

注，显示方式可以点击设置下的Compact Directories设置。

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\06修改显示.png)

修改后的显示方式如下

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\07修改显示方式.png)

# 2.运行项目

新生成的项目目录结构如上图所示（HelloController除外），添加控制器。代码如下：

```java
package com.example.lts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(){
        return "hello spring_boot_lts";
    }
}
```

点击运行

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\08运行.png)

IDEA提示运行成功

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\09运行.png)

网页测试

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\10运行效果.png)

# 3.POM文件

```xaml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>lts</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>lts</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

## 3.1pom.xml文件

```xml
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.2.2.RELEASE</version>
	<relativePath/> <!-- lookup parent from repository -->
</parent>
```

该部分代码用于配置 Spring Boot的父级依赖。

# 4.应用入口

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\11应用入口.png)

入口程序的代码如下：

```java
package com.example.lts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LtsApplication.class, args);
    }
}
```

Spring Boot 项目通常有一个名为 *Application 的入口类，入口类里有一个 main 方法， **这个 main 方法其实就是一个标准的 Java 应用的入口方法。**

**@SpringBootApplication** 是 Spring Boot 的核心注解，它是一个组合注解，该注解组合了：**@Configuration、@EnableAutoConfiguration、@ComponentScan；** 若不是用 @SpringBootApplication 注解也可以使用这三个注解代替。

# 5.配置文件

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\12配置文件.png)

Spring Boot 使用一个全局的配置文件 application.properties 或 application.yml，放置在【src/main/resources】目录或者类路径的 /config 下。

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\13配置文件.png)

## 5.1使用注解加载配置信息

添加名为 `application.yml` 的配置文件

```yml
student:
  name: i'm java newer
  age: 18
```

添加与配置信息相对应的类文件

```java
package com.example.lts.Properities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "student")
public class StudentProperties {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```

此时的程序目录如下：

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\14添加配置类.png)

修改控制器的相关代码，获取配置信息：

```java
package com.example.lts;

import com.example.lts.Properities.StudentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private StudentProperties studentProperties;

    @RequestMapping("getStudent")
    public String GetStudent(){
        return "my name is :" + studentProperties.getName() + ", age is :" + studentProperties.getAge();
    }
}
```

页面效果如下：

![](F:\03Github\12SpringBootDemo\01HelloWorld\Pics\15配置测试.png)