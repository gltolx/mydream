package com.lin.mydream.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Splitter;
import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.manager.DingPhoneRelManager;
import com.lin.mydream.manager.RememberManager;
import com.lin.mydream.model.Remember;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/20.
 * @desc Remember Servicec层
 */
@Service
public class RememberService {

    @Autowired
    private RememberManager rememberManager;
    @Autowired
    private DingPhoneRelManager dingPhoneRelManager;

    /**
     * wake up remembers;
     * 唤醒记忆
     */
    public String wakeupRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());
        List<Remember> remembers = rememberManager.listByRobotId(robotId);
        Date now = new Date();
        StringBuilder sb = new StringBuilder("##### 因为记忆，爱才弥足珍贵 —— ");
        remembers.forEach(x -> {
            long days = CommonUtil.getDistanceOfTwoDate(x.getRememberTime(), now);
            String diffTime = CommonUtil.transferDays(days);

            // TODO 未来记忆的文案
            sb.append(CommonUtil.format(days > 0 ? "\n> 距{}已经{}了" : "\n> 距{}还剩{}", x.getName(), diffTime));
        });

        return sb.toString();
    }

    /**
     * list remembers;
     * 列出所有记忆
     */
    public String listRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());

        return CommonUtil.orEmpty(() -> rememberManager.listByRobotId(robotId))
                .stream()
                .map(Remember::toSimpleString)
                .collect(Collectors.joining("\n"));

    }

    /**
     * delete remember - like 'love';
     * 删除一个记忆
     */
    public boolean deleteRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());

        List<String> list = command.extractKeysFromBody();
        if (CollectionUtils.isEmpty(list)) {
            throw MydreamException.of("command invalid [{}], please input like 'delete remember - like \"love\"'", command.body());
        }

        return rememberManager.deleteLike(robotId, list.get(list.size() - 1));
    }

    /**
     * create loop notify - 'publish task' '10/5' '17826833386'
     * 创建循环提醒 - 'publish task' '每隔10分钟/提醒5次' '@对象17826833386'
     */
    public boolean createLoopNotify(Command command) {
        List<String> bodies = command.getBodies();
        CommonUtil.asserts(bodies.size(), size -> size <= 3, "invalid command [{}], please complete it like ```create loop notify - 'publish task' '10/5' '178xxxx3386'```", command.body());
        String control = bodies.get(1);

        List<String> ctrls = Splitter.on("/").omitEmptyStrings().trimResults().splitToList(control);
        CommonUtil.asserts(ctrls.size(), s -> s == 2);
        int min = CommonUtil.tryParseInteger(ctrls.get(0));
        int cnt = CommonUtil.tryParseInteger(ctrls.get(1));
        CommonUtil.asserts(min != 0 && cnt != 0, control);
        Date now = new Date();
        List<Remember> notifies = new LinkedList<>();
        for (int i = 1; i <= cnt; i++) {
            Date theTime = DateUtils.addMinutes(now, min * i);

            Remember remember = new Remember()
                    .setRobotId(ReceivedRobotHolder.id(command.ogt()))
                    .setType(RobotEnum.RememberType.notify.code())
                    .setName(bodies.get(0) + "_" + i)
                    .setReceiver(this.obtainReceiver(command))
                    .setRememberTime(theTime);
            notifies.add(remember);
        }
        if (!notifies.isEmpty()) {
            rememberManager.saveBatch(notifies);
        }
        return true;

    }


    /**
     * create remember/notify - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
     * <p>
     * 创建记忆
     */
    public boolean createRemember(Command command) {
        List<String> bodies = command.getBodies();
        CommonUtil.asserts(bodies.size(), s-> s< 1, "invalid command [{}], maybe should complete the remember name or time like [create remember - 'xxx' '2021-02-14 10:00:00']", command.body());
        CommonUtil.asserts(bodies.size(), s-> s> 3, "invalid command [{}], only three parameters can be received", command.body());

        RobotEnum.RememberType rememberType = RobotEnum.RememberType.judge(command.head());
        Date rememberTime = null;
        String theDate = bodies.get(1);
        try {
            rememberTime = DateUtils.parseDate(theDate, theDate.length() > 10 ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd");
        } catch (ParseException e) {
            throw MydreamException.of("date parse failed. input:{}", theDate);
        }

        Remember remember = new Remember()
                .setRobotId(command.getRobotId())
                .setType(rememberType.code())
                .setName(bodies.get(0))
                .setRememberTime(rememberTime);
        remember.setReceiver(this.obtainReceiver(command));

        return rememberManager.save(remember);
    }


    /**
     * 获取消息/记忆/提醒的接收者手机号
     */
    private String obtainReceiver(Command command) {
        String receiver;
        if (command.getBodies().size() == 3) {
            receiver = command.getBodies().get(2);
        } else {
            String senderId = command.getMsgContext().getSenderId();
            receiver = dingPhoneRelManager.phone(senderId);
        }
        return receiver;
    }

    public List<Remember> findNotifiesByDatesRange(Date begin, Date end) {

        return findByDatesRange(begin, end, RobotEnum.RememberType.notify);
    }

    public List<Remember> findRemembersByDatesIn(List<Date> dates) {

        return findByDatesIn(dates, RobotEnum.RememberType.remember);
    }


    public List<Remember> findByDatesIn(List<Date> dates, RobotEnum.RememberType rType) {

        return CommonUtil.orEmpty(() ->
                rememberManager.list(Wrappers.<Remember>query()
                        .in("remember_time", dates)
                        .eq("remember_type", rType.code())
                )
        );
    }

    public List<Remember> findByDatesRange(Date begin, Date end, RobotEnum.RememberType rType) {

        return CommonUtil.orEmpty(() ->
                rememberManager.list(Wrappers.<Remember>query()
                        .ge("remember_time", begin)
                        .le("remember_time", end)
                        .eq("remember_type", rType.code())
                        .eq("is_notified", false)
                )
        );
    }

    public boolean notified(Collection<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        return rememberManager.update(Wrappers.<Remember>update().set("is_notified", true).in("id", ids));
    }
}


