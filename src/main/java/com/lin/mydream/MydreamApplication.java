package com.lin.mydream;

import com.lin.mydream.component.ReceivedRobotHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.lin.mydream.mapper")
@SpringBootApplication
public class MydreamApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MydreamApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ReceivedRobotHolder
                .pick("bb1c18591e")
                .send("睡醒啦☀️");

    }
}
