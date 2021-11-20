package com.lin.mydream;

import com.lin.mydream.service.ReceivedRobotHolder;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.dto.TextDingDTO;
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
        Robotx robotx = ReceivedRobotHolder.pick("e49f5b");
        robotx.send(TextDingDTO.builder().content("苍南高中").mobiles("17826833386").build());
    }
}
