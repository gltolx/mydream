package com.lin.mydream.manager;

import com.lin.mydream.config.RobotProperties;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.mapper.RobotMapper;
import com.lin.mydream.model.Robot;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/16.
 * @desc Robot Manager层
 */
@Slf4j
@Service
public class RobotManager extends BaseManager<RobotMapper, Robot> {

    @Autowired
    private RobotProperties robotProperties;

    /**
     * 生成一个10位的outgoing token
     */
    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }


    public void initRobot(CreateRobotParam param, Long robotId) {
        this.update(uw()
                .set("access_token", param.getAccessToken())
                .set("robot_sign", param.getSign())
                .set("robot_stat", RobotEnum.Stat.valid.code())
                .set("is_admin", false)
                .set("outgoing_enable", true)
                .set("update_time", new Date())
                .eq("id", robotId)
        );
//        this.lambdaUpdate()
//                .set(Robot::getAccessToken, param.getAccessToken())
//                .set(Robot::getSign, param.getSign())
//                .set(Robot::getStat, RobotEnum.Stat.valid.code())
//                .set(Robot::isAdmin, false)
//                .set(Robot::isOutgoingEnable, true)
//                .set(x -> x.getUpdateTime(), new Date())
//                .eq(x -> x.getId(), robotId)
//                .update();
    }

    /**
     * 根据outgoingToken删除机器人
     * 使用场景：用户根据自己的需要删除正在初始化中的机器人
     */
    public boolean deleteByOutgoingToken(String outgoingToken) {
        return this.update(uw().set("is_deleted", true).eq("outgoing_token", outgoingToken));

//        return this.lambdaUpdate()
//                .set(x -> x.isDeleted(), true)
//                .eq(x -> x.getOutgoingToken(), outgoingToken)
//                .update();
    }

    /**
     * 根据accessToken删除机器人
     * 使用场景: 用户根据自己的需要删除已经添加过的机器人
     */
    public boolean deleteByAccessToken(String accessToken) {
        return this.update(uw().eq("is_deleted", true).eq("access_token", accessToken));
//        return this.lambdaUpdate()
//                .set(x -> x.isDeleted(), true)
//                .eq(x -> x.getAccessToken(), accessToken)
//                .update();
    }

    public Robot findByOutgoingToken(String outgoingToken) {
        Objects.requireNonNull(outgoingToken);
        return this.getOne(qw().eq("outgoing_token", outgoingToken));
//        return this.lambdaQuery()
//                .eq(Robot::getOutgoingToken, outgoingToken)
//                .one();
    }


    /**
     * 捞出所有合法的机器人
     *
     * @return 机器人数组
     */
    public List<Robot> findValidRobots() {
        // 应用配置中的机器人
        List<Robot> robots = new ArrayList<>(robotProperties.getDingRobots());
        try {
            // 从db捞的机器人
            robots.addAll(
                    CommonUtil.orEmpty(
                            () -> this.list(qw().eq("robot_stat", RobotEnum.Stat.valid.code()))
//                            lambdaQuery()
//                                    .eq(Robot::getStat, RobotEnum.Stat.valid.code())
//                                    .list()
                    ));
        } catch (Exception e) {
            log.error("select robots from db error.", e);
        }
        return robots;
    }

    public List<Robot> findValidOutgoingRobots() {
        return CommonUtil.orEmpty(
                () -> this.list(
                        qw().eq("robot_stat", RobotEnum.Stat.valid.code())
                        .eq("outgoing_enable", true)
                        .isNotNull("outgoing_token")
                )

//                        this.lambdaQuery()
//                        .eq(Robot::getStat, RobotEnum.Stat.valid.code())
//                        .eq(Robot::isOutgoingEnable, true)
//                        .isNotNull(Robot::getOutgoingToken)
//                        .list()
        );
    }
}
