package com.lin.mydream.component;

import com.lin.mydream.component.helper.TencentChatBotHelper;
import com.lin.mydream.consts.MydreamException;
import com.lin.mydream.manager.DingPhoneRelManager;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.model.enumerate.RobotEnum.CMD;
import com.lin.mydream.service.RememberService;
import com.lin.mydream.service.RobotService;
import com.lin.mydream.service.TestService;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.MarkdownDingDTO;
import com.lin.mydream.service.dto.Reply;
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
    private TestService testService;
    @Autowired
    private TencentChatBotHelper tencentChatBotHelper;
    /**
     * 命令路由, command -> Function{Command, bizReply}
     */
    private final Map<CMD, Function<Command, Reply>> CMD_ROUTER = new ConcurrentHashMap<>();

    /**
     * 获取命令所对应的执行方法
     *
     * @return 执行方法
     */
    private Function<Command, Reply> acquire(String cmd) {
        CMD c = CMD.of(cmd);
        return CMD_ROUTER.get(c);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        initCmdRouter();
    }

    private void initCmdRouter() {
        /////////////////////////////////////////////////////////////////////////
        //                            操作机器人
        ////////////////////////////////////////////////////////////////////////
        // 创建机器人 ｜ 1.获取一个outgoingToken ｜ acquire token
        CMD_ROUTER.put(CMD.ACQUIRE_TOKEN, command -> robotService.getTokenOnPreCreate());

        // 创建机器人 ｜ 2.创建 ｜ create robot - [ACCESS_TOKEN],[SIGN],[OUTGOING_TOKEN]
        CMD_ROUTER.put(CMD.CREATE_ROBOT, command -> robotService.createRobot(command));

        // 删除机器人 ｜ delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN]
        CMD_ROUTER.put(CMD.DELETE_ROBOT, command -> robotService.deleteRobot(command));

        /////////////////////////////////////////////////////////////////////////
        //                            私人定制
        ////////////////////////////////////////////////////////////////////////

        // ———————————————————— 注册手机号 ———————————————————————
        // 注册手机号(方便机器人@你) ｜ register phone - '17826833386'
        CMD_ROUTER.put(CMD.REGISTER_PHONE, command -> dingPhoneRelManager.register(command));

        // ———————————————————— 记忆/提醒 ———————————————————————
        // 列出所有记忆 ｜ list remembers
        CMD_ROUTER.put(CMD.LIST_REMEMBERS, command -> rememberService.listRemembers(command));
        // 创建循环提醒 | create loop notify - 'publish task' '10/5' '17826833386';
        CMD_ROUTER.put(CMD.CREATE_LOOP_NOTIFY, command -> rememberService.createLoopNotify(command));
        // 创建记忆 | create remember - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
        CMD_ROUTER.put(CMD.CREATE_REMEMBER, command -> rememberService.createRemember(command, RobotEnum.RememberType.remember));
        // 创建提醒 | create notify - 'fell in love with LMY' '2021-02-14' '17826833386,13639853155';
        CMD_ROUTER.put(CMD.CREATE_NOTIFY, command -> rememberService.createRemember(command, RobotEnum.RememberType.notify));
        // 删除记忆 | delete remember - like 'love';
        CMD_ROUTER.put(CMD.DELETE_REMEMBER, command -> rememberService.deleteRemember(command));
        // 删除提醒 | delete notify - like 'love';
        CMD_ROUTER.put(CMD.DELETE_NOTIFY, CMD_ROUTER.get(CMD.DELETE_REMEMBER));
        // 唤醒记忆 | wake up remember
        CMD_ROUTER.put(CMD.WAKE_UP, command -> rememberService.wakeupRemember(command));

        // ———————————————————— 传话 ———————————————————————
        // TODO

        /////////////////////////////////////////////////////////////////////////
        //                            在线测试
        ////////////////////////////////////////////////////////////////////////
        /*
         * 在线测试 |
         * 定时任务remember测试 | test - 'schedule::remember'
         * md消息测试 | test - 'msg::markdown' 'markdownTitle...' 'markdownText...'
         */
        CMD_ROUTER.put(CMD.TEST, command -> testService.doTest(command));
    }


    public void execute(String outgoingToken, Map<String, Object> ctxMap) {
        Map<String, String> contentMap = (Map<String, String>) ctxMap.get("text");

        execute(outgoingToken, contentMap.get("content"), ctxMap);
    }

    /**
     * 接收消息->逻辑处理->回复自定消息模板 (主要是管理者机器人操作时回复)
     *
     * @param inputContent 用户键入的信息
     * @param ctxMap       requestBody
     */
    public void execute(String outgoingToken, String inputContent, Map<String, Object> ctxMap) {
        Assert.isTrue(StringUtils.isNotBlank(outgoingToken), "outgoingToken is empty.");

        Robotx robotx = ReceivedRobotHolder.pick(outgoingToken);
        if (robotx == null) {
            throw MydreamException.of("无法识别该token:{}", outgoingToken);
        }

        try {
            Pair<String, String> pair = CommonUtil.parseCommand(inputContent);
            String input = pair.getLeft(); // 经过一轮校验后的input
            Function<Command, Reply> fun = this.acquire(input);
            if (fun == null) {
                // 既然不认识命令那就开启闲聊模式^_^
                ReplyDTO replyDTO = tencentChatBotHelper.chat(input);
                robotx.send(replyDTO.getReply());
                return;
            }
            // 构建command
            Command command = buildCommand(outgoingToken, ctxMap, pair);
            // 执行调用逻辑，返回消息
            Reply finalReply = fun.apply(command);
            if (RobotEnum.MsgType.markdown.equals(finalReply.getMsgType())) {
                MarkdownDingDTO markdownMsg = MarkdownDingDTO.builder()
                        .title(finalReply.getMdTitle())
                        .markdownText(finalReply.getContent())
                        .atAll(Boolean.FALSE)
                        .build();
                robotx.send(markdownMsg);
            } else if (RobotEnum.MsgType.link.equals(finalReply.getMsgType())) {
                // TODO write text-link
            } else {
                robotx.send(finalReply.getContent());
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
