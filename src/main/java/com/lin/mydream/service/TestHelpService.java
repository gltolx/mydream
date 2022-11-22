package com.lin.mydream.service;

import com.lin.mydream.component.schedule.RobotSchedule;
import com.lin.mydream.model.enumerate.RobotEnum;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.Reply;
import com.lin.mydream.util.LogUtil;
import com.lin.mydream.util.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仅作测试和帮助
 */
@Service
public class TestHelpService {

    public static final String HELP_TEXT = "## 初始化机器人\n" +
            "### 规则\n" +
            "\n" +
            "+ 完整命令__须艾特__机器人，例如：@robot1 后再键入以下命令\n" +
            "+ {XXX}，{}表示占位符，XXX表示释义。()表示非必填。\n" +
            "+ 命令头__均支持简写__以及__中文__。例如：list remembers，简写为`lr`，或中文表示为`列出记忆`\n" +
            "\n" +
            "---\n" +
            "\n" +
            "### 创建机器人\n" +
            "\n" +
            "1. 获取令牌：`acquire token`\n" +
            "2. 创建机器人：`create robot - {ACCESS_TOKEN},{SIGN},{OUTGOING_TOKEN}`\n" +
            "3. 注册手机：`register phone - '136****5678'`\n" +
            "\n" +
            "### 删除机器人\n" +
            "\n" +
            "`delete robot - [ACCESS_TOKEN],[OUTGOING_TOKEN]`\n" +
            "\n" +
            "\n" +
            "\n" +
            "## 记忆\n" +
            "\n" +
            "---\n" +
            "\n" +
            "+ 列出记忆：`list remembers`\n" +
            "+ 创建记忆：`create remember - '{TITLE}' '{yyyy-MM-dd}' ('{136****1234},{136****5678}')`\n" +
            "+ 删除记忆：`delete remember - like '{KEYWORD}'`\n" +
            "+ 唤醒记忆：`wake up`\n" +
            "\n" +
            "\n" +
            "\n" +
            "## 提醒\n" +
            "\n" +
            "---\n" +
            "\n" +
            "+ 列出提醒：`list notifies`\n" +
            "\n" +
            "+ 创建提醒：`create notify - '{TITLE}' '{yyyy-MM-dd}' '({136****1234},{136****5678})'`\n" +
            "\n" +
            "+ 创建循环提醒：`create loop notify - '{TITLE}' '{数字：每隔x分钟}/{数字：共提醒y次}' ('17826833386')`\n" +
            "\n" +
            "  > create loop notify - 'publish task' '10/5' '178xxxx3386'\n" +
            "  >\n" +
            "  > 表示每隔10分钟循环提醒，共提醒5次。提醒接收人为178xxxx3386，缺省默认当前命令发送人\n" +
            "\n" +
            "+ 删除提醒：delete notify - like '{KEYWORD}'\n" +
            "\n" +
            "\n" +
            "\n" +
            "## 待续……\n" +
            "\n" +
            "<a href=\"gltolx@163.com\">Click to contact</a>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";

    public Reply doTest(Command command) {
        long startMillis = System.currentTimeMillis();
        LogUtil.info("STARTING do test... command:{}", command.originString());

        String body = command.getBody();
        if (body.startsWith("'schedule::remember'")) {
            RobotSchedule robotSchedule = SpringUtil.getBean(RobotSchedule.class);
            robotSchedule.remember();
        } else if (body.startsWith("'msg::markdown")) {
            List<String> bodies = command.getBodies();
            if (bodies.size() == 3) {
                String title = bodies.get(1);
                String text = bodies.get(2);
                return Reply.of(text, RobotEnum.MsgType.markdown)
                        .mdTitle(title);

            } else {
                return Reply.of("invalid command, body size should be 3");
            }
        }

        LogUtil.info("ENDING do test... spent {}ms", System.currentTimeMillis() - startMillis);
        return Reply.of("completed test");

    }

    public Reply doHelp() {

        return Reply.of(HELP_TEXT, RobotEnum.MsgType.markdown).mdTitle("help-doc.md");
    }
}
