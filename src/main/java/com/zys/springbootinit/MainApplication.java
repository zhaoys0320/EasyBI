package com.zys.springbootinit;

import com.zys.springbootinit.bizmq.BiInitMain;
import com.zys.springbootinit.bizmq.MqInitMain;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.zys.springbootinit.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        BiInitMain.biQueneInit();
        MqInitMain.codeQueneInit();
        SpringApplication.run(MainApplication.class, args);
    }

}
