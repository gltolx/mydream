package com.lin.mydream.service;

import com.lin.mydream.component.ReceivedRobotHolder;
import com.lin.mydream.component.schedule.RobotSchedule;
import com.lin.mydream.model.Robotx;
import com.lin.mydream.service.dto.Command;
import com.lin.mydream.service.dto.MarkdownDingDTO;
import com.lin.mydream.service.dto.Reply;
import com.lin.mydream.util.LogUtil;
import com.lin.mydream.util.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仅作测试
 */
@Service
public class TestService {

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
                MarkdownDingDTO markdownMsg = MarkdownDingDTO.builder()
                        .title(title).markdownText(text).atAll(Boolean.FALSE).build();
                Robotx robotx = ReceivedRobotHolder.pick(command.getOgt());
                robotx.send(markdownMsg);

            } else {
                return Reply.of("invalid command, body size should be 3");
            }
        }

        LogUtil.info("ENDING do test... spent {}ms", System.currentTimeMillis() - startMillis);
        return Reply.of("completed test");

    }
}
