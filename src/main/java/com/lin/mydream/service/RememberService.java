package com.lin.mydream.service;

import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.manager.RememberManager;
import com.lin.mydream.model.Remember;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
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

    /**
     * wake up remembers;
     * 唤醒记忆
     */
    public String wakeupRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());
        List<Remember> remembers = rememberManager.listByRobotId(robotId);
        Date now = new Date();
        StringBuilder sb = new StringBuilder("因为记忆，爱才弥足珍贵 ———— \n");
        remembers.forEach(x-> {
            long days = CommonUtil.getDistanceOfTwoDate(x.getRememberTime(), now);
            String diffTime = CommonUtil.transferDays(days);
            sb.append(CommonUtil.format("> 距{}已经{}了", x.getName(), diffTime));
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

        return rememberManager.deleteLike(robotId, list.get(list.size()));
    }

    /**
     * create remember/notify - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
     * <p>
     * 创建记忆
     */
    public boolean createRemember(Command command) {
        Long robotId = ReceivedRobotHolder.id(command.ogt());

        List<String> list = command.extractKeysFromBody();
        if (list.size() < 2) {
            throw MydreamException.of("invalid command [{}], maybe should complete the remember name or time like [create remember 'xxx' '2021-02-14 10:00:00']", command.body());
        }
        if (list.size() > 3) {
            throw MydreamException.of("invalid command [{}], only three parameters can be received", command.body());
        }


        RobotEnum.RememberType rememberType = RobotEnum.RememberType.judge(command.head());
        Date rememberTime = null;
        try {
            rememberTime = DateUtils.parseDate(list.get(1), "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            throw MydreamException.of("date parse failed. input:{}", list.get(1));
        }

        Remember remember = new Remember()
                .setRobotId(robotId)
                .setType(rememberType.code())
                .setName(list.get(0))
                .setRememberTime(rememberTime);

        if (list.size() == 3) {
            remember.setReceiver(list.get(2));
        }
        return rememberManager.save(remember);
    }

    public List<Remember> findByDatesIn(List<Date> dates) {

        return CommonUtil.orEmpty(() -> rememberManager.lambdaQuery()
                .in(x -> x.getRememberTime(), dates)
                .list());
    }
}


