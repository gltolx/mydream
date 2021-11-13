package com.lin.mydream.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Splitter;
import com.lin.mydream.model.base.BaseModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/4.
 * @desc 机器人模型
 */
@Slf4j
@Data
@TableName("my_robot")
public class Robot extends BaseModel {
    /**
     * 机器人名字
     * (由钉钉提供，和钉钉机器人名字保持一致)
     */
    @TableField("robot_name")
    private String name;
    /**
     * AccessToken
     * (由钉钉提供)
     */
    @TableField("access_token")
    private String accessToken;
    /**
     * 验签
     * (由钉钉提供)
     */
    @TableField("robot_sign")
    private String sign;
    /**
     * 是否开启outgoing机制
     * (由钉钉提供)
     */
    @TableField("outgoing_enable")
    private boolean outgoingEnable;
    /**
     * outgoing机制的token
     * (由MyDream提供和识别, 填写到钉钉机器人中)
     */
    @TableField("outgoing_token")
    private String outgoingToken;
    /**
     * 是否是管理者机器人
     */
    @TableField("is_admin")
    private boolean admin;

    /**
     * 机器人状态 1正常 0失效
     * @see com.lin.mydream.model.enumerate.RobotEnum.Stat
     */
    @TableField("robot_stat")
    private Integer stat;


    public static Robot transferByConfig(String config) {
        Objects.requireNonNull(config);

        List<String> list = Splitter.on(",").splitToList(config);
        if (list.size() < 4) {
            return null;
        }
        Robot robot = new Robot();
        robot.setName(list.get(0));
        robot.setAccessToken(list.get(1));
        robot.setSign(list.get(2));
        robot.setOutgoingEnable("true".equals(list.get(3)) || "1".equals(list.get(3)));
        return robot;
    }

    public static List<Robot> transferByConfigs(List<String> ding) {
        List<Robot> robots = Optional.ofNullable(ding)
                .map(x -> x.stream()
                        .map(Robot::transferByConfig)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        log.info("==> init robot, input ding:{}, output robots:{}", ding, robots);
        return robots;
    }
}
