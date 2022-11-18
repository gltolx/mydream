package com.lin.mydream.service;

import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.manager.RobotManager;
import com.lin.mydream.model.Robot;
import com.lin.mydream.model.base.BaseModel;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.Reply;
import com.lin.mydream.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc robot service层
 */
@Service
public class RobotService {
    @Autowired
    private RobotManager robotManager;

    /**
     * 生成一个10位的outgoing token
     */
    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    /**
     * acquire token;
     * 喊话机器人预创建
     *
     * @return 回传一个outgoing token
     */
    public Reply getTokenOnPreCreate() {
        String outgoingToken = generateToken();
        robotManager.save(Robot.preCreate(outgoingToken));

        return Reply.of(CommonUtil.format(
                "your token is: {}, the token is valid for half an hour, please create robot as soon as possible."
                , outgoingToken));
    }

    /**
     * create robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN];
     * 根据之前回传的token识别并创建机器人
     *
     * @return 机器人id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createByConfirmingToken(CreateRobotParam param) {
        Long robotId = Optional
                .ofNullable(robotManager.findByOutgoingToken(param.getOutgoingToken()))
                .map(BaseModel::getId)
                .orElseThrow(() -> MydreamException.of("无法识别该机器人[{}]", param.getOutgoingToken()));
        robotManager.initRobot(param, robotId);
        // 在pussyPicker里put一个机器人
        ReceivedRobotHolder.put(robotManager.getById(robotId));
        return robotId;
    }

    public Reply createRobot(Command command) {
        CreateRobotParam p = CreateRobotParam.of(command);
        Long newRobotId = this.createByConfirmingToken(p);
        CommonUtil.format("create success, SEQUENCE ID:[{}], enjoy it.");
        return Reply.of(CommonUtil.format("create success. robot id:{}", String.valueOf(newRobotId)));
    }


    public Reply deleteRobot(Command command) {
        if (!this.deleteByAccessToken(command.getBody())) {
            this.deleteByOutgoingToken(command.getBody());
        }
        return Reply.of(CommonUtil.format("delete success, bye~"));
    }

    /**
     * delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN];
     */
    public boolean deleteByOutgoingToken(String outgoingToken) {
        return robotManager.deleteByOutgoingToken(outgoingToken);
    }

    /**
     * delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN];
     */
    public boolean deleteByAccessToken(String accessToken) {
        return robotManager.deleteByAccessToken(accessToken);
    }


}
