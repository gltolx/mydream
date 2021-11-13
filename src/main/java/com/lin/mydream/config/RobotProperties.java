package com.lin.mydream.config;

import com.lin.mydream.model.Robot;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/4.
 * @desc 机器人配置信息
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "robot")
public class RobotProperties implements InitializingBean {
    /**
     * 钉钉机器人String配置
     */
    private List<String> ding;

    /**
     * 钉钉机器人模型数组
     */
    private List<Robot> dingRobots;

    @Override
    public void afterPropertiesSet() throws Exception {
        dingRobots = Robot.transferByConfigs(getDing());
    }
}
