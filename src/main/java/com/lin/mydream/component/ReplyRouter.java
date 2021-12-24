package com.lin.mydream.component;

import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.RememberService;
import com.lin.mydream.service.RobotService;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.MarkdownDingDTO;
import com.lin.mydream.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class ReplyRouter implements InitializingBean {

    @Autowired
    private RobotService robotService;
    @Autowired
    private RememberService rememberService;
    /**
     * command -> Function{Command, bizReply}
     */
    private final Map<String, Function<Command, String>> ROUTER_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //                            操作机器人
        ////////////////////////////////////////////////////////////////////////
        // 创建机器人 ｜ 1.获取一个outgoingToken ｜ acquire token
        ROUTER_MAP.put("acquire token", command -> CommonUtil.format(
                "your token is: {}, the token is valid for half an hour, please create robot as soon as possible."
                , robotService.getTokenOnPreCreate()));
        ROUTER_MAP.put("acquire outgoing token", ROUTER_MAP.get("acquire token"));

        // 创建机器人 ｜ 2.创建 ｜ create robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN]
        ROUTER_MAP.put("create robot", command -> {
            CreateRobotParam p = CreateRobotParam.of(command);
            Long newRobotId = robotService.createByConfirmingToken(p);
            CommonUtil.format("create success, SEQUENCE ID:[{}], enjoy it.");
            return String.valueOf(newRobotId);
        });

        // 删除机器人 ｜ delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN]
        ROUTER_MAP.put("delete robot", command -> {
            if (!robotService.deleteByAccessToken(command.getBody())) {
                robotService.deleteByOutgoingToken(command.getBody());
            }
            return CommonUtil.format("delete success, bye~");
        });

        /////////////////////////////////////////////////////////////////////////
        //                            私人定制
        ////////////////////////////////////////////////////////////////////////
        // ———————————————————— 记忆 ———————————————————————
        // 列出所有记忆 ｜ list remembers
        ROUTER_MAP.put("list remembers", command -> {
            String rememberString = rememberService.listRemember(command);
            if (StringUtils.isBlank(rememberString)) {
                return "your remembers are empty";
            }
            return CommonUtil.format("your remembers are follows:\n{}", rememberString);
        });
        // 创建记忆 | create remember/notify - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
        ROUTER_MAP.put("create remember", command -> {
            rememberService.createRemember(command);
            return "create success.";
        });
        ROUTER_MAP.put("create notify", ROUTER_MAP.get("create remember"));

        // 删除记忆 | delete remember - like 'love';
        ROUTER_MAP.put("delete remember", command -> {
            rememberService.deleteRemember(command);
            return "delete success.";
        });
        // 唤醒记忆 | wake up remember
        ROUTER_MAP.put("wake up", command -> rememberService.wakeupRemember(command));
        ROUTER_MAP.put("wake up remember", ROUTER_MAP.get("wake up"));
        ROUTER_MAP.put("wake up remembers", ROUTER_MAP.get("wake up"));

        // ———————————————————— 传话 ———————————————————————
        // TODO
    }

    public void execute(String outgoingToken, String inputContent) {
        execute(outgoingToken, inputContent, null);
    }

    /**
     * 接收消息->逻辑处理->回复自定消息模板 (主要是管理者机器人操作时回复)
     *
     * @param inputContent 用户键入的信息
     */
    public void execute(String outgoingToken, String inputContent, String markdownTitle) {
        Assert.isTrue(StringUtils.isNotBlank(outgoingToken), "outgoingToken is empty.");

        Robotx robotx = ReceivedRobotHolder.pick(outgoingToken);
        if (robotx == null) {
            throw MydreamException.of("无法识别该token:{}", outgoingToken);
        }

        try {
            Pair<String, String> pair = CommonUtil.parseCommand(inputContent);
            Function<Command, String> fun = ROUTER_MAP.get(pair.getLeft());
            if (fun == null) {
                throw MydreamException.of("阿哦～我还不认识[{}]，主人快教教我吧(´･ω･`)", pair.getLeft());
            }
            Command command = Command.builder()
                    .ogt(outgoingToken)
                    .head(pair.getLeft())
                    .body(pair.getRight())
                    .build();
            // 执行调用逻辑，返回消息
            String finalReply = fun.apply(command);
            if (markdownTitle == null) {
                robotx.send(finalReply);
            } else {
                MarkdownDingDTO markdownMsg = MarkdownDingDTO.builder()
                        .title(markdownTitle).markdownText(inputContent).atAll(Boolean.FALSE).build();
                robotx.send(markdownMsg);
            }
        } catch (Exception e) {
            log.error("收发异常 - input:{}, outgoingToken:{}", inputContent, outgoingToken, e);
            robotx.send(e.getMessage());
        }

    }

}
