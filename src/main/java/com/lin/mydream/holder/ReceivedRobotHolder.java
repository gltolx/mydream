package com.lin.mydream.holder;

import com.lin.mydream.model.Robotx;
import com.lin.mydream.model.Robot;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.service.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/9.
 * @desc 随时准备接收信息的机器人Holder类, 每次启动都加载表和配置中机器人的数据
 */
@Slf4j
@Order()
@Component
public class ReceivedRobotHolder implements InitializingBean {

    /**
     * real outgoing token -> "roobot"
     * 投喂机器人消息的分发器
     */
    private Map<String, Robotx> robotDistributor;

    @Resource
    private RobotService robotService;

    /**
     * 根据outgoingToken识别机器人
     */
    public Robotx get(String outgoingToken) {
        return robotDistributor.get(outgoingToken);
    }

    /**
     * 放置一个机器人
     */
    private void put(String outgoingToken, Robotx robotx) {
        robotDistributor.put(outgoingToken, robotx);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Robot> robots = robotService
                .lambdaQuery()
                .eq(Robot::getStat, RobotEnum.Stat.valid.code())
                .eq(Robot::isOutgoingEnable, true)
                .isNotNull(Robot::getOutgoingToken)
                .list();

        robotDistributor = robots.stream()
                .map(Robotx::new)
                .collect(Collectors.toMap(
                        k -> k.self().getOutgoingToken()
                        , v -> v
                        , (old, newly) -> newly
                        , ConcurrentHashMap::new)
                );
        log.info("ReceivedRobotHolder init >> robots:{}", robots);
    }


}
