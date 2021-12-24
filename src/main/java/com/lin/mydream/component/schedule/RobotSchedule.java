package com.lin.mydream.component.schedule;

import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.manager.RobotManager;
import com.lin.mydream.model.Remember;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.RememberService;
import com.lin.mydream.service.dto.MarkdownDingDTO;
import com.lin.mydream.service.dto.TextDingDTO;
import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    @Scheduled(cron = "0 05 10 ? * MON-FRI")
    public void notifyEnjoyingWork() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.atAll("上班啦，专注一下，早点下班！！^-^")));
    }

    @Scheduled(cron = "0 05 15 ? * MON-FRI")
    public void notifyEnjoyingFishing() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.normal("喝杯茶，解个手，舒缓一下吧")));
    }

    @Scheduled(cron = "0 05 18 ? * MON-THU")
    public void notifyEnjoyingLifeMon2Thu() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.atAll("别卷了，下班吧，少年！！")));
    }

    @Scheduled(cron = "0 05 18 ? * FRI")
    public void notifyEnjoyingLifeFri() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.atAll("下班了 ———— ta喜欢你，你喜欢这世界，世界只喜欢今天，因为～今天是周五。")));
    }

    @Scheduled(cron = "0 55 19 ? * MON-FRI")
    public void notifyEnjoyingLife2() {
        this.travelAll(robotx -> robotx.send(TextDingDTO.atAll("你不卷我不卷，生活处处是笑脸")));
    }


//    @Scheduled(cron = "0 0 22 ? * MON-FRI")
//    public void notifyEnjoyingLife4() {
//        this.travelAll(robotx -> robotx.send(TextDingDTO.normal("就在这一瞬间，你累了，也倦了")));
//    }

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
        Date _now_1 = DateUtils.addDays(now, 1);
        Date _now_3 = DateUtils.addDays(now, 3);
        Date _now_10 = DateUtils.addDays(now, 10);
        Date _now_15 = DateUtils.addDays(now, 15);
        Date _now_50 = DateUtils.addDays(now, 50);
        Date _now_99 = DateUtils.addDays(now, 99);
        Date _now_199 = DateUtils.addDays(now, 199);
        Date _now_365 = DateUtils.addDays(now, 365);
        List<Date> dates = new ArrayList<>();
        dates.add(now_21);
        dates.add(now_99);
        dates.add(now_180);
        dates.add(_now_1);
        dates.add(_now_3);
        dates.add(_now_10);
        dates.add(_now_15);
        dates.add(_now_50);
        dates.add(_now_99);
        dates.add(_now_199);
        dates.add(_now_365);
        for (int i = 1; i <= 10; i++) { // 10周年以内
            dates.add(DateUtils.addYears(now, -i));
        }
        List<Remember> remembers = rememberService.findByDatesIn(dates);
        if (CollectionUtils.isEmpty(remembers)) {
            return;
        }
        Map<Long, Robotx> robotxMap = ReceivedRobotHolder.robotIdMap();

        Map<Long, List<Remember>> robotRemMap = remembers.stream()
                .collect(Collectors.groupingBy(Remember::getRobotId));

        robotRemMap.forEach((robotId, rems) -> {
            Robotx robotx = robotxMap.get(robotId);
            if (robotx == null) {
                return;
            }
            StringBuilder text = new StringBuilder("#### 回首山河已是秋，再落风花月对酒\n> 亲爱的，");
            rems.sort(Comparator.comparing(Remember::getRememberTime));

            rems.forEach(x -> {
                long days = CommonUtil.getDistanceOfTwoDate(x.getRememberTime(), now);
                String diffTime = CommonUtil.transferDays(days);

                text.append(CommonUtil.format(days > 0 ? "\n> 今天是{}{}的日子" : "\n> 距{}还剩{}", x.getName(), diffTime));
            });
            String allReceiver = rems.stream().map(Remember::getReceiver).collect(Collectors.joining(","));
            text.append("\n> __去发现，去纪念，去写一封信给未来的自己吧～__");
            MarkdownDingDTO markdownMsg = MarkdownDingDTO.builder()
                    .title("记忆唤醒").markdownText(text.toString()).atAll(false).atMobiles(allReceiver).build();
            robotx.send(markdownMsg);
        });
    }

    /**
     * 遍历所有的机器人并消费
     */
    public void travelAll(Consumer<Robotx> consumer) {
        robotManager
                .findValidRobots()
                .stream()
                .map(Robotx::new)
                .forEach(consumer);
    }

}
