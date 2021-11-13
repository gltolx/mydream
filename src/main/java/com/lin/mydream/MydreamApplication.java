package com.lin.mydream;

import com.lin.mydream.holder.ReceivedRobotHolder;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.dto.TextDingDTO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lin.mydream.mapper")
@SpringBootApplication
public class MydreamApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MydreamApplication.class, args);
    }

    @Autowired
    private ReceivedRobotHolder receivedRobotHolder;
    @Override
    public void run(String... args) throws Exception {
        Robotx robotx = receivedRobotHolder.get("e49f5b");
        robotx.sendText(TextDingDTO.builder().content("苍南高中").mobiles("17826833386").build());
    }
}
