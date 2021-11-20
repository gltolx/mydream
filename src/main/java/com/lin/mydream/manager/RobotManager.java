package com.lin.mydream.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.mapper.RobotMapper;
import com.lin.mydream.model.Robot;
import com.lin.mydream.model.base.BaseModel;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/16.
 * @desc Robot Manager层
 */
@Service
public class RobotManager extends ServiceImpl<RobotMapper, Robot> {
    /**
     * 生成一个10位的outgoing token
     */
    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    /**
     * 喊话机器人预创建
     *
     * @return 回传一个outgoing token
     */
    public String getTokenOnPreCreate() {
        String outgoingToken = generateToken();
        this.save(Robot.preCreate(outgoingToken));
        return outgoingToken;
    }

    /**
     * 根据之前回传的token识别并创建机器人
     *
     * @return 机器人id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createByConfirmingToken(CreateRobotParam param) {
        Long robotId = Optional
                .ofNullable(findByOutgoingToken(param.getOutgoingToken()))
                .map(BaseModel::getId)
                .orElseThrow(() -> MydreamException.of("无法识别该机器人[{}]", param.getOutgoingToken()));
        this.initRobot(param, robotId);
        // 在pussyPicker里put一个机器人
        Robot robot = this.getById(robotId);



        return robotId;
    }

    public void initRobot(CreateRobotParam param, Long robotId) {
        this.lambdaUpdate()
                .set(Robot::getAccessToken, param.getAccessToken())
                .set(Robot::getSign, param.getSign())
                .set(Robot::getStat, RobotEnum.Stat.valid.code())
                .set(Robot::isAdmin, false)
                .set(Robot::isOutgoingEnable, true)
                .set(x -> x.getUpdateTime(), new Date())
                .eq(x -> x.getId(), robotId)
                .update();
    }

    /**
     * 根据outgoingToken删除机器人
     * 使用场景：用户根据自己的需要删除正在初始化中的机器人
     */
    public boolean deleteByOutgoingToken(String outgoingToken) {
        return this.lambdaUpdate()
                .set(x -> x.isDeleted(), true)
                .eq(x -> x.getOutgoingToken(), outgoingToken)
                .update();
    }

    /**
     * 根据accessToken删除机器人
     * 使用场景: 用户根据自己的需要删除已经添加过的机器人
     */
    public boolean deleteByAccessToken(String accessToken) {
        return this.lambdaUpdate()
                .set(x -> x.isDeleted(), true)
                .eq(x -> x.getAccessToken(), accessToken)
                .update();
    }

    public Robot findByOutgoingToken(String outgoingToken) {
        Objects.requireNonNull(outgoingToken);
        return this.lambdaQuery()
                .eq(Robot::getOutgoingToken, outgoingToken)
                .one();
    }


    public List<Robot> findValidRobots() {
        return CommonUtil.orEmpty(
                () -> this.lambdaQuery()
                        .eq(Robot::getStat, RobotEnum.Stat.valid.code())
                        .list()
        );
    }

    public List<Robot> findValidOutgoingRobots() {
        return CommonUtil.orEmpty(
                () -> this.lambdaQuery()
                        .eq(Robot::getStat, RobotEnum.Stat.valid.code())
                        .eq(Robot::isOutgoingEnable, true)
                        .isNotNull(Robot::getOutgoingToken)
                        .list()
        );
    }
}
