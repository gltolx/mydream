package com.lin.mydream.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Splitter;
import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.manager.DingPhoneRelManager;
import com.lin.mydream.manager.RememberManager;
import com.lin.mydream.model.Remember;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.Reply;
import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;
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
    public Reply wakeupRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());
        List<Remember> remembers = rememberManager.listByRobotId(robotId, RobotEnum.RememberType.remember.code());
        Date now = new Date();
        StringBuilder sb = new StringBuilder("### " + Mydreams.getZzs());
        remembers.forEach(x -> {
            long days = CommonUtil.getDistanceOfTwoDate(x.getRememberTime(), now);
            String diffTime = CommonUtil.transferDays(days);

            sb.append(CommonUtil.format(days > 0 ? "\n- #### 距{}已经{}了" : "\n- #### 距{}还剩{}", x.getName(), diffTime));
        });

        return Reply.of(sb.toString(), RobotEnum.MsgType.markdown)
                .setMdTitle("wakeup remembers");
    }

    /**
     * list remembers;
     * 列出所有记忆/提醒
     */
    public String selectListBy(Command command, RobotEnum.RememberType rememberType) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());

        return CommonUtil
                .orEmpty(() -> rememberManager.listByRobotId(robotId, rememberType.code()))
                .stream()
                .map(Remember::toSimpleString)
                .collect(Collectors.joining("\n"));

    }

    public Reply listRemembers(Command command, RobotEnum.RememberType rememberType) {
        String rememberString = this.selectListBy(command, rememberType);
        if (StringUtils.isBlank(rememberString)) {
            return Reply.of("there is empty");
        }
        return Reply.of(
                CommonUtil.format("there are follows:\n{}", rememberString)
        );
    }

    /**
     * delete remember - like 'love';
     * 删除一个记忆
     */
    public Reply deleteRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());

        List<String> list = command.extractKeysFromBody();
        if (CollectionUtils.isEmpty(list)) {
            throw MydreamException.of("command invalid [{}], please input like 'delete remember - like \"love\"'", command.body());
        }

        rememberManager.deleteLike(robotId, list.get(list.size() - 1));
        return Reply.of("已为您成功删去这段记忆和日历");
    }

    /**
     * create loop notify - 'publish task' '10/5' '17826833386'
     * 创建循环提醒 - 'publish task' '每隔10分钟/提醒5次' '@对象17826833386'
     */
    public Reply createLoopNotify(Command command) {
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
                    .setRememberTime(theTime)
                    .setRemTimeStr(DateFormatUtils.format(theTime, Mydreams.Y_M_D));
            notifies.add(remember);
        }
        if (!notifies.isEmpty()) {
            rememberManager.saveBatch(notifies);
        }
        return Reply.of(CommonUtil.format("已经为阁下成功创建循环提醒[{}]，每隔{}分钟，共提醒{}次。敬请留意。", bodies.get(0), min, cnt));

    }


    /**
     * create remember/notify - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
     * <p>
     * 创建记忆
     */
    public Reply createRemember(Command command, RobotEnum.RememberType rememberType) {
        List<String> bodies = command.getBodies();
        CommonUtil.negative(bodies.size(), s -> s < 1, "invalid command [{}], maybe should complete the remember name or time like [create remember - 'xxx' '2021-02-14 10:00:00']", command.body());
        CommonUtil.negative(bodies.size(), s -> s > 3, "invalid command [{}], only three parameters can be received", command.body());

//        RobotEnum.RememberType rememberType = RobotEnum.RememberType.judge(command.head());
        Date rememberTime = null;
        String theDate = bodies.get(1);
        try {
            rememberTime = DateUtils.parseDate(theDate, theDate.length() > 10 ? Mydreams.Y_M_D_H_M_S : Mydreams.Y_M_D);
        } catch (ParseException e) {
            throw MydreamException.of("date parse failed. input:{}", theDate);
        }

        Remember remember = new Remember()
                .setRobotId(command.getRobotId())
                .setType(rememberType.code())
                .setName(bodies.get(0))
                .setRememberTime(rememberTime)
                .setRemTimeStr(DateFormatUtils.format(rememberTime, Mydreams.Y_M_D));
        remember.setReceiver(this.obtainReceiver(command));

        rememberManager.save(remember);
        return Reply.of(RobotEnum.RememberType.remember.equals(rememberType) ? "已成功为您种下一个记忆，期待在未来的土壤中生根发芽。" : "已成功为您设置一个提醒日");
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

    public List<Remember> findAllRemembersByDatesIn(List<Date> dates) {
        if (CollectionUtils.isEmpty(dates)) {
            return Collections.emptyList();
        }
        List<String> dateStrList = dates.stream().map(x -> DateFormatUtils.format(x, Mydreams.Y_M_D)).collect(Collectors.toList());
        return findByDatesIn(dateStrList, RobotEnum.RememberType.remember);
    }


    public List<Remember> findByDatesIn(List<String> dates, RobotEnum.RememberType rType) {

        return CommonUtil.orEmpty(() ->
                rememberManager.list(Wrappers.<Remember>query()
                        .in("rem_time_str", dates)
                        .eq("remember_type", rType.code())
                )
        );
    }

    public List<Remember> findByDatesIn(List<String> dates) {

        return CommonUtil.orEmpty(() ->
                rememberManager.list(Wrappers.<Remember>query().in("rem_time_str", dates))
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


