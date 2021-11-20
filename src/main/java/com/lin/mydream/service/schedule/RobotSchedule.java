package com.lin.mydream.service.schedule;

import com.lin.mydream.manager.RobotManager;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.RobotService;
import com.lin.mydream.service.dto.TextDingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc 机器人固有的定时任务
 */
@Component
public class RobotSchedule {

    @Autowired
    private RobotManager robotManager;

    @Scheduled(cron = "0 0 10 * * ?")
    public void notifyEnjoyingWork() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.atAll("上班啦，专注一下，早点下班！！^-^")));
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void notifyEnjoyingLife() {
        this.travelAll(robotx -> robotx.sendTwice(TextDingDTO.atAll("别卷了，下班吧，骚年！！")));
    }

    @Scheduled(cron = "0 30 18 * * ?")
    public void notifyEnjoyingLife2() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.atAll("你不卷我不卷，生活处处有笑脸")));
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void notifyEnjoyingLife3() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.normal("某些人，🙏真的球球你别再卷了")));
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void notifyEnjoyingLife4() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.normal("就在这一瞬间，你累了。也倦了。")));
    }

    /**
     * 遍历所有的机器人并消费
     */
    public void travelAll(Consumer<Robotx> consumer) {
        robotManager.findValidRobots()
                .stream()
                .map(Robotx::new)
                .forEach(consumer);
    }

}
