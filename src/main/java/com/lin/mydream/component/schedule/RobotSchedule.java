package com.lin.mydream.component.schedule;

import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.manager.RobotManager;
import com.lin.mydream.model.Remember;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.RememberService;
import com.lin.mydream.service.dto.TextDingDTO;
import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
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
    @Autowired
    private RememberService rememberService;

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

//    @Scheduled(cron = "0 0 21 * * ?")
//    public void notifyEnjoyingLife3() {
//        this.travelAll(robotx -> robotx.send(TextDingDTO.normal("某些人，🙏真的球球你别再卷了")));
//    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void notifyEnjoyingLife4() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.normal("就在这一瞬间，你累了。也倦了。")));
    }

    @Scheduled(cron = "0 30 9 * * ?")
    public void remember() {

        // 21天、99天、180天、n周年
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date now = calendar.getTime();

        Date now_21 = DateUtils.addDays(now, -21);
        Date now_99 = DateUtils.addDays(now, -99);
        Date now_180 = DateUtils.addDays(now, -180);
        List<Date> dates = new ArrayList<>();
        dates.add(now_21);
        dates.add(now_99);
        dates.add(now_180);
        for (int i = 1; i <= 10; i++) { // 10周年以内
            dates.add(DateUtils.addYears(now, -i));
        }
        List<Remember> remembers = rememberService.findByDatesIn(dates);
        Map<Long, Robotx> robotxMap = ReceivedRobotHolder.robotIdMap();
        remembers.forEach(x->{
                    Robotx robotx = robotxMap.get(x.getRobotId());
                    if (robotx == null) {
                        return;
                    }
                    long days = CommonUtil.getDistanceOfTwoDate(x.getRememberTime(), now);
                    String diffTime = CommonUtil.transferDays(days);
                    String text = CommonUtil.format("亲爱的，^_^今天是{}{}的日子。回首山河已是秋，再看山河复长流。去发现，去沉淀，去纪念，去写一封信@未来的自己吧～", x.getName(), diffTime);
                    robotx.sendAt(text, x.getReceiver());
                });
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
