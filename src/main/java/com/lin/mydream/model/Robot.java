package com.lin.mydream.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Splitter;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.model.base.BaseModel;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.util.CommonUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;
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
@Accessors(chain = true)
@TableName("my_robot")
public class Robot extends BaseModel {
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
     * outgoing机制的token（MyDream的唯一码）
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
     * 机器人状态 1正常 0初始化 -1失效
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
//        robot.setName(list.get(0));
        robot.setAccessToken(list.get(0));
        robot.setSign(list.get(1));
        robot.setOutgoingEnable(true);
        robot.setOutgoingToken(list.get(2));
        robot.setAdmin(BooleanUtils.toBoolean(list.get(3)));
        robot.setStat(RobotEnum.Stat.valid.getCode());
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

    /**
     * 是否支持outgoing机制
     */
    public boolean ifOutgoing() {
        return isOutgoingEnable() && StringUtils.isNotEmpty(getOutgoingToken());
    }

    /**
     * 是否为正常（已激活）的状态
     */
    public boolean isValid() {
        return RobotEnum.Stat.valid.equalsCode(this.getStat());
    }

    public String getStatDesc() {
        RobotEnum.Stat st = RobotEnum.of(getStat(), RobotEnum.Stat.class);
        if (st == null) {
            return "无状态(状态缺失)";
        }
        return st.getName();
    }

    public static Robot preCreate(String token) {
        Robot entity = new Robot();
        entity.setSign("0");
        entity.setAccessToken("0");
        entity.setOutgoingEnable(true);
        // 后续创建识别这个outgoingToken追加信息
        entity.setOutgoingToken(token);
        entity.setStat(RobotEnum.Stat.initial.getCode());
        entity.setUpdateTime(new Date());
//        entity.setName("0");
        return entity;
    }

    public String toSimpleString() {

        return CommonUtil.format("+ SEQUENCE:{}, 创建时间:{}, OUTGOING_TOKEN:**{}**, 状态:{}"
                , getId()
                , DateFormatUtils.format(getCreateTime(), Mydreams.Y_M_D_H_M_S)
                , getOutgoingToken()
                , getStatDesc()
        );
    }
}
