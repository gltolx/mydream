package com.lin.mydream.service;

import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/17.
 * @desc 消息接收路由分发模板
 */
@Component
public class RouterReplierTemplate implements InitializingBean {

    @Autowired
    private RobotService robotService;
    /**
     * command -> Function{Command, bizReply}
     */
    private Map<String, Function<Command, String>> router = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //                            操作机器人
        ////////////////////////////////////////////////////////////////////////
        // 创建机器人 ｜ 1.获取一个outgoingToken ｜ acquire token
        router.put("acquire token", command -> CommonUtil.format(
                "your token is: {}, the token is valid for half an hour, please create robot as soon as possible."
                , robotService.getTokenOnPreCreate()));
        router.put("acquire outgoing token", router.get("acquire token"));

        // 创建机器人 ｜ 2.创建 ｜ create robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN]
        router.put("create robot", command -> {
            CreateRobotParam p = CreateRobotParam.of(command);
            Long newRobotId = robotService.createByConfirmingToken(p);
            CommonUtil.format("create success, SEQUENCE ID:[{}], enjoy it.");
            return String.valueOf(newRobotId);
        });

        // 删除机器人 ｜ delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN]
        router.put("delete robot", command -> {
            if (!robotService.deleteByAccessToken(command.getBody())) {
                robotService.deleteByOutgoingToken(command.getBody());
            }
            return CommonUtil.format("delete success, bye~");
        });

        /////////////////////////////////////////////////////////////////////////
        //                            私人定制
        ////////////////////////////////////////////////////////////////////////

//        router

    }

    /**
     * 接收消息->逻辑处理->回复自定消息模板 (主要是管理者机器人操作时回复)
     *
     * @param inputContent 用户键入的信息
     */
    public void execute(String outgoingToken, String inputContent) {
        Assert.isTrue(StringUtils.isNotBlank(outgoingToken), "outgoingToken is empty.");

        Robotx robotx = ReceivedRobotHolder.pick(outgoingToken);
        if (robotx == null) {
            throw MydreamException.of("无法识别该token:{}", outgoingToken);
        }

        try {
            Pair<String, String> pair = CommonUtil.parseCommand(inputContent);
            Function<Command, String> fun = router.get(pair.getLeft());
            if (fun == null) {
                throw new MydreamException("无法识别该命令:{}", pair.getLeft());
            }
            Command command = Command.builder()
                    .ogt(outgoingToken)
                    .head(pair.getLeft())
                    .body(pair.getRight())
                    .build();
            // 执行调用逻辑，返回消息
            String finalReply = fun.apply(command);
            robotx.send(finalReply);
        } catch (Exception e) {
            robotx.send(e.getMessage());
        }

    }

}
