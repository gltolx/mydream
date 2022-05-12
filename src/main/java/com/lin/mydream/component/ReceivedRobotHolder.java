package com.lin.mydream.component;

import com.lin.mydream.config.RobotProperties;
import com.lin.mydream.manager.RobotManager;
import com.lin.mydream.model.Robot;
import com.lin.mydream.model.Robotx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
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
     * 投喂机器人消息的分发器（决定选中哪个幸运的小樱桃）
     */
    private static Map<String, Robotx> pussyPicker = new ConcurrentHashMap<>();

    @Autowired
    private RobotProperties robotProperties;
    @Resource
    private RobotManager robotManager;

    /**
     * 根据outgoingToken识别机器人
     */
    public static Robotx pick(String outgoingToken) {
        return pussyPicker.get(outgoingToken);
    }

    public static Long id(String outgoingToken) {
        return pick(outgoingToken).self().getId();
    }

    public static void put(Robot robot) {
        Objects.requireNonNull(robot);

        put(robot.getOutgoingToken(), new Robotx(robot));
    }

    /**
     * 放置一个机器人
     */
    public static void put(String outgoingToken, Robotx robotx) {
        pussyPicker.put(outgoingToken, robotx);
    }

    public static Collection<Robotx> values() {
        return pussyPicker.values();
    }

    public static Map<Long, Robotx> robotIdMap() {
        return ReceivedRobotHolder
                .values()
                .stream()
                .collect(Collectors.toMap(k -> k.self().getId(), v -> v, (old, newly) -> newly));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Robot> robots = null;
        try {
            robots = robotProperties.getDingRobots();
        } catch (Exception e) {
            log.error("loading robots failed from spring properties.", e);
        }
        try {
            robots = robotManager.findValidOutgoingRobots();
        } catch (Throwable t) {
            log.error("loading robots failed from db.", t);
        }
        if (robots == null) {
            return;
        }
        // TODO 其他关联信息

        pussyPicker = robots.stream()
                .filter(Robot::ifOutgoing)
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
