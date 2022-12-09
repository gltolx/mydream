package com.lin.mydream.service;

import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.consts.Mydreams;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.manager.DingPhoneRelManager;
import com.lin.mydream.manager.RobotManager;
import com.lin.mydream.model.Robot;
import com.lin.mydream.model.base.BaseModel;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.Reply;
import com.lin.mydream.util.CommonUtil;
import com.lin.mydream.util.LogUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private DingPhoneRelManager dingPhoneRelManager;

    /**
     * 生成一个10位的outgoing token
     */
    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    public String getIpAddress() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            LogUtil.error("获取本机地址异常", e);
            return "120.26.126.100";
        }
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
                "您的token是：{}, 接下来,\n1. 在机器人管理中绑定POST地址：http://{}:8011/md/api/v1/ding/channel?token={}，绑定Token：{}\n2.安全设置勾选为加签，分别复制Webhook中的[ACCESS_TOKEN]和加签密钥[SIGN]备用\n2. 再使用命令创建一个机器人：\ncreate robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN]"
                , outgoingToken, getIpAddress(), outgoingToken, outgoingToken));
    }

    /**
     * create robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN];
     * 根据之前回传的token识别并创建机器人
     *
     * @return 机器人id
     */
    @Transactional(rollbackFor = Exception.class)
    public Robot createByConfirmingToken(CreateRobotParam param) {
        Long robotId = Optional
                .ofNullable(robotManager.findByOutgoingToken(param.getOutgoingToken()))
                .map(BaseModel::getId)
                .orElseThrow(() -> MydreamException.of("无法识别该机器人[{}]", param.getOutgoingToken()));
        robotManager.initRobot(param, robotId);
        Robot r = robotManager.getById(robotId);
        return r;
    }

    public Robot createRobot(Command command) {
        CreateRobotParam p = CreateRobotParam.of(command);
        return this.createByConfirmingToken(p);
    }


    public Reply deleteRobot(Command command) {
        // 检查是否有管理员权限
        this.checkAdminPermission(command);

        String token = command.getBody();
        Robot r = robotManager.findByOutgoingToken(token);
        if (r == null) {
            r = robotManager.findByAccessToken(token);
        }
        if (r == null) {
            return Reply.of(CommonUtil.format("不存在该机器人[], 删除失败", token));
        }
        String ogt = r.getOutgoingToken();
        this.deleteByOutgoingToken(ogt);
        ReceivedRobotHolder.remove(ogt);
        return Reply.of(CommonUtil.format("已为您成功删除机器人, bye~"));
    }

    /**
     * delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN];
     */
    private boolean deleteByOutgoingToken(String outgoingToken) {
        return robotManager.deleteByOutgoingToken(outgoingToken);
    }

    public Reply active(Command command) {
        // 检查是否有管理员权限
        this.checkAdminPermission(command);

        String senderNick = command.getMsgContext().getSenderNick();
        String outgoingToken = command.getBody();
        if (StringUtils.isBlank(outgoingToken)) {
            return Reply.of(CommonUtil.format("告{}阁下：键入的OUTGOING_TOKEN为空", senderNick));
        }
        Robot r = robotManager.findByOutgoingToken(outgoingToken);
        if (r == null) {
            return Reply.of(CommonUtil.format("告{}阁下：查询无该机器人（OUTGOING_TOKEN={}）", senderNick, outgoingToken));
        }
        if (r.isValid()) {
            return Reply.of(CommonUtil.format("告{}阁下：该机器人（OUTGOING_TOKEN={}）已经被激活，激活时间：{}", senderNick, outgoingToken, DateFormatUtils.format(r.getUpdateTime(), Mydreams.Y_M_D_H_M_S)));
        }
        // 设置为正常激活
        r.setStat(RobotEnum.Stat.valid.code());
        robotManager.updateById(r);
        // 核实通过后，在pussyPicker里put一个机器人
        ReceivedRobotHolder.put(r);
        ReceivedRobotHolder
                .pick(outgoingToken)
                .sendMd(CommonUtil.format("我已被激活，编号[{}]，随时听令。\n > __可使用help命令查看帮助__", outgoingToken), Boolean.TRUE);

        return Reply.of(CommonUtil.format("告{}阁下：已激活该机器人[{}]", senderNick, outgoingToken));
    }

    /**
     * 查询待激活机器人
     */
    public Reply listActive(Command command) {
        // 检查是否有管理员权限
        this.checkAdminPermission(command);
        List<Robot> invalids = robotManager.findInvalidRobots();
        String content = "### 等待激活，以下:\n" + invalids.stream()
                .map(Robot::toSimpleString)
                .collect(Collectors.joining("\n"));
        return Reply.of(content);
    }

    /**
     * 检查是否有管理员权限
     */
    public void checkAdminPermission(Command command) {
        String senderId = command.getMsgContext().getSenderId();
        String phone = dingPhoneRelManager.phone(senderId);
        String senderNick = command.getMsgContext().getSenderNick();
        if (StringUtils.isBlank(phone)) {
            throw MydreamException.of("当前激活操作人[]未注册手机号，请@管理员机器人注册手机号: \nregister phone - '13612345678'", senderNick);
        }
        if (!ArrayUtils.contains(Mydreams.ADMIN_PHONES, phone)) {
            throw MydreamException.of("抱歉，阁下[{},{}]还不是管理员，无法操作", senderNick, phone);
        }
    }
}
