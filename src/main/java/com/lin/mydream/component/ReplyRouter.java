package com.lin.mydream.component;

import com.lin.mydream.component.helper.TencentChatBotHelper;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.controller.param.CreateRobotParam;
import com.lin.mydream.manager.DingPhoneRelManager;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.RememberService;
import com.lin.mydream.service.RobotService;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.MarkdownDingDTO;
import com.lin.mydream.service.dto.tencent.ReplyDTO;
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
    private DingPhoneRelManager dingPhoneRelManager;
    @Autowired
    private RobotService robotService;
    @Autowired
    private RememberService rememberService;
    @Autowired
    private TencentChatBotHelper tencentChatBotHelper;
    /**
     * 命令路由, command -> Function{Command, bizReply}
     */
    private final Map<String, Function<Command, String>> CMD_ROUTER = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //                            操作机器人
        ////////////////////////////////////////////////////////////////////////
        // 创建机器人 ｜ 1.获取一个outgoingToken ｜ acquire token
        CMD_ROUTER.put("acquire token", command -> CommonUtil.format(
                "your token is: {}, the token is valid for half an hour, please create robot as soon as possible."
                , robotService.getTokenOnPreCreate()));
        CMD_ROUTER.put("acquire outgoing token", CMD_ROUTER.get("acquire token"));

        // 创建机器人 ｜ 2.创建 ｜ create robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN]
        CMD_ROUTER.put("create robot", command -> {
            CreateRobotParam p = CreateRobotParam.of(command);
            Long newRobotId = robotService.createByConfirmingToken(p);
            CommonUtil.format("create success, SEQUENCE ID:[{}], enjoy it.");
            return String.valueOf(newRobotId);
        });

        // 删除机器人 ｜ delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN]
        CMD_ROUTER.put("delete robot", command -> {
            if (!robotService.deleteByAccessToken(command.getBody())) {
                robotService.deleteByOutgoingToken(command.getBody());
            }
            return CommonUtil.format("delete success, bye~");
        });

        /////////////////////////////////////////////////////////////////////////
        //                            私人定制
        ////////////////////////////////////////////////////////////////////////

        // ———————————————————— 注册手机号 ———————————————————————
        // 注册手机号(方便机器人@你) ｜ register phone - '17826833386'
        CMD_ROUTER.put("register phone", command -> {
            Command.MsgContext msgContext = command.getMsgContext();
            String phone = command.getBodies().get(0);
            dingPhoneRelManager.register(msgContext.getSenderId(), phone);
            return CommonUtil.format("registered success! my friend～[{}]", msgContext.getSenderNick());
        });
        // ———————————————————— 记忆 ———————————————————————
        // 列出所有记忆 ｜ list remembers
        CMD_ROUTER.put("list remembers", command -> {
            String rememberString = rememberService.listRemember(command);
            if (StringUtils.isBlank(rememberString)) {
                return "your remembers are empty";
            }
            return CommonUtil.format("your remembers are follows:\n{}", rememberString);
        });
        // 创建循环提醒 | create loop notify - 'publish task' '10/5' '17826833386';
        CMD_ROUTER.put("create loop notify", command -> {
            rememberService.createLoopNotify(command);
            return "create success.";
        });
        // 创建记忆 | create remember/notify - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
        CMD_ROUTER.put("create remember", command -> {
            rememberService.createRemember(command);
            return "create success.";
        });
        CMD_ROUTER.put("create notify", CMD_ROUTER.get("create remember"));

        // 删除记忆 | delete remember - like 'love';
        CMD_ROUTER.put("delete remember", command -> {
            rememberService.deleteRemember(command);
            return "delete success.";
        });
        // 唤醒记忆 | wake up remember
        CMD_ROUTER.put("wake up", command -> rememberService.wakeupRemember(command));
        CMD_ROUTER.put("wake up remember", CMD_ROUTER.get("wake up"));
        CMD_ROUTER.put("wake up remembers", CMD_ROUTER.get("wake up"));

        // ———————————————————— 传话 ———————————————————————
        // TODO
    }

    public void execute(String outgoingToken, Map<String, Object> ctxMap) {
        Map<String, String> contentMap = (Map<String, String>) ctxMap.get("text");

        execute(outgoingToken, contentMap.get("content"), null, ctxMap);
    }

    /**
     * 接收消息->逻辑处理->回复自定消息模板 (主要是管理者机器人操作时回复)
     *
     * @param inputContent 用户键入的信息
     * @param ctxMap       requestBody
     */
    public void execute(String outgoingToken, String inputContent, String markdownTitle, Map<String, Object> ctxMap) {
        Assert.isTrue(StringUtils.isNotBlank(outgoingToken), "outgoingToken is empty.");

        Robotx robotx = ReceivedRobotHolder.pick(outgoingToken);
        if (robotx == null) {
            throw MydreamException.of("无法识别该token:{}", outgoingToken);
        }

        try {
            Pair<String, String> pair = CommonUtil.parseCommand(inputContent);
            String input = pair.getLeft(); // 经过一轮校验后的input
            Function<Command, String> fun = CMD_ROUTER.get(input);
            if (fun == null) {
                // 既然不认识命令那就开启闲聊模式^_^
                ReplyDTO replyDTO = tencentChatBotHelper.chat(input);
                robotx.send(replyDTO.getReply());
                return;
            }
            // 构建command
            Command command = buildCommand(outgoingToken, ctxMap, pair);
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

    /**
     * 构建Command对象
     */
    private Command buildCommand(String outgoingToken, Map<String, Object> ctxMap, Pair<String, String> pair) {
        Command command = Command.builder()
                .ogt(outgoingToken)
                .head(pair.getLeft())
                .body(pair.getRight())
                .build();
        command.setBodies(command.extractKeysFromBody());
        // 消息上下文（包含requestBody）
        command.newMsgContext(ctxMap);
        command.setRobotId(ReceivedRobotHolder.id(outgoingToken));
        return command;
    }

}
